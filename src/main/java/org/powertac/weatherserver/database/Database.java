package org.powertac.weatherserver.database;

import org.powertac.weatherserver.DateString;
import org.powertac.weatherserver.beans.Energy;
import org.powertac.weatherserver.beans.Forecast;
import org.powertac.weatherserver.beans.Location;
import org.powertac.weatherserver.beans.Weather;
import org.powertac.weatherserver.constants.Constants;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@ManagedBean
@RequestScoped
public class Database {

	// Requested parameters
	private String date = "";

	// Data limiting factors
	private String datemin = "";
	private String datemax = "";
	
	private List<String> locations;

	private String implementation = "";
	private String reportTable = "";
	private String forecastTable = "";
	private String energyTable = "";

	// Connection Related
	private String dbUrl = "";
	private String database = "";
	private String port = "";
	private String username = "";
	private String password = "";
	private String dbms = "";

	// Database Configurations
	private Connection conn = null;
	private PreparedStatement weatherStatement = null;
  private PreparedStatement forecastStatement = null;
	Properties connectionProps = new Properties();
	Properties prop = new Properties();

	public Database() {
		locations = new ArrayList<String>();
		try {
			prop.load(Database.class.getClassLoader().getResourceAsStream(
					"/weatherserver.properties"));

			// Database Connection related properties
			setDatabase(prop.getProperty("db.database"));
			setDbms(prop.getProperty("db.dbms"));
			setPort(prop.getProperty("db.port"));
			setDbUrl(prop.getProperty("db.dbUrl"));
			setUsername(prop.getProperty("db.username"));
			setPassword(prop.getProperty("db.password"));

			// Configuration Related properties
			setDatemin(prop.getProperty("publicDateMin"));
			setDatemax(prop.getProperty("publicDateMax"));
			setLocations(prop.getProperty("publicLocations"));
			
			setImplementation(prop.getProperty("implementation"));
			setReportTable(prop.getProperty("reportTable"));
			setForecastTable(prop.getProperty("forecastTable"));
			setEnergyTable(prop.getProperty("energyTable"));
		}
    catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void checkDb() {
		try {
			if (conn == null || conn.isClosed()) {
				if (dbms.equalsIgnoreCase("mysql")) {
					try {
						connectionProps.setProperty("user", username);
						connectionProps.setProperty("password", password);
						Class.forName ("com.mysql.jdbc.Driver").newInstance();
						conn = DriverManager.getConnection("jdbc:" + dbms
								+ "://" + dbUrl +  "/" + database,
								connectionProps);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

  public List<Location> getLocationList () throws Exception
  {
    checkDb();

    List<Location> result = new ArrayList<Location>();

    PreparedStatement statement = conn.prepareStatement(
        String.format(Constants.DB_AVAILABLE_LOCATIONS));

    ResultSet resultSet = statement.executeQuery();
    while (resultSet.next()) {
      Location location = new Location();
      location.setLocationName(resultSet.getString("location"));
      location.setMaxDate(resultSet.getString("maxDate").replace(":00.0",""));
      location.setMinDate(resultSet.getString("minDate").replace(":00.0",""));
      result.add(location);
    }
    conn.close();

    return result;
  }


	public List<Weather> getWeatherList(String weatherDate, String location)
      throws Exception
  {
		checkDb();

		if (weatherStatement == null || weatherStatement.isClosed()) {
			weatherStatement = conn.prepareStatement(
          String.format(Constants.DB_SELECT_REPORT, reportTable));
		}

    // Make sure we have a valid date
		if (!validDate(weatherDate)) {
			weatherDate = makeValidDate(weatherDate);
		}

		// Check to make sure they are requesting public data
		if (getLocations().contains(location) && validDate(weatherDate)) {
			weatherStatement.setString(1, new DateString(weatherDate).getLocaleString());
			weatherStatement.setString(2, location);

			ResultSet resultSet = weatherStatement.executeQuery();
	
			List<Weather> result = new ArrayList<Weather>();
			while (resultSet.next()) {
				Weather w = new Weather();
				w.setWeatherDate(resultSet.getString("weatherDate").replace(":00.0",""));
				w.setTemp(resultSet.getDouble("temp"));
				w.setWindDir(resultSet.getDouble("windDir") % 360);
				w.setWindSpeed(resultSet.getDouble("windSpeed"));
				w.setCloudCover(resultSet.getDouble("cloudCover"));
				w.setLocation(resultSet.getString("location"));
				result.add(w);
			}
			conn.close();
			return result;
		}
    else {
			return new ArrayList<Weather>();
		}
	}

	public List<Forecast> getForecastList(String weatherDate,
                                        String location)
      throws Exception
  {
    checkDb();

    if (!validDate(weatherDate)) {
      weatherDate = makeValidDate(weatherDate);
    }

    // Procedural implementation does a trend based random walk
		if (implementation.equals("procedural")) {
			DateString beforeDate = new DateString(weatherDate);
			DateString afterDate = new DateString(weatherDate);
      DateString afterAfterDate = new DateString(weatherDate);

			beforeDate.shiftBackDay();
			afterDate.shiftAheadDay();
      afterAfterDate.shiftAheadDay();
      afterAfterDate.shiftAheadDay();

			List<Weather> rollingBefore = getWeatherList(
          beforeDate.getRestString(), location);
			List<Weather> rollingMiddle = getWeatherList(
          weatherDate, location);
			List<Weather> rollingAfter  = getWeatherList(
          afterDate.getRestString(), location);
      List<Weather> rollingAfterAfter  = getWeatherList(
          afterAfterDate.getRestString(), location);

      List<Weather> tmpList;
			Weather[] avgWeather = new Weather[2 * rollingBefore.size()];
			for (int i = 0; i < rollingBefore.size(); i++) {
				tmpList = new ArrayList<Weather>();
				tmpList.add(rollingBefore.get(i));
				tmpList.add(rollingMiddle.get(i));
				tmpList.add(rollingAfter.get(i));
				avgWeather[i] = avgReports(tmpList);
			}
      for (int i = 0; i < rollingBefore.size(); i++) {
        tmpList = new ArrayList<Weather>();
        tmpList.add(rollingMiddle.get(i));
        tmpList.add(rollingAfter.get(i));
        tmpList.add(rollingAfterAfter.get(i));
        avgWeather[rollingBefore.size() + i] = avgReports(tmpList);
      }

      double sigma = Double.parseDouble(prop.getProperty("sigma"));
      DateString origin = new DateString(weatherDate);
      List<Forecast> result = new ArrayList<Forecast>();
      for (int i = 0; i < 24; i++) {
        double tau0Temp = Double.parseDouble(prop.getProperty("tau0"));
        double tau0Dir = Double.parseDouble(prop.getProperty("tau0"));
        double tau0Speed = Double.parseDouble(prop.getProperty("tau0"));
        double tau0Cloud = Double.parseDouble(prop.getProperty("tau0"));

        for (int j = 0; j < 24; j++) {
          tau0Temp  = tau0Temp  * ((1.0/sigma - sigma) * Math.random() + sigma);
          tau0Dir   = tau0Dir   * ((1.0/sigma - sigma) * Math.random() + sigma);
          tau0Speed = tau0Speed * ((1.0/sigma - sigma) * Math.random() + sigma);
          tau0Cloud = tau0Cloud * ((1.0/sigma - sigma) * Math.random() + sigma);

          Weather weather = avgWeather[i + j + 1];

          Forecast tmpForecast = new Forecast();
          tmpForecast.setId(j);
          tmpForecast.setLocation(location);
          tmpForecast.setOrigin(origin.getLocaleString().substring(0, 16));
          tmpForecast.setTemp(weather.getTemp() * tau0Temp);
          tmpForecast.setWindSpeed(weather.getWindSpeed() * tau0Speed);
          tmpForecast.setWindDir((weather.getWindDir() * tau0Dir) % 360);
          tmpForecast.setCloudCover(Math.min(1, Math.max(0, weather.getCloudCover() * tau0Cloud)));
          result.add(tmpForecast);
        }

        origin.shiftAheadHour();
      }

			return result;
		}
    else if (implementation.equals("database")) {
      if (forecastStatement == null || forecastStatement.isClosed()) {
        forecastStatement = conn.prepareStatement(
            String.format(Constants.DB_SELECT_FORECAST, forecastTable));
      }

      if (!getLocations().contains(location) || !validDate(weatherDate)) {
        return null;
      }
      forecastStatement.setString(1, new DateString(weatherDate)
          .getLocaleString());
      forecastStatement.setString(2, location);

      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00.0");

      List<Forecast> result = new ArrayList<Forecast>();
      ResultSet resultSet = forecastStatement.executeQuery();
      while (resultSet.next()) {
        long diff = sdf.parse(resultSet.getString("weatherDate")).getTime() -
            sdf.parse(resultSet.getString("origin")).getTime();

        Forecast tmpForecast = new Forecast();
        tmpForecast.setId((int) diff / (1000 * 60 * 60) - 1);
        tmpForecast.setLocation(location);
        tmpForecast.setWeatherDate
            (resultSet.getString("weatherDate").replace(":00.0", ""));
        tmpForecast.setOrigin(
            resultSet.getString("origin").replace(":00.0", ""));
        tmpForecast.setTemp(Double.parseDouble(resultSet.getString("temp")));
        tmpForecast.setWindSpeed(
            Double.parseDouble(resultSet.getString("windspeed")));
        tmpForecast.setWindDir(
            Double.parseDouble(resultSet.getString("windDir")));
        tmpForecast.setCloudCover(
            Double.parseDouble(resultSet.getString("cloudcover")));

        result.add(tmpForecast);
      }
      conn.close();

      return result;
		}
		
		return null;
	}

	public List<Energy> getEnergyList (String weatherDate, String weatherLocation)
  {
		return null;
	}
	
	private Weather avgReports (List<Weather> weathers)
  {
		Weather tmpWeather = new Weather();
		Double newTemp = 0.0;
		Double newWindDir = 0.0;
		Double newWindSpeed = 0.0;
		Double newCloudCover = 0.0;
    String date = "";

    int count = 0;
		for (Weather w : weathers) {
			tmpWeather.setLocation(w.getLocation());
			
			newTemp += w.getTemp();
			newWindDir += w.getWindDir();
			newWindSpeed += w.getWindSpeed();
			newCloudCover += w.getCloudCover();

      if (Math.floor(weathers.size() / 2) == count++) {
        date = w.getWeatherDate();
      }
		}
		
		tmpWeather.setTemp(newTemp/weathers.size());
		tmpWeather.setWindDir(newWindDir/weathers.size());
		tmpWeather.setWindSpeed(newWindSpeed/weathers.size());
		tmpWeather.setCloudCover(newCloudCover/weathers.size());
    tmpWeather.setWeatherDate(date);

		return tmpWeather;
	}
	
	public boolean validDate (String date)
  {
		Date sqlDatemin = new DateString(getDatemin()).getSqlDate();
		Date sqlDatemax = new DateString(getDatemax()).getSqlDate();
		Date testDate = new DateString(date).getSqlDate();

		return (!testDate.after(sqlDatemax) && !testDate.before(sqlDatemin));
	}
	
	public String makeValidDate (String date)
  {
		Date sqlDatemin = new DateString(getDatemin()).getSqlDate();
		Date sqlDatemax = new DateString(getDatemax()).getSqlDate();
		Date testDate = new DateString(date).getSqlDate();
		
		if (testDate.before(sqlDatemin)) {
      String newDate = date.substring(0,5) + getDatemin().substring(5,10);
      if (validDate(newDate)) {
        return newDate;
      }
      else {
        return getDatemin();
      }
		}
    else if (testDate.after(sqlDatemax)) {
      String newDate = date.substring(0,5) + getDatemax().substring(5,10);
      if (validDate(newDate)) {
        return newDate;
      }
      else {
        return getDatemax();
      }
		}
    else {
			return date;
		}
	}

  //<editor-fold desc="Setters and Getters">
  public String getDate() {
    return date;
  }
  public void setDate(String date) {
    this.date = date;
  }

  public String getDatabase() {
    return database;
  }
  public void setDatabase(String database) {
    this.database = database;
  }

  public String getPort() {
    return port;
  }
  public void setPort(String port) {
    this.port = port;
  }

  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  public String getDbUrl() {
    return dbUrl;
  }
  public void setDbUrl(String dbUrl) {
    this.dbUrl = dbUrl;
  }

  public String getDbms() {
    return dbms;
  }
  public void setDbms(String dbms) {
    this.dbms = dbms;
  }

  public String getDatemin() {
    return datemin;
  }
  public void setDatemin(String datemin) {
    this.datemin = datemin;
  }

  public String getDatemax() {
    return datemax;
  }
  public void setDatemax(String datemax) {
    this.datemax = datemax;
  }

  public String getReportTable() {
    return reportTable;
  }
  public void setReportTable(String reportTable) {
    this.reportTable = reportTable;
  }

  public String getForecastTable() {
    return forecastTable;
  }
  public void setForecastTable(String forecastTable) {
    this.forecastTable = forecastTable;
  }

  public String getEnergyTable() {
    return energyTable;
  }
  public void setEnergyTable(String energyTable) {
    this.energyTable = energyTable;
  }

  public List<String> getLocations() {
    return locations;
  }
  public void setLocations(String locations) {
    for(String s :locations.split(",")){
      this.locations.add(s.trim());
    }
  }

  public String getImplementation() {
		return implementation;
	}
	public void setImplementation(String implementation) {
		this.implementation = implementation;
	}
  //</editor-fold>
}

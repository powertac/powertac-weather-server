package org.powertac.weatherserver.database;

import org.powertac.weatherserver.DateString;
import org.powertac.weatherserver.beans.Energy;
import org.powertac.weatherserver.beans.Forecast;
import org.powertac.weatherserver.beans.Weather;
import org.powertac.weatherserver.constants.Constants;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@ManagedBean
@RequestScoped
public class Database {

	// Conversions parms
	private Double fromMpsToMs = 0.44704;
	
	// Requested parameters
	private String date = "";
	private String id = "";

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

	public List<Weather> getWeatherList(String weatherDate, String location)
      throws Exception {
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
			Weather lastWeather = new Weather();
			lastWeather.setWeatherId(String.valueOf(-1));
			lastWeather.setWeatherDate(String.valueOf("null"));
			lastWeather.setLocation("minneapolis");

			while (resultSet.next()) {
				Weather w = new Weather();
				w.setWeatherId(resultSet.getString("weatherId"));
				w.setWeatherDate(resultSet.getString("weatherDate"));
				w.setTemp(
            resultSet.getString("temp").contains("**")
                ?lastWeather.getTemp()
                :String.valueOf((resultSet.getDouble("temp")-32.0d)* 5.0d/9.0d));
				w.setWindDir(
            String.valueOf(resultSet.getString("windDir").contains("**")
                ?lastWeather.getWindDir()
                :resultSet.getDouble("windDir")>=360?0:resultSet.getDouble("windDir")));
				w.setWindSpeed(
            resultSet.getString("windSpeed").contains("**")
                ?lastWeather.getWindSpeed()
                :String.valueOf(resultSet.getDouble("windSpeed")*fromMpsToMs));
				w.setCloudCover(
            resultSet.getString("cloudCover").contains("**")
                ?lastWeather.getCloudCover()
                :resultSet.getString("cloudCover"));
				w.setLocation(resultSet.getString("location"));
				result.add(w);
				lastWeather = w;
			}
			conn.close();
			return result;
		}
    else {
			return new ArrayList<Weather>();
		}
	}

	public List<Forecast> getForecastList(String weatherDate,
                                        String weatherLocation)
      throws Exception
  {
		checkDb();

    if (!validDate(weatherDate)) {
      weatherDate = makeValidDate(weatherDate);
    }
		
		// Procedural implementation does a trend based random walk
		if (implementation.equals("procedural")) {
			double tau0 = Double.parseDouble(prop.getProperty("tau0"));
			double sigma = Double.parseDouble(prop.getProperty("sigma"));
			
			DateString beforeDate = new DateString(weatherDate);
			DateString afterDate = new DateString(weatherDate);
			
			beforeDate.shiftBackDay();
			afterDate.shiftAheadDay();

			List<Weather> rollingBefore = getWeatherList(beforeDate.getRestString(), weatherLocation);
			List<Weather> rollingMiddle = getWeatherList(weatherDate, weatherLocation);
			List<Weather> rollingAfter  = getWeatherList(afterDate.getRestString(), weatherLocation);

			Weather[] avgWeather = new Weather[rollingBefore.size()];
			for (int i = 0; i < avgWeather.length ; i++) {
				List<Weather> tmpList = new ArrayList<Weather>();
				tmpList.add(rollingBefore.get(i));
				tmpList.add(rollingMiddle.get(i));
				tmpList.add(rollingAfter.get(i));
				avgWeather[i] = avgReports(tmpList);
			}

			List<Forecast> result = new ArrayList<Forecast>();
			
			Forecast tmpForecast;
			DateString forecastDate = new DateString(weatherDate);
			int forecastId=0;
			for (int i=0; i<2; i++) {
				for (Weather w : avgWeather) {
					tmpForecast = new Forecast();
					tmpForecast.setWeatherId(String.valueOf(forecastId++));
					tmpForecast.setLocation(w.getLocation());
					
					tmpForecast.setWeatherDate(forecastDate.getLocaleString());
					
					Double newTemp = Double.parseDouble(w.getTemp()) * tau0;
					tmpForecast.setTemp(newTemp.toString());
					
					Double newWindDir = (Double.parseDouble(
              (w.getWindDir().equalsIgnoreCase("***")?"0":w.getWindDir())) * tau0)%360;
					tmpForecast.setWindDir(newWindDir.toString());
					
					Double newWindSpeed = Double.parseDouble(
              (w.getWindSpeed().equalsIgnoreCase("***")?"0":w.getWindSpeed())) * tau0;
					tmpForecast.setWindSpeed(newWindSpeed.toString());
					
					String newCloudCover = clampCloudCover(
              getCloudCoverValue(w.getCloudCover()) * tau0 );
					tmpForecast.setCloudCover(newCloudCover);
					
					result.add(tmpForecast);

					tau0 = tau0 * ((1.0/sigma - sigma)*Math.random() + sigma);
					forecastDate.shiftAheadHour();
				}
			}
			
			return result;
		}
    else {
			// TODO: Implement database query for empirical data
		}
		
		return null;
	}

	public List<Energy> getEnergyList (String weatherDate, String weatherLocation)
  {
		checkDb();
		return null;
	}
	
	private Weather avgReports (List<Weather> weathers)
  {
		Weather tmpWeather = new Weather();
		Double newTemp = 0.0;
		Double newWindDir = 0.0;
		Double newWindSpeed = 0.0;
		Double newCloudCover = 0.0;
		
		for (Weather w : weathers) {
			tmpWeather.setLocation(w.getLocation());
			
			newTemp += Double.parseDouble(
          (w.getTemp().contains("***") ? "0" : w.getTemp()));
			newWindDir += Double.parseDouble(
          (w.getWindDir().contains("***") ? "0" : w.getWindDir()));
			newWindSpeed += Double.parseDouble(
          (w.getWindSpeed().contains("***") ? "0" : w.getWindSpeed()));
			newCloudCover += getCloudCoverValue(
          w.getCloudCover().contains("***") ? "0" : w.getCloudCover());
		}
		
		tmpWeather.setTemp(String.valueOf(newTemp/weathers.size()));
		tmpWeather.setWindDir(String.valueOf(newWindDir/weathers.size()));
		tmpWeather.setWindSpeed(String.valueOf(newWindSpeed/weathers.size()));
		tmpWeather.setCloudCover(clampCloudCover(newCloudCover/weathers.size()));
		
		return tmpWeather;
	}
	
	private double getCloudCoverValue (String s)
  {
		if (s.equalsIgnoreCase("clr")) {
			return 0.0;
		}
    else if (s.equalsIgnoreCase("sct")) {
			return 3.0/8.0;
		}
    else if (s.equalsIgnoreCase("bkn")) {
			return 6.0/8.0;
		}
    else if (s.equalsIgnoreCase("ovc")) {
			return 1.0;
		}
    else if (s.equalsIgnoreCase("obs")) {
			return 1.0;
		}
    else {
			return 1.0;
		}
	}
	
	private String clampCloudCover (Double value)
  {
		if (value < 1.0/8.0) {
			return "CLR";
		}
    else if (value < 4.0/8.0) {
			return "SCT";
		}
    else if (value < 7.0/8.0) {
			return "BKN";
		}
    else {
			return "OVC";
		}
	}

	public boolean validDate (String date)
  {
		Date sqlDatemin = new DateString(getDatemin()).getSqlDate();
		Date sqlDatemax = new DateString(getDatemax()).getSqlDate();
		Date testDate = new DateString(date).getSqlDate();

		return (!testDate.after(sqlDatemax) || !testDate.before(sqlDatemin));
	}
	
	public String makeValidDate (String date)
  {
		Date sqlDatemin = new DateString(getDatemin()).getSqlDate();
		Date sqlDatemax = new DateString(getDatemax()).getSqlDate();
		Date testDate = new DateString(date).getSqlDate();
		
		if (testDate.before(sqlDatemin)) {
			return date.substring(0,5) + getDatemin().substring(5,10);
		}
    else if (testDate.after(sqlDatemax)) {
			return date.substring(0,5) + getDatemax().substring(5,10);
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

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
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

package org.powertac.weatherserver.database;

import org.powertac.weatherserver.Properties;
import org.powertac.weatherserver.WeatherDate;
import org.powertac.weatherserver.beans.Energy;
import org.powertac.weatherserver.beans.Forecast;
import org.powertac.weatherserver.beans.Location;
import org.powertac.weatherserver.beans.Weather;
import org.powertac.weatherserver.constants.Constants;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@ManagedBean
@RequestScoped
public class Database {
	private Connection conn = null;
	private PreparedStatement weatherStatement = null;
  private PreparedStatement forecastStatement = null;

	private Properties properties = new Properties();

	public Database ()
  {
    checkDb();
	}

	private void checkDb() {
		try {
			if (conn == null || conn.isClosed()) {
				if (properties.getProperty("db.dbms").equalsIgnoreCase("mysql")) {
					try {
						Class.forName ("com.mysql.jdbc.Driver").newInstance();
            String url = String.format("jdbc:%s://%s/%s",
                properties.getProperty("db.dbms"),
                properties.getProperty("db.dbUrl"),
                properties.getProperty("db.database"));
						conn = DriverManager.getConnection(url,
                properties.getProperty("db.username"),
                properties.getProperty("db.password"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

  public void close ()
  {
    try {
      if (conn != null && !conn.isClosed()) {
        conn.close();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<Location> getLocationList () throws Exception
  {
    checkDb();

    List<Location> result = new ArrayList<Location>();

    PreparedStatement statement = conn.prepareStatement(
        String.format(Constants.DB.AVAILABLE_LOCATIONS));

    ResultSet resultSet = statement.executeQuery();
    while (resultSet.next()) {
      WeatherDate max = new WeatherDate(resultSet, "maxDate");
      WeatherDate min = new WeatherDate(resultSet, "minDate");

      Location location = new Location();
      location.setLocationName(resultSet.getString("location"));
      location.setMaxDate(max.getMediumString());
      location.setMinDate(min.getMediumString());
      result.add(location);
    }

    return result;
  }

	public List<Weather> getWeatherList (WeatherDate weatherDate,
                                       Location location)
      throws Exception
  {
		checkDb();

		if (weatherStatement == null || weatherStatement.isClosed()) {
			weatherStatement = conn.prepareStatement(
          String.format(Constants.DB.SELECT_REPORT,
              properties.getProperty("reportTable")));
		}

    weatherStatement.setString(1, location.getLocationName());
    weatherStatement.setString(2, weatherDate.getFullString());

    ResultSet resultSet = weatherStatement.executeQuery();

    List<Weather> result = new ArrayList<Weather>();
    while (resultSet.next()) {
      WeatherDate date1 = new WeatherDate(resultSet, "weatherDate");

      Weather w = new Weather();
      w.setWeatherDate(date1.getMediumString());
      w.setTemp(resultSet.getDouble("temp"));
      w.setWindDir(resultSet.getDouble("windDir") % 360);
      w.setWindSpeed(resultSet.getDouble("windSpeed"));
      w.setCloudCover(resultSet.getDouble("cloudCover"));
      w.setLocation(location.getLocationName());
      result.add(w);
    }
    return result;
	}

	public List<Forecast> getForecastList (WeatherDate weatherDate,
                                         Location location)
      throws Exception
  {
		if (properties.getProperty("implementation").equals("procedural")) {
      return getForecastListProcedural(weatherDate, location);
		}
    else if (properties.getProperty("implementation").equals("database")) {
      return getForecastListDatabase(weatherDate, location);
		}
		
		return null;
	}

  private List<Forecast> getForecastListProcedural (WeatherDate weatherDate,
                                                    Location location)
      throws Exception
  {
    // Procedural implementation does a trend based random walk
    WeatherDate newDate = new WeatherDate(weatherDate.getSmallString(), true);
    newDate.shiftBackDay();
    List<Weather> rollingBefore = getWeatherList(newDate, location);
    newDate.shiftAheadDay();
    List<Weather> rollingMiddle = getWeatherList(newDate, location);
    newDate.shiftAheadDay();
    List<Weather> rollingAfter = getWeatherList(newDate, location);
    newDate.shiftAheadDay();
    List<Weather> rollingAfterAfter = getWeatherList(newDate, location);

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

    double sigma = Double.parseDouble(properties.getProperty("sigma"));
    WeatherDate origin = new WeatherDate(weatherDate.getSmallString(), true);
    List<Forecast> result = new ArrayList<Forecast>();

    for (int i = 0; i < 24; i++) {
      double tau0Temp = Double.parseDouble(properties.getProperty("tau0"));
      double tau0Dir = Double.parseDouble(properties.getProperty("tau0"));
      double tau0Speed = Double.parseDouble(properties.getProperty("tau0"));
      double tau0Cloud = Double.parseDouble(properties.getProperty("tau0"));

      for (int j = 0; j < 24; j++) {
        tau0Temp  = tau0Temp  * ((1.0/sigma - sigma) * Math.random() + sigma);
        tau0Dir   = tau0Dir   * ((1.0/sigma - sigma) * Math.random() + sigma);
        tau0Speed = tau0Speed * ((1.0/sigma - sigma) * Math.random() + sigma);
        tau0Cloud = tau0Cloud * ((1.0/sigma - sigma) * Math.random() + sigma);

        Weather weather = avgWeather[i + j + 1];

        Forecast tmpForecast = new Forecast();
        tmpForecast.setId(j);
        tmpForecast.setLocation(location.getLocationName());
        tmpForecast.setWeatherDate(weather.getWeatherDate());
        tmpForecast.setOrigin(origin.getMediumString());
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

  private List<Forecast> getForecastListDatabase (WeatherDate weatherDate,
                                                  Location location)
      throws Exception
  {
    checkDb();

    if (forecastStatement == null || forecastStatement.isClosed()) {
      forecastStatement = conn.prepareStatement(
          String.format(Constants.DB.SELECT_FORECAST,
              properties.getProperty("forecastTable")));
    }

    forecastStatement.setString(1, location.getLocationName());
    forecastStatement.setString(2, weatherDate.getFullString());

    List<Forecast> result = new ArrayList<Forecast>();
    ResultSet resultSet = forecastStatement.executeQuery();
    while (resultSet.next()) {
      WeatherDate date = new WeatherDate(resultSet, "weatherDate");
      WeatherDate origin = new WeatherDate(resultSet, "origin");

      long diff = (date.getTime() - origin.getTime()) / (3600 * 1000);

      Forecast tmpForecast = new Forecast();
      tmpForecast.setId((int) diff - 1);
      tmpForecast.setLocation(location.getLocationName());
      tmpForecast.setWeatherDate(date.getMediumString());
      tmpForecast.setOrigin(origin.getMediumString());
      tmpForecast.setTemp(Double.parseDouble(resultSet.getString("temp")));
      tmpForecast.setWindSpeed(
          Double.parseDouble(resultSet.getString("windspeed")));
      tmpForecast.setWindDir(
          Double.parseDouble(resultSet.getString("windDir")));
      tmpForecast.setCloudCover(
          Double.parseDouble(resultSet.getString("cloudcover")));

      result.add(tmpForecast);
    }
    return result;
  }

	public List<Energy> getEnergyList (WeatherDate weatherDate,
                                     Location weatherLocation)
  {
    // TODO Add isEmpty() for energys in Parser.parseRestRequest 1st clause
    List<Energy> result = new ArrayList<Energy>();
		return result;
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
}

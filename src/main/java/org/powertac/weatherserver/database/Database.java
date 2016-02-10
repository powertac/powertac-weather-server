package org.powertac.weatherserver.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.powertac.weatherserver.Properties;
import org.powertac.weatherserver.WeatherDate;
import org.powertac.weatherserver.beans.Energy;
import org.powertac.weatherserver.beans.Forecast;
import org.powertac.weatherserver.beans.Location;
import org.powertac.weatherserver.beans.Weather;
import org.powertac.weatherserver.constants.Constants;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@ManagedBean
@RequestScoped
public class Database
{
  private static ComboPooledDataSource ds;

  private Properties properties = new Properties();

  public Database ()
  {
    if (ds == null) {
      try {
        System.out.println("Configuring connection pool");
        ds = new ComboPooledDataSource();
        ds.setDriverClass("com.mysql.jdbc.Driver");
        ds.setJdbcUrl(String.format("jdbc:mysql://%s/%s?autoReconnect=true",
            properties.getProperty("db.dbUrl"),
            properties.getProperty("db.database")));
        ds.setUser(properties.getProperty("db.username"));
        ds.setPassword(properties.getProperty("db.password"));
        ds.setMinPoolSize(3);
        ds.setMaxPoolSize(10);
        ds.setTestConnectionOnCheckin(false);
        ds.setTestConnectionOnCheckout(true);
      }
      catch (PropertyVetoException pve) {
        pve.printStackTrace();
      }
    }
  }

  private void closeAll (Connection conn, PreparedStatement ps, ResultSet rs)
  {
    try {
      if (rs != null) {
        rs.close();
      }
    }
    catch (SQLException ignored) {
    }
    try {
      if (ps != null) {
        ps.close();
      }
    }
    catch (SQLException ignored) {
    }
    try {
      if (conn != null && !conn.isClosed()) {
        conn.close();
      }
    }
    catch (SQLException ignored) {
      ignored.printStackTrace();
    }
  }

  public List<Location> getLocationList () throws Exception
  {
    List<Location> result = new ArrayList<Location>();

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conn = ds.getConnection();
      ps = conn.prepareStatement(Constants.DB.AVAILABLE_LOCATIONS);

      rs = ps.executeQuery();
      while (rs.next()) {
        WeatherDate max = new WeatherDate(rs, "maxDate");
        WeatherDate min = new WeatherDate(rs, "minDate");

        Location location = new Location();
        location.setLocationName(rs.getString("location"));
        location.setMaxDate(max.getMediumString());
        location.setMinDate(min.getMediumString());
        result.add(location);
      }
    }
    finally {
      closeAll(conn, ps, rs);
    }

    return result;
  }

  private static AtomicInteger counterReport = new AtomicInteger(0);
  private static AtomicInteger counterForecast = new AtomicInteger(0);
  private static int counter;

  public synchronized List<Weather> getWeatherList (WeatherDate weatherDate,
                                                    Location location)
      throws Exception
  {
    List<Weather> result = new ArrayList<Weather>();

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conn = ds.getConnection();
      ps = conn.prepareStatement(String.format(
          Constants.DB.SELECT_REPORT, properties.getProperty("reportTable")));

      ps.setString(1, location.getLocationName());
      ps.setString(2, weatherDate.getFullString());

      rs = ps.executeQuery();

      while (rs.next()) {
        WeatherDate date1 = new WeatherDate(rs, "weatherDate");

        Weather w = new Weather();
        w.setWeatherDate(date1.getMediumString());
        w.setTemp(rs.getDouble("temp"));
        w.setWindDir(rs.getDouble("windDir") % 360);
        w.setWindSpeed(rs.getDouble("windSpeed"));
        w.setCloudCover(rs.getDouble("cloudCover"));
        w.setLocation(location.getLocationName());
        result.add(w);
      }
    }
    finally {
      closeAll(conn, ps, rs);
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

      for (int j = 1; j <= 24; j++) {
        tau0Temp = tau0Temp * ((1.0 / sigma - sigma) * Math.random() + sigma);
        tau0Dir = tau0Dir * ((1.0 / sigma - sigma) * Math.random() + sigma);
        tau0Speed = tau0Speed * ((1.0 / sigma - sigma) * Math.random() + sigma);
        tau0Cloud = tau0Cloud * ((1.0 / sigma - sigma) * Math.random() + sigma);

        Weather weather = avgWeather[i + j];

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

  private synchronized List<Forecast> getForecastListDatabase (WeatherDate weatherDate,
                                                               Location location)
      throws Exception
  {
    List<Forecast> result = new ArrayList<Forecast>();

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conn = ds.getConnection();
      ps = conn.prepareStatement(String.format(Constants.DB.SELECT_FORECAST,
          properties.getProperty("forecastTable")));

      ps.setString(1, location.getLocationName());
      ps.setString(2, weatherDate.getFullString());

      rs = ps.executeQuery();

      while (rs.next()) {
        WeatherDate date = new WeatherDate(rs, "weatherDate");
        WeatherDate origin = new WeatherDate(rs, "origin");

        long diff = (date.getTime() - origin.getTime()) / (3600 * 1000);

        Forecast tmpForecast = new Forecast();
        tmpForecast.setId((int) diff);
        tmpForecast.setLocation(location.getLocationName());
        tmpForecast.setWeatherDate(date.getMediumString());
        tmpForecast.setOrigin(origin.getMediumString());
        tmpForecast.setTemp(Double.parseDouble(rs.getString("temp")));
        tmpForecast.setWindSpeed(
            Double.parseDouble(rs.getString("windspeed")));
        tmpForecast.setWindDir(
            Double.parseDouble(rs.getString("windDir")));
        tmpForecast.setCloudCover(
            Double.parseDouble(rs.getString("cloudcover")));

        result.add(tmpForecast);
      }
    }
    finally {
      closeAll(conn, ps, rs);
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

    tmpWeather.setTemp(newTemp / weathers.size());
    tmpWeather.setWindDir(newWindDir / weathers.size());
    tmpWeather.setWindSpeed(newWindSpeed / weathers.size());
    tmpWeather.setCloudCover(newCloudCover / weathers.size());
    tmpWeather.setWeatherDate(date);

    return tmpWeather;
  }
}

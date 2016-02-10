package org.powertac.weatherserver.constants;

public class Constants
{
  public class Rest
  {
    // Possible Rest Parameters
    public static final String REQ_PARAM_WEATHER_DATE = "weatherDate";
    public static final String REQ_PARAM_WEATHER_LOCATION = "weatherLocation";
    public static final String REQ_PARAM_TYPE = "type";
  }

  public class DB
  {
    // Strings for DB prepared statements
    public static final String SELECT_REPORT =
        "SELECT weatherDate, temp, windSpeed, windDir,cloudCover "
            + "FROM %s WHERE location=? AND weatherDate>=? "
            + "ORDER BY weatherDate LIMIT 24;";
    public static final String SELECT_FORECAST =
        "SELECT weatherDate, origin, temp, windSpeed, windDir, cloudCover "
            + "FROM %s WHERE location=? AND origin>=? "
            + "ORDER BY origin, weatherDate LIMIT 576;";
    public static final String SELECT_ENERGY =
        "SELECT energyDate, price "
            + "FROM %s WHERE location=? AND (energyId=? OR energyDate=?) "
            + "LIMIT 24;";
    public static final String AVAILABLE_LOCATIONS =
        "SELECT location, minDate, maxDate FROM reports AS a, "
            + "(SELECT MIN(weatherDate) AS minDate, "
            + "        MAX(weatherDate) AS maxDate  "
            + " FROM   reports) AS m "
            + "WHERE m.maxDate = a.weatherDate "
            + "   OR m.minDate = a.weatherDate "
            + "group by location;";
  }
}

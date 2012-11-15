package org.powertac.weatherserver.constants;

public class Constants {
	// Possible Rest Parameters
	public static final String REQ_PARAM_WEATHER_DATE = "weatherDate";
	public static final String REQ_PARAM_WEATHER_LOCATION = "weatherLocation";
	public static final String REQ_PARAM_TYPE = "type";
	
	// Strings for DB prepared statements
	public static final String DB_SELECT_REPORT =
      "SELECT weatherDate, temp, windSpeed, windDir,cloudCover, "
          + "location FROM %s WHERE weatherDate>=? AND location=? LIMIT 24";
	public static final String DB_SELECT_FORECAST =
      "SELECT weatherDate, temp, windSpeed, windDir,cloudCover FROM "
          + "%s WHERE weatherDate>=? AND location=? LIMIT 24";
	public static final String DB_SELECT_ENERGY =
      "SELECT energyDate, price, FROM %s WHERE energyId=? OR "
          + "energyDate=? AND location=? LIMIT 24";
}

package org.powertac.weatherserver.constants;

public class Constants {
	// Possible Rest Parameters
	public static final String REQ_PARAM_WEATHER_ID = "weatherId";
	public static final String REQ_PARAM_WEATHER_DATE = "weatherDate";
	public static final String REQ_PARAM_WEATHER_LOCATION = "weatherLocation";
	public static final String REQ_PARAM_TYPE = "type";
	
	// XML Generation Parameters
	// TODO: Using xml libraries to do this properly
	public static final String RESPONSE_ROOT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<data>\n%s</data>";
	public static final String RESPONSE_REPORT_ROOT = "<weatherReports>\n%s</weatherReports>\n";
	public static final String RESPONSE_FORECAST_ROOT = "<weatherForecasts>\n%s</weatherForecasts>\n";
	public static final String RESPONSE_ENERGY_ROOT = "<energyReports>\n%s</energyReports>\n";
	
	public static final String RESPONSE_WEATHER_REPORT = "<weatherReport id=\"%s\" date=\"%s\" temp=\"%s\" windspeed=\"%s\" winddir=\"%s\" cloudcover=\"%s\" location=\"%s\" />\n";
	public static final String RESPONSE_WEATHER_FORECAST = "<weatherForecast id=\"%s\" date=\"%s\" temp=\"%s\" windspeed=\"%s\" winddir=\"%s\" cloudcover=\"%s\" location=\"%s\" />\n";
	public static final String RESPONSE_ENERGY_REPORT = "<energyReport id=\"%s\" date=\"%s\" price=\"%s\" location=\"%s\"  />\n";
	
	// Strings for DB prepared statements
	public static final String DB_SELECT_REPORT = "SELECT weatherId, weatherDate, temp, windSpeed, windDir,cloudCover, location FROM %s WHERE weatherDate>=? AND location=? LIMIT 24";
	public static final String DB_SELECT_FORECAST = "SELECT weatherId, weatherDate, temp, windSpeed, windDir,cloudCover FROM %s WHERE weatherDate>=? AND location=? LIMIT 24";
	public static final String DB_SELECT_ENERGY = "SELECT energyId, energyDate, price, FROM %s WHERE energyId=? OR energyDate=? AND location=? LIMIT 24";
	

}

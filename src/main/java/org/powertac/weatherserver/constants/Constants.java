package org.powertac.weatherserver.constants;

public class Constants {
	public static final String REQ_PARAM_WEATHER_ID = "weatherId";
	public static final String REQ_PARAM_WEATHER_DATE = "weatherDate";
	public static final String REQ_PARAM_WEATHER_LOCATION = "weatherLocation";
	public static final String REQ_PARAM_TYPE = "type";
	
	public static final String RESPONSE_ROOT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<data>\n%s</data>";
	public static final String RESPONSE_REPORT_ROOT = "<weatherReports>\n%s</weatherReports>\n";
	public static final String RESPONSE_FORECAST_ROOT = "<weatherForecasts>\n%s</weatherForecasts>\n";
	public static final String RESPONSE_ENERGY_ROOT = "<energyReports>\n%s</energyReports>\n";
	
	public static final String RESPONSE_WEATHER_REPORT = "<weatherReport id=\"%s\" date=\"%s\" temp=\"%s\" windspeed=\"%s\" winddir=\"%s\" cloudcover=\"%s\" />\n";
	public static final String RESPONSE_WEATHER_FORECAST = "<weatherForecast id=\"%s\" date=\"%s\" temp=\"%s\" windspeed=\"%s\" winddir=\"%s\" cloudcover=\"%s\" />\n";
	public static final String RESPONSE_ENERGY_REPORT = "<energyReport id=\"%s\" date=\"%s\" price=\"%s\"/>\n";
	

}

package org.powertac.weatherserver.beans;

public class Forecast {
	private String weatherId;
	private String weatherDate;
	private String temp;
	private String windDir;
	private String windSpeed;
	private String cloudCover;
	private String location;
	
	public Forecast ()
  {
		weatherId = "0";
		weatherDate = "0000000000";
		temp = "0";
		windDir = "0";
		windSpeed = "0";
		cloudCover = "CLR";
		location = "NONE";
	}
	
	public String getWeatherId() {
		return weatherId;
	}
	public void setWeatherId(String weatherId) {
		this.weatherId = weatherId;
	}
	public String getWeatherDate() {
		return weatherDate;
	}
	public void setWeatherDate(String weatherDate) {
		this.weatherDate = weatherDate;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getWindDir() {
		return windDir;
	}
	public void setWindDir(String windDir) {
		this.windDir = windDir;
	}
	public String getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
	}
	public String getCloudCover() {
		return cloudCover;
	}
	public void setCloudCover(String cloudCover) {
		this.cloudCover = cloudCover;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
}

package org.powertac.weatherserver.beans;

public class Weather {
	private String weatherDate;
  private String location;

	private Double temp;
	private Double windDir;
	private Double windSpeed;
	private Double cloudCover;

	public Weather ()
  {
		weatherDate = "0000000000";
		temp = 0.0;
		windDir = 0.0;
		windSpeed = 0.0;
		cloudCover = 0.0;
		location = "NONE";
	}

  public String getTempString() {
    return String.format("%.1f", temp);
  }

  public String getWindDirString() {
    return String.format("%.0f", windDir);
  }

  public String getWindSpeedString() {
    return String.format("%.2f", windSpeed);
  }

  public String getCloudCoverString() {
    return String.format("%.3f", cloudCover);
  }

  //<editor-fold desc="Setters and Getters">
  public String getWeatherDate() {
    return weatherDate;
  }
  public void setWeatherDate(String weatherDate) {
    this.weatherDate = weatherDate;
  }

  public Double getTemp() {
		return temp;
	}
	public void setTemp(Double temp) {
		this.temp = temp;
	}

	public Double getWindDir() {
		return windDir;
	}
	public void setWindDir(Double windDir) {
		this.windDir = windDir;
	}

	public Double getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(Double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public Double getCloudCover() {
		return cloudCover;
	}
	public void setCloudCover(Double cloudCover) {
		this.cloudCover = cloudCover;
	}

	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
  //</editor-fold>
}

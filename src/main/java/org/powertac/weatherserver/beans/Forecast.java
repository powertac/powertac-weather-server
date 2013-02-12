package org.powertac.weatherserver.beans;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Forecast extends Weather {
  private int id = 0;
  private String origin;

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }

  public String getOrigin() {
    return origin;
  }
  public void setOrigin(String origin) {
    this.origin = origin;
  }

  // For backwards compatibility
  @Override
  public String getWeatherDate() {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      Calendar c = Calendar.getInstance();
      c.setTime(sdf.parse(origin));
      c.add(Calendar.HOUR, 1 + id);
      return sdf.format(c.getTime());
    } catch (Exception ignored) {
      return "";
    }
  }
}

package org.powertac.weatherserver.beans;

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
}

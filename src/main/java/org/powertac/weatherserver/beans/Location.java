package org.powertac.weatherserver.beans;

import org.powertac.weatherserver.database.Database;
import org.powertac.weatherserver.Properties;

import java.util.ArrayList;
import java.util.List;


public class Location
{
  private String locationName = "";
  private String minDate = "";
  private String maxDate = "";

  private static Properties properties = new Properties();
  private static List<String> allowedLocations = null;
  private static List<Location> availableLocations = null;

  public Location ()
  {
  }

  public Location (String locationName)
  {
    if (getAllowedLocations().contains(locationName)) {
      this.locationName = locationName;
    }
    else {
      if (getAllowedLocations().size() > 0) {
        this.locationName = Location.getAllowedLocations().get(0);
      }
      else {
        this.locationName = "";
      }
    }
  }

  public static List<Location> getAvailableLocations ()
  {
    if (availableLocations == null) {
      Database db = new Database();
      try {
        availableLocations = db.getLocationList();
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      finally {
        db.close();
      }
    }

    return availableLocations;
  }

  public static List<String> getAllowedLocations ()
  {
    if (allowedLocations == null || allowedLocations.isEmpty()) {
      allowedLocations = new ArrayList<String>();

      String locationsString = properties.getProperty("publicLocations");
      for (String s: locationsString.split(",")) {
        allowedLocations.add(s.trim());
      }
    }

    return allowedLocations;
  }

  public String getLocationName ()
  {
    return locationName;
  }
  public void setLocationName (String locationName)
  {
    this.locationName = locationName;
  }

  public String getMinDate ()
  {
    return minDate;
  }
  public void setMinDate (String minDate)
  {
    this.minDate = minDate;
  }

  public String getMaxDate ()
  {
    return maxDate;
  }
  public void setMaxDate (String maxDate)
  {
    this.maxDate = maxDate;
  }
}
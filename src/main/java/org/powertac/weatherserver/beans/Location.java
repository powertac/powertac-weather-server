/**
 * Created by IntelliJ IDEA.
 * User: govert
 * Date: 2/4/13
 * Time: 5:47 PM
 */

package org.powertac.weatherserver.beans;

public class Location
{
  String locationName;
  String minDate;
  String maxDate;

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
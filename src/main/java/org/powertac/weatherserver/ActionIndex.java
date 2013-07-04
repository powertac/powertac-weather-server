package org.powertac.weatherserver;

import org.powertac.weatherserver.beans.Location;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.ArrayList;
import java.util.List;


@ManagedBean
@RequestScoped
public class ActionIndex
{
  public ActionIndex()
  {
  }

  public List<String> getLocationList()
  {
    List<Location> locations = new ArrayList<Location>();
    try {
      locations = Location.getAvailableLocations();
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    List<String> result = new ArrayList<String>();

    if (locations == null || locations.size() == 0) {
      return result;
    }

    for (Location location: locations) {
      result.add(String.format("%s : %s - %s",
          location.getLocationName(),
          location.getMinDate(), location.getMaxDate()));
    }

    return result;
  }
}

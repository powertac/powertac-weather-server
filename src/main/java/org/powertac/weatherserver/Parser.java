package org.powertac.weatherserver;

import org.powertac.weatherserver.beans.Energy;
import org.powertac.weatherserver.beans.Forecast;
import org.powertac.weatherserver.beans.Weather;
import org.powertac.weatherserver.constants.Constants;
import org.powertac.weatherserver.database.Database;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@ManagedBean
@RequestScoped
public class Parser {
	
	public static String parseRestRequest (Map<?, ?> params)
  {
		if (params!=null) {
			boolean dateSpecified = false;
			boolean queryFailed = false;
			
			String[] responseTypeArray = (String[]) params.get(Constants.REQ_PARAM_TYPE);
			String[] weatherDateArray = (String[]) params.get(Constants.REQ_PARAM_WEATHER_DATE);
			String[] weatherIdArray = (String []) params.get(Constants.REQ_PARAM_WEATHER_ID);
			String[] weatherLocationArray = (String []) params.get(Constants.REQ_PARAM_WEATHER_LOCATION);
			
			String responseType = "all";
			String weatherDate = "default";
			String weatherId = "1";
			String weatherLocation = "default";

      // TODO Not implemented yet
			/*if(responseTypeArray != null){
				responseType = responseTypeArray[0];
			}*/
			
			if (weatherLocationArray != null) {
				weatherLocation = weatherLocationArray[0];
			}
			
			if (weatherDateArray != null) {
				dateSpecified = true;
				weatherDate = weatherDateArray[0];
			}
			
			//if (!dateSpecified && weatherIdArray != null) {
			//	weatherId = weatherIdArray[0];
			//}
			
			List<Weather> reports = null;
			List<Forecast> forecasts = null;
			List<Energy> energy = null;
			
			if (weatherDate != null && weatherLocation != null) {
        // TODO Remove below
        System.out.println("Weather request : " + weatherDate +" "+ weatherLocation);

				Database db = new Database();
				try {
					if (responseType.equalsIgnoreCase("all")) {
						reports = db.getWeatherList(weatherDate, weatherLocation);
						forecasts = db.getForecastList(weatherDate, weatherLocation);
						energy = db.getEnergyList(weatherDate, weatherLocation);
					}
          /*else if(responseType.equalsIgnoreCase("report")) {
						reports = db.getWeatherList(weatherDate, weatherLocation);						
					}
          else if(responseType.equalsIgnoreCase("forecast")) {
						forecasts = db.getForecastList(weatherDate, weatherLocation);
					}
          else if(responseType.equalsIgnoreCase("energy")) {
						energy = db.getEnergyList(weatherDate, weatherLocation);
					}*/
				} catch (SQLException e) {
					e.printStackTrace();
					queryFailed = true;
				}
			}
			
			if (!queryFailed && reports!=null && forecasts!=null) {
				String output = "";
				output += Constants.RESPONSE_ROOT;
				
				// Do Report Processing
				String allReports = "";				
				for (Weather w: reports) {
					String tmpRecord = Constants.RESPONSE_WEATHER_REPORT;
					tmpRecord = String.format(tmpRecord,
											   w.getWeatherId(),
											   w.getWeatherDate(),
											   w.getTemp(),
											   w.getWindSpeed(),
											   w.getWindDir(),											   
											   w.getCloudCover(),
											   w.getLocation());
					allReports += tmpRecord;
				}
				allReports = String.format(Constants.RESPONSE_REPORT_ROOT, allReports);
				
				// Do Forecast Processing
				String allForecasts = "";
				for (Forecast f: forecasts) {
					String tmpRecord = Constants.RESPONSE_WEATHER_FORECAST;
					tmpRecord = String.format(tmpRecord,
												f.getWeatherId(),
											    f.getWeatherDate(),
											    f.getTemp(),
											    f.getWindSpeed(),
											    f.getWindDir(),											   
											    f.getCloudCover(),
											    f.getLocation());
					
					allForecasts += tmpRecord;
				}
				allForecasts = String.format(Constants.RESPONSE_FORECAST_ROOT, allForecasts);
				
				// Do Energy processing
				String allEnergy = "";/*
				for(Energy e : energy){
					String tmpRecord = Constants.RESPONSE_ENERGY_REPORT;
					tmpRecord = String.format(tmpRecord,
												e.getId(),
												e.getDate(),
												e.getPrice(),
												e.getLocation());
					allEnergy += allEnergy;
				}*/
				allEnergy = String.format(Constants.RESPONSE_ENERGY_ROOT, "");
				
				return String.format(output,allReports + allForecasts + allEnergy);
			}
      else {
				return "Query Failure";
			}
		}
    else {
			return "Error null params";
		}
	}

  public static String parseRestOptions (Map<?, ?> params)
  {



    return "foo";
  }
}

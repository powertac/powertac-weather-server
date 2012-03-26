package org.powertac.weatherserver;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.powertac.weatherserver.beans.Weather;
import org.powertac.weatherserver.constants.Constants;
import org.powertac.weatherserver.database.Database;

@ManagedBean
public class Parser {
	
	public static String parseRestRequest(Map<?, ?> params){
		if(params!=null){
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
			
			
			if(responseTypeArray != null){
				responseType = responseTypeArray[0];
			}
			
			if(weatherLocationArray != null){
				weatherLocation = weatherLocationArray[0];
				
			}
			
			if(weatherDateArray != null){
				dateSpecified = true;
				weatherDate = weatherDateArray[0];
				System.out.println("weatherDate");
			}
			
			if(!dateSpecified && weatherIdArray != null){
				weatherId = weatherIdArray[0];
			}
			
			
			List<Weather> reports = null;
			if (weatherDate != null && weatherLocation != null){
				Database db = (Database) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("database");
				try {
					reports = db.getWeatherList(weatherDate, weatherLocation);
				} catch (SQLException e) {
					e.printStackTrace();
					queryFailed = true;
				}
			}
			
			if (!queryFailed && reports != null){
				String output = "";
				output += Constants.RESPONSE_ROOT;
				output = String.format(output, Constants.RESPONSE_REPORT_ROOT);
				String allrecords = "";				
				for(Weather w : reports){
					String tmpRecord = Constants.RESPONSE_WEATHER_REPORT;
					tmpRecord = String.format(tmpRecord,
											   w.getWeatherId(),
											   w.getWeatherDate(),
											   w.getTemp(),
											   w.getWindSpeed(),
											   w.getWindDir(),											   
											   w.getCloudCover(),
											   w.getLocation());
					allrecords += tmpRecord;
					
				}
				return String.format(output,allrecords);
				
			}else{
				return "Query Failure";
			}
			
		} else {
			
			return "Error";
		}
		
		
	}

}

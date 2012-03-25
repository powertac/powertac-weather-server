package org.powertac.weatherserver;

import java.util.Map;

import org.powertac.weatherserver.constants.Constants;

public class Parser {
	
	public static String parseRestRequest(Map<?, ?> params){
		if(params!=null){
			boolean dateSpecified =false;
			
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
			}
			
			if(!dateSpecified && weatherIdArray != null){
				weatherId = weatherIdArray[0];
			}
			
			
			
			
			String output = "";
			for(Object key : params.keySet()){
				String skey = (String) key;
				output += skey + ":" + params.get(key) + "\n";
			}
			return output;
		} else {
			
			return "Error";
		}
		
		
	}

}

package org.powertac.weatherserver;

import java.util.Map;

public class Parser {
	
	public static String parseRestRequest(Map<?, ?> params){
		if(params!=null){
			return "Test";
		} else {
			
			return "Error";
		}
		
		
	}

}

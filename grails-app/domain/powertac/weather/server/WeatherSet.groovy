/*
* Copyright 2011 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an
* "AS IS" BASIS,  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
* either express or implied. See the License for the specific language
* governing permissions and limitations under the License.
*/

package powertac.weather.server

/**
* A WeatherSet describes the connection string information to the backend 
* weather database. The backend database is expected to be in a SQL style.
*
* @author Erik Onarheim, Josh Edeen
*
* @version 1.0 - 03/May/2011
*/
class WeatherSet {
	int startId
	int numberDays
	boolean fetched
	String tempColumnName
	String windDirColumnName
	String windSpeedColumnName
	String cloudCoverColumnName
	String idColumnName
	
	String dbName
	String dbHost
	String dbPass
	String dbtableName
		
	String reportString = ""
		
    static constraints = {
    }
	
	
	WeatherSet() {
		
	}
	
	
	def update()  {
		
	}
	
		
	def genReports(key) {
		
	}
}

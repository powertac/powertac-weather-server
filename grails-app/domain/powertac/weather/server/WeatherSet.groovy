package powertac.weather.server


class WeatherSet {
	Date startDate
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
	
	//static transients = {"fetched"}
	
    static constraints = {
    }
	
	static hasMany = [reports:WeatherReport,games:GameModel]
	
	WeatherSet() {
		
	}
	
	/*
	 * Returns the applicable weatherReport for the active GameModel ID.
	 * Only works if there are actually weather reports generated
	 */
	
	def gen48Forecast(int gameId){
		if(fetched){
			return new WeatherReport()
		}else{
			genReports()
			gen48Forecast(gameId)
		}	
	}
	
	
	
	/*
	 * Updates the current day and time with all active game models.
	 * Deletes inactive models from the database.
	 */
	def update()  {
		
	}
	
		
	/*
	 * Generates the initial weather reports for the database.
	 * This should only need to be called once per weatherSet.
	 */
	def genReports(key) {
		if(!fetched){
			DatabaseSetup ds = new DatabaseSetup()
			def weatherDatabaseService
			ds.register("localhost", "3306", "myTestWeatherDB", "root", "MKld597F")
			ds.connect()
			
			List result = ds.executeQuery("SELECT ("+tempColumnName+","+windDirColumnName+","+windSpeedColumnName+","+cloudCoverColumnName+") FROM weatherData WHERE "+idColumnName+"=")
			
			result.each ({ item -> reportString += "${item}\n"})
			
			result.each ({ item -> reports += new WeatherReport(item.get("day"),item.get("temp"),0.0f)})
						
			// Retrieve info from database and create WeatherReports
			fetched = true;
		}
	}
}

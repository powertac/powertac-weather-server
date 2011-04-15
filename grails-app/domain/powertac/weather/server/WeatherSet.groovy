package powertac.weather.server


class WeatherSet {
	Date startDate
	int numberDays
	boolean fetched
	String tempColumnName
	String windDirColumnName
	String windSpeedColumnName
	String cloudCoverColumnName
	
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
	def genReports() {
		if(!fetched){
			DatabaseSetup ds = new DatabaseSetup()
			ds.register("localhost", "3306", "myTestWeatherDB", "root", "MKld597F")
			ds.connect()
			
			List result = ds.executeQuery("select * from weatherData")
			
			result.each ({ item -> reportString += "${item}"})
			
			result.each ({ item -> reports += new WeatherReport(item.get("day"),item.get("temp"),0.0f)})
				//	def temp = item.get("temp")
			//	def day = item.get("day")
			//	reports += new WeatherReport(day,temp,0.0)
				//reportString += "${item}-"
				
			
			//	 })
			
			// Retrieve info from database and create WeatherReports
			fetched = true;
		}
	}
}

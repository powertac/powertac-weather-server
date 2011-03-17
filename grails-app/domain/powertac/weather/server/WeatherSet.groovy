package powertac.weather.server


class WeatherSet {
	Date startDate
	int numberDays
	boolean fetched
	//WeatherReport set = []
	
    static constraints = {
    }
	
	static hasMany = [reports:WeatherReport,games:GameModel]
	
	/*
	 * returns the applicable weatherReport for the active GameModel ID
	 */
	
	def gen48Forecast(int gameId){
		return new WeatherReport()
	}
	
	
	/*
	 * Updates the current day and time with all active game models.
	 * Deletes inactive models from the database.
	 */
	def update()  {
		
	}
	
	/*
	 * Generates the initial weather reports for the database.
	 * This should only need to be called once.
	 */
	def genReports() {
		if(!fetched){
			// Retrieve info from database and create WeatherReports
		}
	}
}

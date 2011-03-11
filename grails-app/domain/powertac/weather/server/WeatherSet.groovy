package powertac.weather.server


class WeatherSet {
	Date startDate
	int numberDays
	int currentDay
	int currentTime
	boolean fetched
	WeatherReport set = []
	
    static constraints = {
    }	
}

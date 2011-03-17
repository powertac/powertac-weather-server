package powertac.weather.server

class WeatherReport {
	
	
	static constraints = {
	}
	enum WindDirection{
		NORTH,
		NORTHEAST,
		NORTHWEST,
		SOUTH,
		SOUTHEAST,
		SOUTHWEST,
		EAST,
		WEST
	}
		
	int time = 0
	float temp = 0
	float cloudCover = 0
	float windSpeed = 0
	WindDirection windDir = WindDirection.WEST
	
	def WeatherReport(int time, float temp, float cloudCover) {
		this.time = time
		this.temp = temp
		this.cloudCover = cloudCover
		
	}

    
}

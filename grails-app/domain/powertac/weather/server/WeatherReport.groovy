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
		
	int time
	float temp
	float cloudCover
	float windSpeed
	WindDirection windDir
    
}

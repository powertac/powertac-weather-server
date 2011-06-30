package powertac.weather.server

import groovy.sql.Sql
import java.util.Random

class WeatherDatabaseService {

    static transactional = false
	
	Sql sql
	String serverName
	String dbName
	String username
	String password
	
	String tempColumn
	String windDirColumn
	String windSpeedColumn
	String cloudCoverColumn
	
	
	String defaultQuery = "SELECT id_weather, temp, wind_spd, wind_dir, 0.0 FROM historical_weather_data_minneapolis limit 24;"
	
	
	boolean registered = false
	
	// Connects to a mysql database in the sky, the service is ready for queries
	def connect() {
		if(registered){
			
			sql = Sql.newInstance("jdbc:mysql://"+ serverName +"/"+dbName,
			username, password, "com.mysql.jdbc.Driver")
		}
	}
	
	// Register for the default database connection, used for development purposes
	def defaultRegister() {
		registered = true
		serverName = "db.itlabs.umn.edu:3313"
		dbName = "powertac"
		this.username = "powertac"
		this.password = "ce32985DRAjeL34dg"
		
	}
	
	// Generate the query from the current weather id and a number of days out to get the forecast rows, assuming 12 server requests
	def genWeatherQuery(int weatherId, int nDaysOut){
		int nRecords = 12*2 + nDaysOut*48 // Every 12 hours the database is pulled, 2 timeslots per hour and 48 timeslots per day
		String defaultWeatherQuery = 
		"SELECT id_weather, temp, wind_spd, wind_dir, 0.0 " +
		"FROM historical_weather_data_minneapolis " +
		"WHERE id_weather >= " + weatherId + 
		" AND id_weather <= " + (weatherId+nRecords)  +
		" LIMIT " + nRecords + ";"
		
		return defaultWeatherQuery
	}
	
	def genForecastMultipliers(int nDaysOut){
		int nRecords = 12*2 + nDaysOut*48 // Every 12 hours the database is pulled, 2 timeslots per hour and 48 timeslots per day
		List multList = (nRecords+2)..1
		Random rand = new Random()
		def max = 100
		def newList = []
		for(i in 0..(multList.size()-1)){
			// Generates more random numbers as the forecast moves farther into the future
			newList << ((1.0f - multList[i]/(nRecords+1.0f)) * (rand.nextInt(max+1)/max - 1/2.0f))
			//println newList[i]	
		}
		return newList		
	}
	
	// Register for any database connection, used for competition and custom datasets
	def register(String server, String port, String db, String username, String password){
		registered = true
		serverName = server+":"+port
		dbName = db
		this.username = username
		this.password = password
	}
	
	/*
	 * Execute selection queries
	 */
	def executeQuery(String q){
		return sql.rows(q)
	}
	
	/*
	 * Execute and update queries
	 */
	def executeNonQuery(String q){
		return false
	}
    def serviceMethod() {

    }
}

package powertac.weather.server

import groovy.sql.Sql;

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
	
	
	String defaultQuery = "SELECT id_date, id_hrmn, temp, wind_spd, wind_dir, 0.0 FROM historical_weather_data_minneapolis limit 24;"
	
	
	boolean registered = false
	
	def connect() {
		if(registered){
			
			sql = Sql.newInstance("jdbc:mysql://"+ serverName +"/"+dbName,
			username, password, "com.mysql.jdbc.Driver")
		}
	}
	
	def defaultRegister() {
		registered = true
		serverName = "db.itlabs.umn.edu:3313"
		dbName = "powertac"
		this.username = "powertac"
		this.password = "ce32985DRAjeL34dg"
		
	}
	
	def genWeatherQuery(int weatherId){
		String defaultWeatherQuery = "SELECT id_date, id_hrmn, temp, wind_spd, wind_dir, 0.0 FROM historical_weather_data_minneapolis limit 168;"
	}
	
	
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

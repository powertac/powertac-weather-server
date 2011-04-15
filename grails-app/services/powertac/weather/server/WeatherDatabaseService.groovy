package powertac.weather.server

import groovy.sql.Sql;

class WeatherDatabaseService {

    static transactional = true
	
	Sql sql
	String serverName
	String dbName
	String username
	String password
	boolean registered = false
	
	def connect() {
		if(registered){
			//Sql sql = Sql.newInstance("jdbc:mysql://localhost:3306/test","root", "MKld597F", "com.mysql.jdbc.Driver")
			sql = Sql.newInstance("jdbc:mysql://"+ serverName +"/"+dbName,
			username, password, "com.mysql.jdbc.Driver")
		}
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

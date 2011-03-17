package powertac.weather.server


import groovy.sql.Sql

public class DatabaseSetup {
	private Sql sql
	private String serverName
	private String dbName
	private String username
	private String password
	
	private boolean registered = false
	
	public void connect(){
		if(registered){
			sql = Sql.newInstance("jdbc:mysql://"+ serverName +"/"+dbName,
			username, password, "com.mysql.jdbc.Driver")
		}
	}
	
	public void register(String server, String port, String db, String username, String password){
		registered = true
		serverName = server+":"+port
		dbName = db
		this.username = username
		this.password = password	
	}	
	
	public List executeQuery(String q){
		return sql.rows(q)
	}
	
	public boolean executeNonQuery(String q){
		return false
	}
}
package org.powertac.weatherserver.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.powertac.weatherserver.beans.Energy;
import org.powertac.weatherserver.beans.Forecast;
import org.powertac.weatherserver.beans.Weather;

@ManagedBean(eager=true)
@ApplicationScoped
public class Database {
	
	// Requested parameters
	private String date = "";
	private String id = "";
	
	// Data limiting factors
	private String location = "";
	private String datemin = "";
	private String datemax = "";
	
	// Connection Related
	private String dbUrl = "";
	private String database = "";
	private String port = "";
	private String username = "";
	private String password = "";
	private String dbms = "";
	
	private Connection conn = null;
	Properties connectionProps = new Properties();
	Properties prop = new Properties();
	
	
	public Database(){
		try {
			
			prop.load(Database.class.getClassLoader().getResourceAsStream("/weatherserver.properties"));
			System.out.println(prop);
			// Database Connection related properties
			this.setDatabase(prop.getProperty("db.database"));
			this.setDbms(prop.getProperty("db.dbms"));
			this.setPort(prop.getProperty("db.port"));
			this.setDbUrl(prop.getProperty("db.dbUrl"));
			this.setUsername(prop.getProperty("db.username"));
			this.setPassword(prop.getProperty("db.password"));
			
			// Configuration Related properties
			this.setDatemin(prop.getProperty("datemin"));
			this.setDatemax(prop.getProperty("datemax"));
			this.setLocation(prop.getProperty("location"));
			System.out.println("Successfully instantiated Database bean!");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void checkDb(){
		if(conn == null){
			if (this.dbms.equals("mysql")) {
		        try {
		        	connectionProps.setProperty("user", this.username);
		        	connectionProps.setProperty("password", this.password);
					conn = DriverManager.getConnection(
							"jdbc:" + this.dbms +
							"://" + this.dbUrl +
					        ":" + this.port + 
					        "/", connectionProps);
				} catch (SQLException e) {
				}
			}
		}
	}
	
	public List<Weather> getWeatherList(){
		checkDb();
		return null;
		
	}
	
	public List<Forecast> getForecastList(){
		checkDb();
		return null;
	}
	
	
	public List<Energy> getEnergyList(){
		checkDb();
		return null;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbms() {
		return dbms;
	}

	public void setDbms(String dbms) {
		this.dbms = dbms;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDatemin() {
		return datemin;
	}

	public void setDatemin(String datemin) {
		this.datemin = datemin;
	}

	public String getDatemax() {
		return datemax;
	}

	public void setDatemax(String datemax) {
		this.datemax = datemax;
	}

}

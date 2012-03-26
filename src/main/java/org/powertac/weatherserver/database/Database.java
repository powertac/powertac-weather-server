package org.powertac.weatherserver.database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.powertac.weatherserver.DateString;
import org.powertac.weatherserver.beans.Energy;
import org.powertac.weatherserver.beans.Forecast;
import org.powertac.weatherserver.beans.Weather;
import org.powertac.weatherserver.constants.Constants;

@ManagedBean(eager = true)
@ApplicationScoped
public class Database {

	// Requested parameters
	private String date = "";
	private String id = "";

	// Data limiting factors
	private String location = "";
	private String datemin = "";
	private String datemax = "";

	private String reportTable = "";
	private String forecastTable = "";
	private String energyTable = "";

	// Connection Related
	private String dbUrl = "";
	private String database = "";
	private String port = "";
	private String username = "";
	private String password = "";
	private String dbms = "";

	// Database Configurations
	private Connection conn = null;
	private PreparedStatement weatherStatement = null;
	private PreparedStatement forecastStatement = null;
	private PreparedStatement energyStatement = null;
	Properties connectionProps = new Properties();
	Properties prop = new Properties();

	public Database() {
		try {

			prop.load(Database.class.getClassLoader().getResourceAsStream(
					"/weatherserver.properties"));
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
			this.setReportTable(prop.getProperty("reportTable"));
			this.setForecastTable(prop.getProperty("forecastTable"));
			this.setEnergyTable(prop.getProperty("energyTable"));
			System.out.println("Successfully instantiated Database bean!");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void checkDb() {
		if (conn == null) {
			System.out.println("Connection is null");
			if (this.dbms.equalsIgnoreCase("mysql")) {
				System.out.println("Using mysql as dbms ...");
				try {
					connectionProps.setProperty("user", this.username);
					connectionProps.setProperty("password", this.password);
					Class.forName ("com.mysql.jdbc.Driver").newInstance();
					conn = DriverManager.getConnection("jdbc:" + this.dbms
							+ "://" + this.dbUrl +  "/" + this.database,
							connectionProps);
					System.out.println("Connected Successfully");
				} catch (Exception e) {
					System.out.println("Connection Error");
					e.printStackTrace();
				}
			}
		}else{
			System.out.println("Connection is good");
		}
	}

	public List<Weather> getWeatherList(String startDate, String location) throws SQLException {
		checkDb();
		if (weatherStatement == null) {
			weatherStatement = conn.prepareStatement(String.format(Constants.DB_SELECT_REPORT,this.reportTable));
		}

		
		weatherStatement.setDate(1, (new DateString(startDate)).getSqlDate());
		weatherStatement.setString(2, location);

		ResultSet result = weatherStatement.executeQuery();

		List<Weather> list = new ArrayList<Weather>();
		
		while(result.next()){
			Weather w = new Weather();
			w.setWeatherId(result.getString("weatherId"));
			w.setWeatherDate(result.getString("weatherDate"));
			w.setTemp(result.getString("temp"));
			w.setWindDir(result.getString("windDir"));
			w.setWindSpeed(result.getString("windSpeed"));
			w.setCloudCover(result.getString("cloudCover"));
			w.setLocation(result.getString("location"));
			list.add(w);
		}
		
		return list;

	}

	public List<Forecast> getForecastList() {
		checkDb();
		return null;
	}

	public List<Energy> getEnergyList() {
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

	public String getReportTable() {
		return reportTable;
	}

	public void setReportTable(String reportTable) {
		this.reportTable = reportTable;
	}

	public String getForecastTable() {
		return forecastTable;
	}

	public void setForecastTable(String forecastTable) {
		this.forecastTable = forecastTable;
	}

	public String getEnergyTable() {
		return energyTable;
	}

	public void setEnergyTable(String energyTable) {
		this.energyTable = energyTable;
	}

}

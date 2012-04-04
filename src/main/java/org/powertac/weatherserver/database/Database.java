package org.powertac.weatherserver.database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
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
	private String datemin = "";
	private String datemax = "";
	
	private List<String> locations;

	private String implementation = "";
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
		locations = new ArrayList<String>();
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
			this.setDatemin(prop.getProperty("publicDateMin"));
			this.setDatemax(prop.getProperty("publicDateMax"));
			this.setLocations(prop.getProperty("publicLocations"));
			
			this.setImplementation(prop.getProperty("implementation"));
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
		
		// Datestring is not the correct length
		if (startDate.length() != 10){
			return new ArrayList<Weather>();
		}
		
		if (weatherStatement == null) {
			weatherStatement = conn.prepareStatement(String.format(Constants.DB_SELECT_REPORT,this.reportTable));
		}

		// Check to make sure they are requesting public data
		if (this.getLocations().contains(location) && this.validDate(startDate)){
			System.out.println("Datestring: " + startDate);
			System.out.println("Sql Date: " + new DateString(startDate).getLocaleString());
			//weatherStatement.setDate(1, (new DateString(startDate)).getSqlDate());
			weatherStatement.setString(1, new DateString(startDate).getLocaleString());
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
		}else{
			return new ArrayList<Weather>();
		}
		
		

	}

	public List<Forecast> getForecastList(String weatherDate, String weatherLocation) throws SQLException {
		checkDb();
		
		// Procedural implementation does a trend based random walk
		if(implementation.equals("procedural")){
			double tau0 = Double.parseDouble(prop.getProperty("tau0"));
			double sigma = Double.parseDouble(prop.getProperty("sigma"));
			
			DateString beforeDate = new DateString(weatherDate);
			DateString afterDate = new DateString(weatherDate);
			
			beforeDate.shiftBackDay();
			afterDate.shiftAheadDay();
			
			System.out.println("Procedural Implementation");
			System.out.println("Dates: " + beforeDate.getLocaleString() + " " + weatherDate + " " + afterDate.getLocaleString());
			
			
			List<Weather> rollingBefore = this.getWeatherList(beforeDate.getRestString(), weatherLocation);
			List<Weather> rollingMiddle = this.getWeatherList(weatherDate, weatherLocation);
			List<Weather> rollingAfter  = this.getWeatherList(afterDate.getRestString(), weatherLocation);
			
			System.out.println("Before Date: " + beforeDate.getRestString());
			System.out.println("Middle Date: " + weatherDate);
			System.out.println("After Date: " + afterDate.getRestString());

			System.out.println("Rolling Before Size: " + rollingBefore.size());
			System.out.println("Rolling Middle Size: " + rollingMiddle.size());
			System.out.println("Rolling After Size: " + rollingAfter.size());
			
			Weather[] avgWeather = new Weather[rollingBefore.size()];
			for (int i = 0; i < avgWeather.length ; i++){
				List<Weather> tmpList = new ArrayList<Weather>();
				tmpList.add(rollingBefore.get(i));
				tmpList.add(rollingMiddle.get(i));
				tmpList.add(rollingAfter.get(i));
				avgWeather[i] = avgReports(tmpList);
				System.out.println("1: " + rollingBefore.get(i).getWindDir() + " 2: " + rollingMiddle.get(i).getWindDir() + " 3: " + rollingAfter.get(i).getWindDir());
				System.out.println("Avg: " + avgWeather[i].getWindDir());
				
				
			}
			System.out.println("Avg Weather Length: " + avgWeather.length);
			
			List<Forecast> result = new ArrayList<Forecast>();
			
			Forecast tmpForecast;
			DateString forecastDate = new DateString(weatherDate);
			
			for(int i=0; i<2; i++){
				for(Weather w : avgWeather){
					tmpForecast = new Forecast();
					tmpForecast.setLocation(w.getLocation());
					
					tmpForecast.setWeatherDate(forecastDate.getLocaleString());
					
					Double newTemp = Double.parseDouble(w.getTemp()) * tau0;
					tmpForecast.setTemp(newTemp.toString());
					
					Double newWindDir = (Double.parseDouble((w.getWindDir().equalsIgnoreCase("***")?"0":w.getWindDir())) * tau0)%360; 
					tmpForecast.setWindDir(newWindDir.toString());
					
					Double newWindSpeed = Double.parseDouble((w.getWindSpeed().equalsIgnoreCase("***")?"0":w.getWindSpeed())) * tau0;
					tmpForecast.setWindSpeed(newWindSpeed.toString());
					
					String newCloudCover = clampCloudCover( getCloudCoverValue(w.getCloudCover()) * tau0 );
					tmpForecast.setCloudCover(newCloudCover);
					
					result.add(tmpForecast);
					//System.out.println("tau0: " + tau0 );
					//System.out.println("Avg Temp: " + w.getTemp());
					
					tau0 = tau0 * ((1.0/sigma - sigma)*Math.random() + sigma);
					forecastDate.shiftAheadHour();
				}
			}
			
			return result;
		}else{
			// TODO: Implement database query
		}
		
		return null;
	}

	public List<Energy> getEnergyList(String weatherDate, String weatherLocation) {
		checkDb();
		return null;
	}
	
	private Weather avgReports(List<Weather> weathers){
		Weather tmpWeather = new Weather();
		Double newTemp = 0.0;
		Double newWindDir = 0.0;
		Double newWindSpeed = 0.0;
		Double newCloudCover = 0.0;
		
		for (Weather w : weathers){		
			tmpWeather.setLocation(w.getLocation());
			
			newTemp += Double.parseDouble(w.getTemp());						
			newWindDir += Double.parseDouble((w.getWindDir().equalsIgnoreCase("***")?"0":w.getWindDir()));						
			newWindSpeed += Double.parseDouble((w.getWindSpeed().equalsIgnoreCase("***")?"0":w.getWindSpeed()));
			newCloudCover += getCloudCoverValue(w.getCloudCover());
		}
		
		tmpWeather.setTemp(String.valueOf(newTemp/weathers.size()));
		tmpWeather.setWindDir(String.valueOf(newWindDir/weathers.size()));
		tmpWeather.setWindSpeed(String.valueOf(newWindSpeed/weathers.size()));
		tmpWeather.setCloudCover(clampCloudCover(newCloudCover/weathers.size()));
		
		return tmpWeather;
		
	}
	
	private double getCloudCoverValue(String s){
		if(s.equalsIgnoreCase("clr")){
			return 0.0;
		}else if (s.equalsIgnoreCase("sct")){
			return 3.0/8.0;
		}else if (s.equalsIgnoreCase("bkn")){
			return 6.0/8.0;
		}else if (s.equalsIgnoreCase("ovc")){
			return 1.0;
		}else if (s.equalsIgnoreCase("obs")){
			return 1.0;
		}else{
			return 1.0;
		}
		
	}
	
	private String clampCloudCover(Double value){
		if(value < 1.0/8.0){
			return "CLR";
		}else if (value < 4.0/8.0){
			return "SCT";
		}else if (value < 7.0/8.0){
			return "BKN";
		}else{
			return "OVC";
		}
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

	public List<String> getLocations() {
		return locations;
	}

	public void setLocations(String locations) {
		for(String s :locations.split(",")){
			this.locations.add(s.trim());
		}
		
	}
	
	public boolean validDate(String date){
		Date sqlDatemin = new DateString(this.getDatemin()).getSqlDate();
		Date sqlDatemax = new DateString(this.getDatemax()).getSqlDate();
		Date testDate = new DateString(date).getSqlDate();
		
		return (testDate.before(sqlDatemax) && testDate.after(sqlDatemin));
		
	}

	public String getImplementation() {
		return implementation;
	}

	public void setImplementation(String implementation) {
		this.implementation = implementation;
	}

}

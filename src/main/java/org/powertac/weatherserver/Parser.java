package org.powertac.weatherserver;

import org.powertac.weatherserver.beans.Energy;
import org.powertac.weatherserver.beans.Forecast;
import org.powertac.weatherserver.beans.Location;
import org.powertac.weatherserver.beans.Weather;
import org.powertac.weatherserver.constants.Constants;
import org.powertac.weatherserver.database.Database;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean
@RequestScoped
public class Parser
{
  private static Properties properties = new Properties();

	public static String parseRestRequest (Map<?, ?> params)
  {
    if (params == null) {
      return "Error null params";
    }

    String responseType = "all";
    String[] responseTypeArray =
        (String[]) params.get(Constants.Rest.REQ_PARAM_TYPE);
    if (responseTypeArray != null) {
      responseType = responseTypeArray[0];
    }

    WeatherDate weatherDate = null;
    String[] weatherDateArray =
        (String[]) params.get(Constants.Rest.REQ_PARAM_WEATHER_DATE);
    if (weatherDateArray != null) {
      weatherDate = new WeatherDate(weatherDateArray[0], true);
    }

    Location weatherLocation = null;
    String[] weatherLocationArray =
        (String []) params.get(Constants.Rest.REQ_PARAM_WEATHER_LOCATION);
    if (weatherLocationArray != null) {
      weatherLocation = new Location(weatherLocationArray[0]);
    }

    if (Boolean.parseBoolean(properties.getProperty("showRequestInLog")) &&
        weatherLocation != null && weatherLocation.getLocationName() != null &&
        weatherDate != null && weatherDate.getMediumString() != null) {
      System.out.println(String.format("\nRequest for : %s %s\n",
          weatherLocation.getLocationName(), weatherDate.getMediumString()));
    }

    if (properties.getProperty("useFlatFiles", "").equals("true")) {
      String flatFile = parseFile(weatherDate, weatherLocation);
      if (flatFile != null) {
        return flatFile;
      }
    }

    return queryDB (responseType, weatherDate, weatherLocation);
	}

  private static String parseFile (WeatherDate weatherDate, Location location)
  {
    StringBuilder sb = null;
    BufferedReader br = null;
    try {
      String sCurrentLine;
      sb = new StringBuilder();
      br = new BufferedReader(
          new FileReader(properties.getProperty("flatFileLocation")
              + weatherDate.getSmallString() + "."
              + location.getLocationName() + ".xml"));

      while ((sCurrentLine = br.readLine()) != null) {
        sb.append(sCurrentLine);
      }
    } catch (IOException ignored) {
      sb = null;
    } finally {
      try {
        if (br != null) {
          br.close();
        }
      } catch (IOException ignored) {}
    }

    if (sb != null) {
      return sb.toString();
    }
    return null;
  }

  private static String queryDB (String responseType, WeatherDate weatherDate,
                                 Location weatherLocation)
  {
    List<Weather> reports = new ArrayList<Weather>();
    List<Forecast> forecasts = new ArrayList<Forecast>();
    List<Energy> energys = new ArrayList<Energy>();
    Database db = new Database();
    try {
      if (responseType.equalsIgnoreCase("all")) {
        reports = db.getWeatherList(weatherDate, weatherLocation);
        forecasts = db.getForecastList(weatherDate, weatherLocation);
        energys = db.getEnergyList(weatherDate, weatherLocation);
        if (reports.isEmpty() || forecasts.isEmpty()) {
          return "Query Failure";
        }
      }
      else if (responseType.equalsIgnoreCase("report")) {
        reports = db.getWeatherList(weatherDate, weatherLocation);
        if (reports.isEmpty()) {
          return "Query Failure";
        }
      }
      else if (responseType.equalsIgnoreCase("forecast")) {
        forecasts = db.getForecastList(weatherDate, weatherLocation);
        if (forecasts.isEmpty()) {
          return "Query Failure";
        }
      }
      else if (responseType.equalsIgnoreCase("energy")) {
        energys = db.getEnergyList(weatherDate, weatherLocation);
        if (energys.isEmpty()) {
          return "Query Failure";
        }
      }
    }
    catch (Exception e) {
      return "Query Failure";
    }
    finally {
      db.close();
    }

    return createXML(reports, forecasts, energys);
  }

  private static String createXML (List<Weather> reports,
                                   List<Forecast> forecasts,
                                   List<Energy> energys)
  {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

      // Root element
      Document doc = docBuilder.newDocument();
      doc.setXmlStandalone(true);
      Element rootElement = doc.createElement("data");
      doc.appendChild(rootElement);

      // weatherReports elements
      Element weatherReports = doc.createElement("weatherReports");
      rootElement.appendChild(weatherReports);

      for (Weather weather: reports) {
        Element weatherReport = doc.createElement("weatherReport");
        weatherReport.setAttribute("date", weather.getWeatherDate());
        weatherReport.setAttribute("temp", weather.getTempString());
        weatherReport.setAttribute("windspeed", weather.getWindSpeedString());
        weatherReport.setAttribute("winddir", weather.getWindDirString());
        weatherReport.setAttribute("cloudcover", weather.getCloudCoverString());
        weatherReport.setAttribute("location", weather.getLocation());
        weatherReports.appendChild(weatherReport);
      }

      // weatherForecasts elements
      Element weatherForecasts = doc.createElement("weatherForecasts");
      rootElement.appendChild(weatherForecasts);

      for (Forecast forecast: forecasts) {
        Element weatherForecast = doc.createElement("weatherForecast");
        weatherForecast.setAttribute("id", String.valueOf(forecast.getId()));
        weatherForecast.setAttribute("origin", forecast.getOrigin());
        weatherForecast.setAttribute("temp", forecast.getTempString());
        weatherForecast.setAttribute("windspeed", forecast.getWindSpeedString());
        weatherForecast.setAttribute("winddir", forecast.getWindDirString());
        weatherForecast.setAttribute("cloudcover", forecast.getCloudCoverString());
        weatherForecast.setAttribute("location", forecast.getLocation());
        weatherForecast.setAttribute("date", forecast.getWeatherDate());
        weatherForecasts.appendChild(weatherForecast);
      }

      // energyReports elements
      Element energyReports = doc.createElement("energyReports");
      rootElement.appendChild(energyReports);

      for (Energy energy: energys) {
        Element energyReport = doc.createElement("energyReport");
        energyReport.setAttribute("date", energy.getDate());
        energyReport.setAttribute("price", energy.getPrice());
        energyReport.setAttribute("location", energy.getLocation());
        energyReports.appendChild(energyReport);
      }

      TransformerFactory transFactory = TransformerFactory.newInstance();
      Transformer transformer = transFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      StringWriter buffer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(buffer));

      return buffer.toString();
    }
    catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    }
    catch (TransformerException tfe) {
      tfe.printStackTrace();
    }

    return "Query Failure";
  }

  public static String parseShowLocationsRequest (Map<?, ?> params)
  {
    List<Location> locations;
    try {
      locations = Location.getAvailableLocations();
    }
    catch (Exception ignored) {
      return "Query Failure";
    }

    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

      Document doc = docBuilder.newDocument();
      doc.setXmlStandalone(true);
      Element rootElement = doc.createElement("locations");
      doc.appendChild(rootElement);

      for (Location location: locations) {
        Element element = doc.createElement("location");
        rootElement.appendChild(element);
        element.setAttribute("name", location.getLocationName());
        element.setAttribute("minDate", location.getMinDate());
        element.setAttribute("maxDate", location.getMaxDate());
      }

      TransformerFactory transFactory = TransformerFactory.newInstance();
      Transformer transformer = transFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      StringWriter buffer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(buffer));

      return buffer.toString();
    }
    catch (Exception e) {
      e.printStackTrace();
      return "Query Failure";
    }
  }

  public static String parseOptionsRequest (Map<?, ?> params)
  {
    return "Not implemented yet";
  }
}

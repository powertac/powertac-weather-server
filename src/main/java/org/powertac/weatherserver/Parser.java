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
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean
@RequestScoped
public class Parser {

	public static String parseRestRequest (Map<?, ?> params)
  {
    if (params == null) {
      return "Error null params";
    }

    //String[] responseTypeArray = (String[]) params.get(Constants.REQ_PARAM_TYPE);
    String[] weatherDateArray = (String[]) params.get(Constants.REQ_PARAM_WEATHER_DATE);
    String[] weatherLocationArray = (String []) params.get(Constants.REQ_PARAM_WEATHER_LOCATION);

    String responseType = "all";
    String weatherDate = "default";
    String weatherLocation = "default";

    //if(responseTypeArray != null){
    //	responseType = responseTypeArray[0];
    //}
    if (weatherLocationArray != null) {
      weatherLocation = weatherLocationArray[0];
    }
    if (weatherDateArray != null) {
      weatherDate = weatherDateArray[0];
    }

    List<Weather> reports = new ArrayList<Weather>();
    List<Forecast> forecasts = new ArrayList<Forecast>();
    List<Energy> energys = new ArrayList<Energy>();

    if (weatherDate != null && weatherLocation != null) {
      Database db = new Database();

      // Make sure we have a valid date
      if (!db.validDate(weatherDate)) {
        weatherDate = db.makeValidDate(weatherDate);
      }

      try {
        if (responseType.equalsIgnoreCase("all")) {
          reports = db.getWeatherList(weatherDate, weatherLocation);
          forecasts = db.getForecastList(weatherDate, weatherLocation);
          //energy = db.getEnergyList(weatherDate, weatherLocation);
        }
        /*else if(responseType.equalsIgnoreCase("report")) {
          reports = db.getWeatherList(weatherDate, weatherLocation);
        }
        else if(responseType.equalsIgnoreCase("forecast")) {
          forecasts = db.getForecastList(weatherDate, weatherLocation);
        }
        else if(responseType.equalsIgnoreCase("energy")) {
          energy = db.getEnergyList(weatherDate, weatherLocation);
        }*/
      }
      catch (Exception e) {
        e.printStackTrace();
        return "Query Failure";
      }
    }

    if (reports.isEmpty() || forecasts.isEmpty()) {
      return "Query Failure";
    }
    return createXML(reports, forecasts, energys);
	}

  private static String createXML (List<Weather> reports,
                                 List<Forecast> forecasts, List<Energy> energys)
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
    Database db = new Database();
    try {
      locations = db.getLocationList();
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

package org.powertac.weatherserver.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.powertac.weatherserver.forecast.WeatherForecast;
import org.powertac.weatherserver.report.WeatherReport;

import java.util.List;

@AllArgsConstructor
@JacksonXmlRootElement(localName = "data")
public class CombinedWeatherData {

    @Getter
    @JacksonXmlElementWrapper(localName = "weatherReports")
    @JacksonXmlProperty(localName = "weatherReport")
    private List<WeatherReport> weatherReports;

    @Getter
    @JacksonXmlElementWrapper(localName = "weatherForecasts")
    @JacksonXmlProperty(localName = "weatherForecast")
    private List<WeatherForecast> weatherForecasts;

}

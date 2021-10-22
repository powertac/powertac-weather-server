package org.powertac.weatherserver.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "forecasts")
@IdClass(WeatherForecastId.class)
public class WeatherForecast {

    @Id
    @Getter
    @Setter
    @Column(name = "weatherdate")
    @JacksonXmlProperty(localName = "date", isAttribute = true)
    private String weatherDate;

    @Id
    @Getter
    @Setter
    @JacksonXmlProperty(isAttribute = true)
    private String origin;

    @Id
    @Getter
    @Setter
    @JacksonXmlProperty(localName = "location", isAttribute = true)
    private String location;

    @Getter
    @Setter
    @JacksonXmlProperty(localName = "temp", isAttribute = true)
    private Double temp;

    @Getter
    @Setter
    @Column(name = "winddir")
    @JacksonXmlProperty(localName = "winddir", isAttribute = true)
    private Double windDir;

    @Getter
    @Setter
    @Column(name = "windspeed")
    @JacksonXmlProperty(localName = "windspeed", isAttribute = true)
    private Double windSpeed;

    @Getter
    @Setter
    @Column(name = "cloudcover")
    @JacksonXmlProperty(localName = "cloudcover", isAttribute = true)
    private Double cloudCover;

    @Getter
    @Setter
    @Transient
    @JsonInclude
    @JacksonXmlProperty(isAttribute = true)
    public Integer id;

}

package org.powertac.weatherserver.report;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "reports")
@IdClass(WeatherReportId.class)
public class WeatherReport {

    @Id
    @Getter
    @Setter
    @Column(name = "weatherdate", columnDefinition = "datetime NOT NULL")
    @JacksonXmlProperty(localName = "date", isAttribute = true)
    private String weatherDate;

    @Id
    @Getter
    @Setter
    @JacksonXmlProperty(isAttribute = true)
    @Column(length = 128)
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

}

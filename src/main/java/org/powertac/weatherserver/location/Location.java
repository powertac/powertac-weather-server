package org.powertac.weatherserver.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
public class Location {

    @Getter
    private String name;

    @Getter
    @Setter
    private Instant minReportTime;

    @Getter
    @Setter
    private Instant maxReportTime;

    @Getter
    @Setter
    private Instant minForecastTime;

    @Getter
    @Setter
    private Instant maxForecastTime;

    public Location(String name) {
        this.name = name;
    }

    public boolean includesDate(Instant date) {
        return date.isAfter(minReportTime.minus(1, ChronoUnit.SECONDS))
            && date.isBefore(maxReportTime.plus(1, ChronoUnit.SECONDS))
            && date.isAfter(minForecastTime.minus(1, ChronoUnit.SECONDS))
            && date.isBefore(maxForecastTime.plus(1, ChronoUnit.SECONDS));
    }

}

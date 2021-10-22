package org.powertac.weatherserver.report;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class WeatherReportId implements Serializable {

    private String weatherDate;
    private String location;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherReportId that = (WeatherReportId) o;
        if (!weatherDate.equals(that.weatherDate)) return false;
        return location.equals(that.location);
    }

    @Override
    public int hashCode() {
        int result = weatherDate.hashCode();
        result = 31 * result + location.hashCode();
        return result;
    }

}

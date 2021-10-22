package org.powertac.weatherserver.forecast;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class WeatherForecastId implements Serializable {

    private String weatherDate;
    private String location;
    private String origin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeatherForecastId that = (WeatherForecastId) o;

        if (!weatherDate.equals(that.weatherDate)) return false;
        if (!location.equals(that.location)) return false;
        return origin.equals(that.origin);
    }

    @Override
    public int hashCode() {
        int result = weatherDate.hashCode();
        result = 31 * result + location.hashCode();
        result = 31 * result + origin.hashCode();
        return result;
    }

}

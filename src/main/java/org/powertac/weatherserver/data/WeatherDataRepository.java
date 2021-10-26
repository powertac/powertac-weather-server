package org.powertac.weatherserver.data;

import java.time.Instant;

public interface WeatherDataRepository {

    CombinedWeatherData findByLocationAndStartTime(String location, Instant start);

}

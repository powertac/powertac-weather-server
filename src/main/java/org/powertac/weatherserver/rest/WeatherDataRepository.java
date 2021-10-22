package org.powertac.weatherserver.rest;

import java.time.Instant;

public interface WeatherDataRepository {

    CombinedWeatherData findByLocationAndStartTime(String location, Instant start);

}

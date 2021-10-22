package org.powertac.weatherserver.forecast;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WeatherForecastRepository extends CrudRepository<WeatherForecast, WeatherForecastId> {

    List<WeatherForecast> findAllByLocationAndOriginBetweenOrderByOriginAscWeatherDateAsc(String location, String origin, String origin2);

}

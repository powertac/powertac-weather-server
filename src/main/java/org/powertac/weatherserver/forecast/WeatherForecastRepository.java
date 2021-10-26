package org.powertac.weatherserver.forecast;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public interface WeatherForecastRepository extends CrudRepository<WeatherForecast, WeatherForecastId> {

    List<WeatherForecast> findAllByLocationAndOriginBetweenOrderByOriginAscWeatherDateAsc(String location, String origin, String origin2);

    @Query(value = "SELECT location FROM forecasts", nativeQuery = true)
    Set<String> findLocations();

    @Query(value = "SELECT MIN(origin) FROM forecasts WHERE location = :#{#location}", nativeQuery = true)
    Instant findMinDateByLocation(@Param("location") String location);

    @Query(value = "SELECT MAX(origin) FROM forecasts WHERE location = :#{#location}", nativeQuery = true)
    Instant findMaxDateByLocation(@Param("location") String location);

}

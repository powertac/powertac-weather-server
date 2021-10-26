package org.powertac.weatherserver.report;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public interface WeatherReportRepository extends CrudRepository<WeatherReport, WeatherReportId> {

    List<WeatherReport> findAllByLocationAndWeatherDateBetweenOrderByWeatherDateAsc(String location, String weatherDate, String weatherDate2);

    @Query(value = "SELECT location FROM reports", nativeQuery = true)
    Set<String> findLocations();

    @Query(value = "SELECT MIN(weatherDate) FROM reports WHERE location = :#{#location}", nativeQuery = true)
    Instant findMinDateByLocation(@Param("location") String location);

    @Query(value = "SELECT MAX(weatherDate) FROM reports WHERE location = :#{#location}", nativeQuery = true)
    Instant findMaxDateByLocation(@Param("location") String location);

}

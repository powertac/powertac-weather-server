package org.powertac.weatherserver.report;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WeatherReportRepository extends CrudRepository<WeatherReport, WeatherReportId> {

    List<WeatherReport> findAllByLocationAndWeatherDateBetweenOrderByWeatherDateAsc(String location, String weatherDate, String weatherDate2);

}

package org.powertac.weatherserver.data;

import org.powertac.weatherserver.forecast.WeatherForecast;
import org.powertac.weatherserver.forecast.WeatherForecastRepository;
import org.powertac.weatherserver.report.WeatherReport;
import org.powertac.weatherserver.report.WeatherReportRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class DatabaseWeatherDataRepository implements WeatherDataRepository {

    private final WeatherReportRepository reportRepository;
    private final WeatherForecastRepository forecastRepository;

    public DatabaseWeatherDataRepository(WeatherReportRepository reportRepository, WeatherForecastRepository forecastRepository) {
        this.reportRepository = reportRepository;
        this.forecastRepository = forecastRepository;
    }

    @Override
    public CombinedWeatherData findByLocationAndStartTime(String location, Instant start) {
        Instant end = start.plus(23, ChronoUnit.HOURS);
        return new CombinedWeatherData(
            findReports(location, start, end),
            findForecasts(location, start, end));
    }

    private List<WeatherReport> findReports(String location, Instant start, Instant end) {
        return reportRepository.findAllByLocationAndWeatherDateBetweenOrderByWeatherDateAsc(
            location,
            start.toString(),
            end.toString());
    }

    private List<WeatherForecast> findForecasts(String location, Instant start, Instant end) {
        List<WeatherForecast> forecasts = forecastRepository.findAllByLocationAndOriginBetweenOrderByOriginAscWeatherDateAsc(
            location,
            start.toString(),
            end.toString());
        return setIds(forecasts);
    }

    private List<WeatherForecast> setIds(List<WeatherForecast> forecasts) {
        int offset = 1;
        for (WeatherForecast forecast : forecasts) {
            forecast.setId(offset);
            offset = offset == 24 ? 1 : offset + 1;
        }
        return forecasts;
    }

}

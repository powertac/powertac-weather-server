package org.powertac.weatherserver.rest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class WeatherDataController {

    private final WeatherDataRepository weather;

    public WeatherDataController(WeatherDataRepository weather) {
        this.weather = weather;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<CombinedWeatherData> findAll(@RequestParam("weatherDate") String startTime, @RequestParam("weatherLocation") String location) {
        Instant start = parseStart(startTime);
        return ResponseEntity.ok(weather.findByLocationAndStartTime(location, start));
    }

    private Instant parseStart(String startTime) {
        String year = startTime.substring(0, 4);
        String month = startTime.substring(4, 6);
        String day = startTime.substring(6,8);
        String hour = startTime.substring(8, 10);
        return Instant.parse(String.format("%s-%s-%sT%s:00:00Z", year, month, day, hour));
    }

}

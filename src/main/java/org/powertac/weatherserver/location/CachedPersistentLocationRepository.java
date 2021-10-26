package org.powertac.weatherserver.location;

import org.powertac.weatherserver.forecast.WeatherForecastRepository;
import org.powertac.weatherserver.report.WeatherReportRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class CachedPersistentLocationRepository implements LocationRepository {

    private final static int CACHE_TTL_HOURS = 24;

    private final WeatherReportRepository reports;
    private final WeatherForecastRepository forecasts;

    private final Map<String, Location> cachedLocations;
    private Instant cacheTime;

    public CachedPersistentLocationRepository(WeatherReportRepository reports, WeatherForecastRepository forecasts) {
        this.reports = reports;
        this.forecasts = forecasts;
        this.cachedLocations = new HashMap<>();
    }

    @Override
    public Set<Location> findAllLocations() {
        heatCache();
        return new HashSet<>(cachedLocations.values());
    }

    @Override
    public boolean contains(String name, Instant date) {
        heatCache();
        return cachedLocations.containsKey(name)
            && cachedLocations.get(name).includesDate(date);
    }

    private void heatCache() {
        if (null == cacheTime || cacheExpired()) {
            invalidateCache();
            updateLocations();
            cacheTime = Instant.now();
        }
    }

    private boolean cacheExpired() {
        return null != cacheTime
            && cacheTime.plus(CACHE_TTL_HOURS, ChronoUnit.HOURS).isBefore(Instant.now());
    }

    private void invalidateCache() {
        cachedLocations.clear();
    }

    private void updateLocations() {
        for (String name : getLocationNames()) {
            Location location = new Location(name);
            hydrateReportTimeRange(location);
            hydrateForecastTimeRange(location);
            cachedLocations.put(name, location);
        }
    }

    private Set<String> getLocationNames() {
        Set<String> locationNames = new HashSet<>();
        locationNames.addAll(reports.findLocations());
        locationNames.addAll(forecasts.findLocations());
        return locationNames;
    }

    private void hydrateReportTimeRange(Location location) {
        location.setMinReportTime(reports.findMinDateByLocation(location.getName()));
        location.setMaxReportTime(reports.findMaxDateByLocation(location.getName()));
    }

    private void hydrateForecastTimeRange(Location location) {
        location.setMinForecastTime(forecasts.findMinDateByLocation(location.getName()));
        location.setMaxForecastTime(forecasts.findMaxDateByLocation(location.getName()));
    }

}

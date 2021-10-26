package org.powertac.weatherserver.location;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/locations")
public class LocationRestController {

    private final LocationRepository locationRepository;

    public LocationRestController(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @GetMapping("/")
    public ResponseEntity<Collection<Location>> getLocations() {
        return ResponseEntity.ok(locationRepository.findAllLocations());
    }

}

package org.powertac.weatherserver.location;

import java.time.Instant;
import java.util.Set;

public interface LocationRepository {

    Set<Location> findAllLocations();
    boolean contains(String location, Instant date);

}

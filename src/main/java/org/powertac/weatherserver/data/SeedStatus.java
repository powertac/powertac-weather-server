package org.powertac.weatherserver.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SeedStatus {

    @Id
    @Getter
    @Column(length = 128)
    private String md5;

    @Getter
    private Instant completedAt;

}

package org.powertac.weatherserver.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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

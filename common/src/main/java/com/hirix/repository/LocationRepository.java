package com.hirix.repository;

import com.hirix.domain.Location;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Cacheable("locations")
    List<Location> findAllByVisibleIs(boolean isVisible);
}

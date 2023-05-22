package com.hirix.repository;

import com.hirix.domain.Position;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Long> {

    @Cacheable("positions")
    List<Position> findAllByVisibleIs(boolean isVisible);
}

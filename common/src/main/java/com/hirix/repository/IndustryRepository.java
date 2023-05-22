package com.hirix.repository;

import com.hirix.domain.Industry;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndustryRepository extends JpaRepository<Industry, Long> {

    @Cacheable("industries")
    List<Industry> findAllByVisibleIs(boolean isVisible);
}

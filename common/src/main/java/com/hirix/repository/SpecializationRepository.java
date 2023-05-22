package com.hirix.repository;

import com.hirix.domain.Specialization;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

    @Cacheable("specializations")
    List<Specialization> findAllByVisibleIs(boolean isVisible);
}

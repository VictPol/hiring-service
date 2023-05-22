package com.hirix.repository;

import com.hirix.domain.Profession;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessionRepository extends JpaRepository<Profession, Long> {

    @Cacheable("professions")
    List<Profession> findAllByVisibleIs(boolean isVisible);
}

package com.hirix.repository;

import com.hirix.domain.Rank;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RankRepository extends JpaRepository<Rank, Long> {

    @Cacheable("ranks")
    List<Rank> findAllByVisibleIs(boolean isVisible);
}

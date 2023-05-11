package com.hirix.repository;

import com.hirix.domain.Profession;
import com.hirix.domain.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepository extends JpaRepository<Rank, Long> {

}

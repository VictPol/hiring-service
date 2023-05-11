package com.hirix.repository;

import com.hirix.domain.Position;
import com.hirix.domain.Profession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {

}

package com.hirix.repository;

import com.hirix.domain.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequirementRepository extends JpaRepository<Requirement, Long> {

    List<Requirement> findRequirementsByEquipmentsLike(String query);
}

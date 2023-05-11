package com.hirix.repository;

import com.hirix.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findCompaniesByEquipmentsLike(String query);

}

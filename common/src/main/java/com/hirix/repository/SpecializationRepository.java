package com.hirix.repository;

import com.hirix.domain.Profession;
import com.hirix.domain.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

}

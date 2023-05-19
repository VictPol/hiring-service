package com.hirix.repository;

import com.hirix.domain.Industry;
import com.hirix.domain.Position;
import com.hirix.domain.Profession;
import com.hirix.domain.Rank;
import com.hirix.domain.Skill;
import com.hirix.domain.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findSkillsByEquipmentsLike(String query);

    @Query(value = "select s from Skill s where" +
            " s.id in (select l.skillId from LinkSkillsLocations l where l.locationId = :offeredLocationId) and" +
            " s.experience >= :experience and" +
            " s.active = :isActive and" +
            " s.recommendations >= :recommendations and" +
            " s.salaryMax >=:salary and" +
            " s.salaryMin <= :salary and" +
            " s.termMax >= :term and" +
            " s.termMin <= :term and" +
            " s.industry = :industry and" +
            " s.profession = :profession and" +
            " s.specialization = :specialization and" +
            " s.rank = :rank and" +
            " s.position = :position"
    )
    List<Skill> findSkillsByRequirementId(Integer experience, boolean isActive, Integer recommendations,
                                          Integer salary, Integer term, Industry industry,
                                          Profession profession, Specialization specialization, Rank rank,
                                          Position position, Long offeredLocationId);

    @Query(value =
            "select s from Skill s where s.salaryMin in (" +
            " select min (s.salaryMin) from Skill s where" +
            " s.id in (select l.skillId from LinkSkillsLocations l where l.locationId = :offeredLocationId) and" +
            " s.experience >= :experience and" +
            " s.active = :isActive and" +
            " s.recommendations >= :recommendations and" +
            " s.salaryMax >=:salary and" +
            " s.salaryMin <= :salary and" +
            " s.termMax >= :term and" +
            " s.termMin <= :term and" +
            " s.industry.id = :industryId and" +
            " s.profession.id = :professionId and" +
            " s.specialization.id = :specializationId and" +
            " s.rank.id = :rankId and" +
            " s.position.id = :positionId) and" +
                    " s.id in (select l.skillId from LinkSkillsLocations l where l.locationId = :offeredLocationId) and" +
                    " s.experience >= :experience and" +
                    " s.active = :isActive and" +
                    " s.recommendations >= :recommendations and" +
                    " s.salaryMax >=:salary and" +
                    " s.salaryMin <= :salary and" +
                    " s.termMax >= :term and" +
                    " s.termMin <= :term and" +
                    " s.industry.id = :industryId and" +
                    " s.profession.id = :professionId and" +
                    " s.specialization.id = :specializationId and" +
                    " s.rank.id = :rankId and" +
                    " s.position.id = :positionId"
    )
    List<Skill> findSkillsByRequirementIdWithMinSalary(Integer experience, boolean isActive, Integer recommendations,
                                          Integer salary, Integer term, Long industryId,
                                          Long professionId, Long specializationId, Long rankId,
                                          Long positionId, Long offeredLocationId);


    @Query(value = "select s from Skill s where" +
            " s.id in (select l.skillId from LinkSkillsLocations l where l.locationId = :offeredLocationId) and" +
            " s.experience >= :experience and" +
            " s.active = :isActive and" +
            " s.recommendations >= :recommendations and" +
            " s.salaryMax >=:salary and" +
            " s.salaryMin <= :salary and" +
            " s.termMax >= :term and" +
            " s.termMin <= :term and" +
            " s.industry = :industry and" +
            " s.profession = :profession and" +
            " s.specialization = :specialization and" +
            " s.rank = :rank and" +
            " s.position = :position and" +
            " lower(s.equipments) like :equipment"
    )
    List<Skill> findSkillsByRequirementIdAndEquipmentLike(Integer experience, boolean isActive, Integer recommendations,
                                       Integer salary, Integer term, Industry industry, Profession profession,
                                       Specialization specialization, Rank rank, Position position, Long offeredLocationId,
                                       String equipment);

    @Query(value = "select s from Skill s where" +
            " s.id in (select l.skillId from LinkSkillsLocations l where l.locationId = :offeredLocationId) and" +
            " s.experience >= :experience and" +
            " s.active = :isActive and" +
            " s.recommendations >= :recommendations and" +
            " s.salaryMax >=:salary and" +
            " s.salaryMin <= :salary and" +
            " s.termMax >= :term and" +
            " s.termMin <= :term and" +
            " s.industry.id = :industryId and" +
            " s.profession.id = :professionId and" +
            " s.specialization.id = :specializationId and" +
            " s.rank.id = :rankId and" +
            " s.position.id = :positionId and" +
            " s.employee.location.id = :employeeLocationId"
    )
    List<Skill> findSkillsByRequirementIdAndEmployeeLocationId(Integer experience, boolean isActive, Integer recommendations,
                                          Integer salary, Integer term, Long industryId,
                                          Long professionId, Long specializationId, Long rankId,
                                          Long positionId, Long offeredLocationId, Long employeeLocationId);

}

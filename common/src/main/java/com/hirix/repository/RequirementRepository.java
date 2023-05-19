package com.hirix.repository;

import com.hirix.domain.Industry;
import com.hirix.domain.Location;
import com.hirix.domain.Position;
import com.hirix.domain.Profession;
import com.hirix.domain.Rank;
import com.hirix.domain.Requirement;
import com.hirix.domain.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface RequirementRepository extends JpaRepository<Requirement, Long> {

    List<Requirement> findRequirementsByEquipmentsLike(String query);

    @Query(value = "select r from Requirement r where" +
            " r.locationOffered.id in (select l.locationId from  LinkSkillsLocations l where l.skillId = :skillId) and" +
            " r.experience <= :experience and" +
            " r.active = :isActive and" +
            " r.recommendations <= :recommendations and" +
            " r.salary >= :salaryMin and" +
            " r.salary <= :salaryMax and" +
            " r.term >= :termMin and" +
            " r.term <= :termMax and" +
            " r.industry = :industry and" +
            " r.profession = :profession and" +
            " r.specialization = :specialization and" +
            " r.rank = :rank and" +
            " r.position = :position"
    )
    List<Requirement> findRequirementsBySkillId(Integer experience, boolean isActive,
                    Integer recommendations, Integer salaryMin, Integer salaryMax, Integer termMin,
                    Integer termMax, Industry industry, Profession profession, Specialization specialization,
                    Rank rank, Position position, Long skillId);

    @Query(value =
            "select r from Requirement r where r.salary in (" +
            " select max (r.salary) from Requirement r where" +
            " r.locationOffered.id in (select l.locationId from  LinkSkillsLocations l where l.skillId = :skillId) and" +
            " r.experience <= :experience and" +
            " r.active = :isActive and" +
            " r.recommendations <= :recommendations and" +
            " r.salary >= :salaryMin and" +
            " r.salary <= :salaryMax and" +
            " r.term >= :termMin and" +
            " r.term <= :termMax and" +
            " r.industry.id = :industryId and" +
            " r.profession.id = :professionId and" +
            " r.specialization.id = :specializationId and" +
            " r.rank.id = :rankId and" +
            " r.position.id = :positionId) and" +
                    " r.locationOffered.id in (select l.locationId from  LinkSkillsLocations l where l.skillId = :skillId) and" +
                    " r.experience <= :experience and" +
                    " r.active = :isActive and" +
                    " r.recommendations <= :recommendations and" +
                    " r.salary >= :salaryMin and" +
                    " r.salary <= :salaryMax and" +
                    " r.term >= :termMin and" +
                    " r.term <= :termMax and" +
                    " r.industry.id = :industryId and" +
                    " r.profession.id = :professionId and" +
                    " r.specialization.id = :specializationId and" +
                    " r.rank.id = :rankId and" +
                    " r.position.id = :positionId"
    )
    List<Requirement> findRequirementsBySkillIdAndSalaryMaxQuery(Integer experience, boolean isActive,
                                                Integer recommendations, Integer salaryMin, Integer salaryMax, Integer termMin,
                                                Integer termMax, Long industryId, Long professionId, Long specializationId,
                                                Long rankId, Long positionId, Long skillId);

    @Query(value = "select r from Requirement r where" +
            " r.locationOffered.id in (select l.locationId from  LinkSkillsLocations l where l.skillId = :skillId) and" +
            " r.experience <= :experience and" +
            " r.active = :isActive and" +
            " r.recommendations <= :recommendations and" +
            " r.salary >= :salaryMin and" +
            " r.salary <= :salaryMax and" +
            " r.term >= :termMin and" +
            " r.term <= :termMax and" +
            " r.industry = :industry and" +
            " r.profession = :profession and" +
            " r.specialization = :specialization and" +
            " r.rank = :rank and" +
            " r.position = :position and" +
            " lower(r.equipments) like :equipment"
    )
    List<Requirement> findRequirementsBySkillIdAndEquipmentLike(Integer experience, boolean isActive,
                                                Integer recommendations, Integer salaryMin, Integer salaryMax, Integer termMin,
                                                Integer termMax, Industry industry, Profession profession, Specialization specialization,
                                                Rank rank, Position position, Long skillId, String equipment);

    @Query(value = "select r from Requirement r where" +
            " r.locationOffered.id in (select l.locationId from  LinkSkillsLocations l where l.skillId = :skillId) and" +
            " r.experience <= :experience and" +
            " r.active = :isActive and" +
            " r.recommendations <= :recommendations and" +
            " r.salary >= :salaryMin and" +
            " r.salary <= :salaryMax and" +
            " r.term >= :termMin and" +
            " r.term <= :termMax and" +
            " r.industry = :industry and" +
            " r.profession = :profession and" +
            " r.specialization = :specialization and" +
            " r.rank = :rank and" +
            " r.position = :position and" +
            " r.company.location.id = :companyLocationId"
    )
    List<Requirement> findRequirementsBySkillIdAndCompanyLocationId(Integer experience, boolean isActive,
                                        Integer recommendations, Integer salaryMin, Integer salaryMax, Integer termMin,
                                        Integer termMax, Industry industry, Profession profession, Specialization specialization,
                                        Rank rank, Position position, Long skillId, Long companyLocationId);

}

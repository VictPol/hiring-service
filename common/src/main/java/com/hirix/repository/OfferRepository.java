package com.hirix.repository;

import com.hirix.domain.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    @Query(value = "select o from Offer o where" +
            " o.skill.id = :skillId"
    )
    List<Offer> findOffersBySkillIdQuery(Long skillId);


    @Query(value =
            "select o from Offer o inner join Requirement r on o.requirement.id = r.id" +
            " where o.skill.id = :skillId and" +
            " o.requirement.salary = (select max(r.salary) from r where r.id in" +
            " (select o.requirement.id from o where o.skill.id = :skillId))"
    )
    List<Offer> findOffersBySkillIdQueryAndSalaryMax(Long skillId);


    @Query(value = "select o from Offer o where" +
            " o.skill.employee.id = :employeeId"
    )
    List<Offer> findOffersByEmployeeIdQuery(Long employeeId);

    @Query(value = "select o from Offer o where" +
            " o.requirement.id = :requirementId"
    )
    List<Offer> findOffersByRequirementIdQuery(Long requirementId);

    @Query(value = "select o from Offer o where" +
            " o.requirement.company.id = :companyId"
    )
    List<Offer> findOffersByCompanyIdQuery(Long companyId);

}

package com.hirix.controller.convertors.skill;

import com.hirix.controller.convertors.employee.EmployeeBaseConvertor;
import com.hirix.controller.requests.create.EmployeeCreateRequest;
import com.hirix.controller.requests.create.SkillCreateRequest;
import com.hirix.domain.Employee;
import com.hirix.domain.Industry;
import com.hirix.domain.Location;
import com.hirix.domain.Position;
import com.hirix.domain.Profession;
import com.hirix.domain.Rank;
import com.hirix.domain.Skill;
import com.hirix.domain.Specialization;
import com.hirix.domain.User;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.EmployeeRepository;
import com.hirix.repository.IndustryRepository;
import com.hirix.repository.LocationRepository;
import com.hirix.repository.PositionRepository;
import com.hirix.repository.ProfessionRepository;
import com.hirix.repository.RankRepository;
import com.hirix.repository.SpecializationRepository;
import com.hirix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SkillCreateConvertor extends SkillBaseConvertor<SkillCreateRequest, Skill> {
    private final EmployeeRepository employeeRepository;
    private final IndustryRepository industryRepository;
    private final ProfessionRepository professionRepository;
    private final SpecializationRepository specializationRepository;
    private final RankRepository rankRepository;
    private final PositionRepository positionRepository;

    @Override
    public Skill convert(SkillCreateRequest request) {
        Skill skill = new Skill();
        Long employeeId;
        try {
            employeeId = request.getEmployeeId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about employee id in request body to create skill. Must be Long type. " +
                            e.getCause());
        }
        if (employeeId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create skill. " +
                    "Employee id must be more than 0L");
        }
        Optional<Employee> optionalEmployee;
        try {
            optionalEmployee = employeeRepository.findById(employeeId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get employee by id from DB. " + e.getCause());
        }
        Employee employee = optionalEmployee.orElseThrow(() -> new NoSuchElementException("No employee with such id"));
        skill.setEmployee(employee);

        Long industryId;
        try {
            industryId = request.getIndustryId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about industry id in request body to create skill. Must be Long type. " +
                            e.getCause());
        }
        if (industryId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create skill. " +
                    "Industry id must be more than 0L");
        }
        Optional<Industry> optionalIndustry;
        try {
            optionalIndustry = industryRepository.findById(industryId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get industry by id from DB. " + e.getCause());
        }
        Industry industry = optionalIndustry.orElseThrow(() -> new NoSuchElementException("No industry with such id"));
        skill.setIndustry(industry);

        Long professionId;
        try {
            professionId = request.getProfessionId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about profession id in request body to create skill. Must be Long type. " +
                            e.getCause());
        }
        if (professionId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create skill. " +
                    "Profession id must be more than 0L");
        }
        Optional<Profession> optionalProfession;
        try {
            optionalProfession = professionRepository.findById(professionId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get profession by id from DB. " + e.getCause());
        }
        Profession profession = optionalProfession.orElseThrow(() -> new NoSuchElementException("No profession with such id"));
        skill.setProfession(profession);

        Long specializationId;
        try {
            specializationId = request.getSpecializationId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about specialization id in request body to create skill. Must be Long type. " +
                            e.getCause());
        }
        if (specializationId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create skill. " +
                    "Specialization id must be more than 0L");
        }
        Optional<Specialization> optionalSpecialization;
        try {
            optionalSpecialization = specializationRepository.findById(specializationId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get specialization by id from DB. " + e.getCause());
        }
        Specialization specialization = optionalSpecialization.orElseThrow(() -> new NoSuchElementException("No specialization with such id"));
        skill.setSpecialization(specialization);

        Long rankId;
        try {
            rankId = request.getRankId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about rank id in request body to create skill. Must be Long type. " +
                            e.getCause());
        }
        if (rankId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create skill. " +
                    "Rank id must be more than 0L");
        }
        Optional<Rank> optionalRank;
        try {
            optionalRank = rankRepository.findById(rankId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get rank by id from DB. " + e.getCause());
        }
        Rank rank = optionalRank.orElseThrow(() -> new NoSuchElementException("No rank with such id"));
        skill.setRank(rank);

        Long positionId;
        try {
            positionId = request.getPositionId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about position id in request body to create skill. Must be Long type. " +
                            e.getCause());
        }
        if (positionId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create skill. " +
                    "Position id must be more than 0L");
        }
        Optional<Position> optionalPosition;
        try {
            optionalPosition = positionRepository.findById(positionId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get position by id from DB. " + e.getCause());
        }
        Position position = optionalPosition.orElseThrow(() -> new NoSuchElementException("No position with such id"));
        skill.setPosition(position);

        skill.setCreated(Timestamp.valueOf(LocalDateTime.now()));

        return doConvert(request, skill);
    }
}

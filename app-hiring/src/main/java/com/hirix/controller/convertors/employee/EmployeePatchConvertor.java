package com.hirix.controller.convertors.employee;

import com.hirix.controller.requests.patch.EmployeePatchRequest;
import com.hirix.domain.Employee;
import com.hirix.domain.Location;
import com.hirix.domain.enums.Education;
import com.hirix.domain.enums.Gender;
import com.hirix.domain.enums.Health;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.EmployeeRepository;
import com.hirix.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmployeePatchConvertor implements Converter<EmployeePatchRequest, Employee> {

    private final EmployeeRepository employeeRepository;
    private final LocationRepository locationRepository;

    @Override
    public Employee convert(EmployeePatchRequest request) {
        Long id;
        try {
            id = request.getId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about employee id in request body to update employee. Must be Long type. " +
                            e.getCause());
        }
        if (id < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor employee id in request body to get employee. " +
                    "Id must be more than 0L");
        }
        Optional<Employee> optionalEmployee;
        try {
            optionalEmployee = employeeRepository.findById(id);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get employee by id from DB, " + e.getCause());
        }
        Employee employee = optionalEmployee.orElseThrow(() -> new NoSuchElementException("No employee with such id"));
        try {
            if (request.getFullName() != null) {
                employee.setFullName(request.getFullName());
            }
            if (request.getBirthday() != null) {
                employee.setBirthday(Timestamp.valueOf(request.getBirthday()));
            }
            if (request.getEducation() != null) {
                employee.setEducation(Education.valueOf(request.getEducation()));
            }
            if (request.getHealth() != null) {
                employee.setHealth(Health.valueOf(request.getHealth()));
            }
            if (request.getGender() != null) {
                employee.setGender(Gender.valueOf(request.getGender()));
            }
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to patch update employee. " +
                    e.getCause());
        }

        Long locationId;
        try {
            locationId = request.getLocationId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about employee location id in request body to update employee. Must be Long type. " +
                            e.getCause());
        }
        if (locationId != null && locationId > 0L && !locationId.equals(employee.getLocation().getId())) {
            setLocationToEmployee(employee, locationId);
        }
        if (locationId != null && locationId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about employee location id in request body to patch update employee. Must be more than 1L.");
        }

        employee.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        return employee;
    }

    private void setLocationToEmployee(Employee employee, Long locationId) {
        setLocationToEmployee(employee, locationId, locationRepository);
    }

    static void setLocationToEmployee(Employee employee, Long locationId, LocationRepository locationRepository) {
        Optional<Location> optionalLocation;
        try {
            optionalLocation = locationRepository.findById(locationId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get employee location by id from DB, " + e.getCause());
        }
        Location location = optionalLocation.orElseThrow(() -> new NoSuchElementException("No employee location with such id"));
        employee.setLocation(location);
    }
}


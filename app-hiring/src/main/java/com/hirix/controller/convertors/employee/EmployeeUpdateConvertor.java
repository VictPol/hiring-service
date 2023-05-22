package com.hirix.controller.convertors.employee;


import com.hirix.controller.requests.update.EmployeeUpdateRequest;
import com.hirix.domain.Employee;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.EmployeeRepository;
import com.hirix.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmployeeUpdateConvertor extends EmployeeBaseConvertor<EmployeeUpdateRequest, Employee> {

    private final EmployeeRepository employeeRepository;
    private final LocationRepository locationRepository;

    @Override
    public Employee convert(EmployeeUpdateRequest request) {
        Long id;
        try {
            id = request.getId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about employee id in request body to update employee. Must be Long type. " + e.getCause());
        }
        if (id < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update employee. " +
                    "Id must be more than 0L");
        }
        Optional<Employee> optionalEmployee;
        try {
            optionalEmployee = employeeRepository.findById(id);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get employee by id from DB, " + e.getCause());
        }
        Employee employee = optionalEmployee.orElseThrow(() -> new NoSuchElementException("No employee with such id"));

        Long locationId;
        try {
            locationId = request.getLocationId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about location id in request body to update employee. Must be Long type" +
                            e.getCause());
        }
        if (locationId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update employee. " +
                    "LocationId must be more than 0L");
        }
        if (!locationId.equals(employee.getLocation().getId())) {
            setLocationToEmployee(employee, locationId);
        }
        return doConvert(request, employee);
    }

    private void setLocationToEmployee(Employee employee, Long locationId) {
        EmployeePatchConvertor.setLocationToEmployee(employee, locationId, locationRepository);
    }
}

package com.hirix.controller.convertors.employee;

import com.hirix.controller.requests.create.EmployeeCreateRequest;
import com.hirix.domain.Employee;
import com.hirix.domain.enums.Education;
import com.hirix.domain.enums.Gender;
import com.hirix.domain.enums.Health;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class EmployeeBaseConvertor<S, T> implements Converter<S, T> {

    public Employee doConvert(EmployeeCreateRequest request, Employee employeeForSave) {
        try {
            employeeForSave.setFullName(request.getFullName());
            employeeForSave.setBirthday(Timestamp.valueOf(request.getBirthday()));
            employeeForSave.setEducation(Education.valueOf(request.getEducation()));
            employeeForSave.setHealth(Health.valueOf(request.getHealth()));
            employeeForSave.setGender(Gender.valueOf(request.getGender()));
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create employee" +
                    e.getCause());
        }
        if (employeeForSave.getFullName() == null ||
                employeeForSave.getBirthday() == null ||
                employeeForSave.getEducation() == null ||
                employeeForSave.getHealth() == null ||
                employeeForSave.getGender() == null) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create employee");
        }

        employeeForSave.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        return employeeForSave;
    }
}

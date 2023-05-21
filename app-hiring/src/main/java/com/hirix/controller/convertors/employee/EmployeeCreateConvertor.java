package com.hirix.controller.convertors.employee;

import com.hirix.controller.requests.create.EmployeeCreateRequest;
import com.hirix.domain.Employee;
import com.hirix.domain.Location;
import com.hirix.domain.User;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.LocationRepository;
import com.hirix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class EmployeeCreateConvertor extends EmployeeBaseConvertor<EmployeeCreateRequest, Employee> {
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    @Override
    public Employee convert(EmployeeCreateRequest request) {
        Employee employee = new Employee();
        Long userId;
        try {
            userId = request.getUserId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about user id in request body to create employee. Must be Long type" +
                            e.getCause());
        }
        if (userId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create employee. " +
                    "User id must be more than 0L");
        }
        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findById(userId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get user by id from DB, " + e.getCause());
        }
        User user = optionalUser.orElseThrow(() -> new NoSuchElementException("No user with such id"));
        if (user.getEmployee() == null) {
            employee.setUser(user);
        } else {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Can not create employee, because employee with such user id exists yet");
        }
        Long locationId;
        try {
            locationId = request.getLocationId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about location id in request body to create employee. Must be Long type" +
                            e.getCause());
        }
        if (locationId < 1) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create employee. " +
                    "LocationId must be more than 0L");
        }
        Optional<Location> optionalLocation;
        try {
            optionalLocation = locationRepository.findById(locationId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get location by id from DB, " + e.getCause());
        }
        Location location = optionalLocation.orElseThrow(() -> new NoSuchElementException("No location with such id"));
        employee.setLocation(location);
        employee.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        return doConvert(request, employee);
    }
}

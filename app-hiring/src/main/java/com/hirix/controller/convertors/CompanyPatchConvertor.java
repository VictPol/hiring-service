package com.hirix.controller.convertors;

import com.hirix.controller.requests.patch.CompanyPatchRequest;
import com.hirix.domain.Company;
import com.hirix.domain.Location;
import com.hirix.domain.User;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.CompanyRepository;
import com.hirix.repository.LocationRepository;
import com.hirix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CompanyPatchConvertor implements Converter<CompanyPatchRequest, Company> {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    @Override
    public Company convert(CompanyPatchRequest request) {
        Long id;
        try {
            id = request.getId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                ("Poor information about company id in request body to update company. Must be Long type",
                    e.getCause());
        }
        Optional<com.hirix.domain.Company> optionalCompany;
        try {
            optionalCompany = companyRepository.findById(id);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get company by id from DB, ", e.getCause());
        }
        com.hirix.domain.Company company = optionalCompany.orElseThrow(() -> new NoSuchElementException("No company with such id"));
        try {
            if (request.getFullTitle() != null) {
                company.setFullTitle(request.getFullTitle());
            }
            if (request.getShortTitle() != null) {
                company.setShortTitle(request.getShortTitle());
            }
            if (request.getRegNumber() != null) {
                company.setRegNumber(request.getRegNumber());
            }
            if (request.getOrgType() != null) {
                company.setOrgType(request.getOrgType());
            }
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update company",
                    e.getCause());
        }
        company.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        Long userId;
        try {
            userId = request.getUserId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                ("Poor information about user id in request body to create company. Must be Long type",
                    e.getCause());
        }
        if (userId != null && userId > 0) {
            Optional<User> optionalUser;
            try {
                optionalUser = userRepository.findById(userId);
            } catch (Exception e) {
                throw new EntityNotFoundException("Can not get user by id from DB, ", e.getCause());
            }
            User user = optionalUser.orElseThrow(() -> new NoSuchElementException("No user with such id"));
            if (user.getCompany() == null) {
                company.getUser().setCompany(null);
                company.setUser(user);
            } else {
                throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Can not create company, because company with such user id exists yet");
            }
        }
        Long locationId;
        try {
            locationId = request.getLocationId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                ("Poor information about location id in request body to update company. Must be Long type",
                    e.getCause());
        }
        if (locationId != null && locationId > 0) {
            Optional<Location> optionalLocation;
            try {
                optionalLocation = locationRepository.findById(locationId);
            } catch (Exception exception) {
                throw new EntityNotFoundException("Can not get location by id from DB, ", exception.getCause());
            }
            Location location = optionalLocation.orElseThrow(() -> new NoSuchElementException("No location with such id"));
            company.setLocation(location);
        }
        return company;
    }
}

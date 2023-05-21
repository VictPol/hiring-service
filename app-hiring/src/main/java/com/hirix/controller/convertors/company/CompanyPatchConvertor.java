package com.hirix.controller.convertors.company;

import com.hirix.controller.requests.patch.CompanyPatchRequest;
import com.hirix.domain.Company;
import com.hirix.domain.Location;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.CompanyRepository;
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
public class CompanyPatchConvertor implements Converter<CompanyPatchRequest, Company> {
    private final CompanyRepository companyRepository;
    private final LocationRepository locationRepository;

    @Override
    public Company convert(CompanyPatchRequest request) {
        Long id;
        try {
            id = request.getId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                ("Poor information about company id in request body to update company. Must be Long type. " +
                    e.getCause());
        }
        if (id < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor company id in request body to get company. " +
                    "Id must be more than 0L");
        }
        Optional<Company> optionalCompany;
        try {
            optionalCompany = companyRepository.findById(id);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get company by id from DB, " + e.getCause());
        }
        Company company = optionalCompany.orElseThrow(() -> new NoSuchElementException("No company with such id"));
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
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update company. " +
                e.getCause());
        }
        company.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        Long userId;
        try {
            userId = request.getUserId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                ("Poor information about user id in request body to patch update company. " +
                     "Must be null or Long type" + e.getCause());
        }
        if (userId != null && !userId.equals(company.getUser().getId())) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                ("Can not patch update company, because user id does not correspond to this company");
        }
        Long locationId;
        try {
            locationId = request.getLocationId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                ("Poor information about location id in request body to patch update company. Must be Long type. " +
                    e.getCause());
        }
        if (locationId != null && locationId > 0L && !locationId.equals(company.getLocation().getId())) {
            Optional<Location> optionalLocation;
            try {
                optionalLocation = locationRepository.findById(locationId);
            } catch (Exception e) {
                throw new EntityNotFoundException("Can not get company location by id from DB, " + e.getCause());
            }
            Location location = optionalLocation.orElseThrow(() -> new NoSuchElementException("No company location with such id"));
            company.setLocation(location);
        }
        if (locationId != null && locationId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                ("Poor information about company location id in request body to patch update company. Must be more than 1L.");
        }
        return company;
    }
}

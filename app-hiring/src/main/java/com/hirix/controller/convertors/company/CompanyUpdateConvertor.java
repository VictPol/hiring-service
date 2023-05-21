package com.hirix.controller.convertors.company;

import com.hirix.controller.requests.update.CompanyUpdateRequest;
import com.hirix.domain.Company;
import com.hirix.domain.Location;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.CompanyRepository;
import com.hirix.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CompanyUpdateConvertor extends CompanyBaseConvertor<CompanyUpdateRequest, Company> {
    private final CompanyRepository companyRepository;
    private final LocationRepository locationRepository;

    @Override
    public Company convert(CompanyUpdateRequest request) {
        Long id;
        try {
            id = request.getId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                ("Poor information about company id in request body to update company. Must be Long type" + e.getCause());
        }
        if (id < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update company. " +
                "Id must be more than 0L");
        }
        Optional<Company> optionalCompany;
        try {
            optionalCompany = companyRepository.findById(id);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get company by id from DB, " + e.getCause());
        }
        Company company = optionalCompany.orElseThrow(() -> new NoSuchElementException("No company with such id"));
        Long userId;
        try {
            userId = request.getUserId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                ("Poor information about user id in request body to update company. Must be Long type" +
                    e.getCause());
        }
        if (!userId.equals(company.getUser().getId())) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                ("Can not update company, because user id does not correspond to this company");
        }
        Long locationId;
        try {
            locationId = request.getLocationId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                ("Poor information about location id in request body to update company. Must be Long type" +
                    e.getCause());
        }
        if (locationId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update company. " +
                "LocationId must be more than 0L");
        }
        if (!locationId.equals(company.getLocation().getId())) {
            Optional<Location> optionalLocation;
            try {
                optionalLocation = locationRepository.findById(locationId);
            } catch (Exception e) {
                throw new EntityNotFoundException("Can not get location by id from DB, " + e.getCause());
            }
            Location location = optionalLocation.orElseThrow(() -> new NoSuchElementException("No location with such id"));
            company.setLocation(location);
        }
        return doConvert(request, company);
    }
}

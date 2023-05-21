package com.hirix.controller.convertors.offer;

import com.hirix.controller.requests.update.OfferUpdateRequestCompany;
import com.hirix.domain.Offer;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OfferUpdateConvertorByCompany implements Converter<OfferUpdateRequestCompany, Offer> {
    private final OfferRepository offerRepository;

    @Override
    public Offer convert(OfferUpdateRequestCompany request) {
        Long id;
        try {
            id = request.getId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about offer id in request body to update offer by company. Must be Long type. " + e.getCause());
        }
        if (id < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update offer by company. " +
                    "Offer id must be more than 0L");
        }
        Optional<Offer> optionalOffer;
        try {
            optionalOffer = offerRepository.findById(id);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get offer by id from DB. " + e.getCause());
        }
        Offer offer = optionalOffer.orElseThrow(() -> new NoSuchElementException("No offer with such id"));

        offer.setContracted(request.isContracted());
        offer.setCommentsCompany(request.getCommentsCompany());
        return offer;
    }
}
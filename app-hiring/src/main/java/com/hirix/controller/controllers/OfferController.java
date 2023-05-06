package com.hirix.controller.controllers;

import com.hirix.domain.Company;
import com.hirix.domain.Offer;
import com.hirix.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
public class OfferController {
    private final OfferRepository offerRepository;

    @GetMapping
    public ResponseEntity<Object> getAllOffers() {
        List<Offer> offers = offerRepository.findAll();
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }
}

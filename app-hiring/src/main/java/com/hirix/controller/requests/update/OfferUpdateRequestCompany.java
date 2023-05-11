package com.hirix.controller.requests.update;

import com.hirix.controller.requests.create.OfferCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class OfferUpdateRequestCompany extends OfferCreateRequest {
    private Long id;
    private boolean contracted;
}

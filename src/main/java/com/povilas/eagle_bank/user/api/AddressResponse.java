package com.povilas.eagle_bank.user.api;

public record AddressResponse(
        String line1,
        String line2,
        String line3,
        String town,
        String county,
        String postcode
) {}

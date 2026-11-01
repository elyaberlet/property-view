package com.hotel.propertyview.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddressRequest(

        @NotNull
        Integer houseNumber,

        @NotBlank
        @Size(max = 255)
        String street,

        @NotBlank
        @Size(max = 100)
        String city,

        @NotBlank
        @Size(max = 100)
        String country,

        @NotBlank
        @Size(max = 20)
        String postCode

) {
}
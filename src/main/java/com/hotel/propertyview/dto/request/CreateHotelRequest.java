package com.hotel.propertyview.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateHotelRequest(
        @NotBlank
        @Size(max = 255)
        String name,

        @Size(max = 2000)
        String description,

        @NotBlank
        @Size(max = 100)
        String brand,

        @Valid
        @NotNull
        AddressRequest address,

        @Valid
        @NotNull
        ContactsRequest contacts,

        @Valid
        @NotNull
        ArrivalTimeRequest arrivalTime
) {}
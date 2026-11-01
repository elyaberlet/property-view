package com.hotel.propertyview.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record ArrivalTimeRequest(

        @NotNull
        LocalTime checkIn,

        LocalTime checkOut

) {
}
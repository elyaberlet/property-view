package com.hotel.propertyview.dto.response;

import java.time.LocalTime;

public record ArrivalTimeResponse(

        LocalTime checkIn,
        LocalTime checkOut

) {
}
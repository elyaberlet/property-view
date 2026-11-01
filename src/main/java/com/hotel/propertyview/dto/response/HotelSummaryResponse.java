package com.hotel.propertyview.dto.response;

public record HotelSummaryResponse(
        Long id,
        String name,
        String description,
        String address,
        String phone
) {
}
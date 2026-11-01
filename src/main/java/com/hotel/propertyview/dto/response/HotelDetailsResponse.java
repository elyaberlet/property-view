package com.hotel.propertyview.dto.response;

import java.util.Set;

public record HotelDetailsResponse(

        Long id,
        String name,
        String description,
        String brand,
        AddressResponse address,
        ContactsResponse contacts,
        ArrivalTimeResponse arrivalTime,
        Set<String> amenities

) {
}

package com.hotel.propertyview.service;

import com.hotel.propertyview.dto.request.CreateHotelRequest;
import com.hotel.propertyview.dto.response.HotelDetailsResponse;
import com.hotel.propertyview.dto.response.HotelSummaryResponse;

import java.util.List;
import java.util.Map;

public interface HotelService {

    List<HotelSummaryResponse> getAllHotels();

    HotelDetailsResponse getHotelById(Long id);

    List<HotelSummaryResponse> searchHotels(
            String name,
            String brand,
            String city,
            String country,
            String amenities
    );

    HotelSummaryResponse createHotel(CreateHotelRequest request);

    HotelDetailsResponse addAmenities(Long hotelId, List<String> amenities);

    Map<String, Long> getHistogram(String param);
}

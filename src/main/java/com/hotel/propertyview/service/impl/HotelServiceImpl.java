package com.hotel.propertyview.service.impl;

import com.hotel.propertyview.dto.request.CreateHotelRequest;
import com.hotel.propertyview.dto.response.HotelDetailsResponse;
import com.hotel.propertyview.dto.response.HotelSummaryResponse;
import com.hotel.propertyview.entity.Hotel;
import com.hotel.propertyview.enums.HistogramParam;
import com.hotel.propertyview.exception.HotelNotFoundException;
import com.hotel.propertyview.mapper.HotelMapper;
import com.hotel.propertyview.repository.HotelRepository;
import com.hotel.propertyview.service.HotelService;
import com.hotel.propertyview.specification.HotelSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Override
    public List<HotelSummaryResponse> getAllHotels() {
        return hotelRepository.findAll()
                .stream()
                .map(hotelMapper::toSummaryResponse)
                .toList();
    }

    @Override
    public HotelDetailsResponse getHotelById(Long id) {
        Hotel hotel = findHotelById(id);

        return hotelMapper.toDetailsResponse(hotel);
    }

    @Override
    public List<HotelSummaryResponse> searchHotels(
            String name,
            String brand,
            String city,
            String country,
            String amenities
    ) {
        Specification<Hotel> specification = Specification
                .where(HotelSpecification.hasName(name))
                .and(HotelSpecification.hasBrand(brand))
                .and(HotelSpecification.hasCity(city))
                .and(HotelSpecification.hasCountry(country))
                .and(HotelSpecification.hasAmenities(amenities));

        return hotelRepository.findAll(specification)
                .stream()
                .map(hotelMapper::toSummaryResponse)
                .toList();
    }

    @Override
    @Transactional
    public HotelSummaryResponse createHotel(CreateHotelRequest request) {
        Hotel hotel = hotelMapper.toEntity(request);

        // Если время выезда не указано, устанавливаем 12:00
        if (hotel.getArrivalTime().getCheckOut() == null) {
            hotel.getArrivalTime().setCheckOut(LocalTime.of(12, 0));
        }

        Hotel savedHotel = hotelRepository.save(hotel);

        return hotelMapper.toSummaryResponse(savedHotel);
    }

    @Override
    @Transactional
    public HotelDetailsResponse addAmenities(
            Long hotelId,
            List<String> amenities
    ) {
        Hotel hotel = findHotelById(hotelId);

        hotel.getAmenities().addAll(amenities);

        Hotel savedHotel = hotelRepository.save(hotel);

        return hotelMapper.toDetailsResponse(savedHotel);
    }

    @Override
    public Map<String, Long> getHistogram(String param) {
        HistogramParam histogramParam = HistogramParam.fromString(param);

        List<Object[]> results = switch (histogramParam) {
            case BRAND -> hotelRepository.countHotelsByBrand();
            case CITY -> hotelRepository.countHotelsByCity();
            case COUNTRY -> hotelRepository.countHotelsByCountry();
            case AMENITIES -> hotelRepository.countHotelsByAmenity();
        };

        return convertToHistogramMap(results);
    }

    private Map<String, Long> convertToHistogramMap(List<Object[]> results) {
        Map<String, Long> histogram = new LinkedHashMap<>();

        for (Object[] row : results) {
            String key = (String) row[0];
            Long count = ((Number) row[1]).longValue();

            histogram.put(key, count);
        }

        return histogram;
    }

    private Hotel findHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException(
                        "Hotel not found with id: " + id
                ));
    }
}

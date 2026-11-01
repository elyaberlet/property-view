package com.hotel.propertyview.service;

import com.hotel.propertyview.dto.request.CreateHotelRequest;
import com.hotel.propertyview.dto.response.HotelDetailsResponse;
import com.hotel.propertyview.dto.response.HotelSummaryResponse;
import com.hotel.propertyview.entity.Hotel;
import com.hotel.propertyview.exception.HotelNotFoundException;
import com.hotel.propertyview.mapper.HotelMapper;
import com.hotel.propertyview.repository.HotelRepository;
import com.hotel.propertyview.service.impl.HotelServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @Test
    void getAllHotels_shouldReturnHotelSummaries() {
        Hotel hotel1 = mock(Hotel.class);
        Hotel hotel2 = mock(Hotel.class);

        HotelSummaryResponse response1 = mock(HotelSummaryResponse.class);
        HotelSummaryResponse response2 = mock(HotelSummaryResponse.class);

        when(hotelRepository.findAll()).thenReturn(List.of(hotel1, hotel2));
        when(hotelMapper.toSummaryResponse(hotel1)).thenReturn(response1);
        when(hotelMapper.toSummaryResponse(hotel2)).thenReturn(response2);

        List<HotelSummaryResponse> result = hotelService.getAllHotels();

        assertEquals(2, result.size());
        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));

        verify(hotelRepository).findAll();
        verify(hotelMapper).toSummaryResponse(hotel1);
        verify(hotelMapper).toSummaryResponse(hotel2);
    }

    @Test
    void getHotelById_shouldReturnHotelDetails() {
        Long hotelId = 1L;
        Hotel hotel = mock(Hotel.class);
        HotelDetailsResponse response = mock(HotelDetailsResponse.class);

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(hotelMapper.toDetailsResponse(hotel)).thenReturn(response);

        HotelDetailsResponse result = hotelService.getHotelById(hotelId);

        assertEquals(response, result);
        verify(hotelRepository).findById(hotelId);
        verify(hotelMapper).toDetailsResponse(hotel);
    }

    @Test
    void getHotelById_shouldThrowExceptionWhenHotelNotFound() {
        Long hotelId = 999L;

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        assertThrows(
                HotelNotFoundException.class,
                () -> hotelService.getHotelById(hotelId)
        );

        verify(hotelRepository).findById(hotelId);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void searchHotels_shouldReturnMatchingHotels() {
        Hotel hotel = mock(Hotel.class);
        HotelSummaryResponse response = mock(HotelSummaryResponse.class);

        when(hotelRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(hotel));
        when(hotelMapper.toSummaryResponse(hotel)).thenReturn(response);

        List<HotelSummaryResponse> result = hotelService.searchHotels(
                "Grand",
                "Marriott",
                "New York",
                "USA",
                "Wi-Fi"
        );

        assertEquals(1, result.size());
        assertEquals(response, result.get(0));

        verify(hotelRepository).findAll(any(Specification.class));
        verify(hotelMapper).toSummaryResponse(hotel);
    }

    @Test
    void createHotel_shouldSaveAndReturnHotelSummary() {
        CreateHotelRequest request = mock(CreateHotelRequest.class);
        Hotel hotel = mock(Hotel.class);
        Hotel savedHotel = mock(Hotel.class);
        HotelSummaryResponse response = mock(HotelSummaryResponse.class);

        com.hotel.propertyview.entity.ArrivalTime arrivalTime =
                mock(com.hotel.propertyview.entity.ArrivalTime.class);

        when(hotelMapper.toEntity(request)).thenReturn(hotel);
        when(hotel.getArrivalTime()).thenReturn(arrivalTime);
        when(arrivalTime.getCheckOut()).thenReturn(null);
        when(hotelRepository.save(hotel)).thenReturn(savedHotel);
        when(hotelMapper.toSummaryResponse(savedHotel)).thenReturn(response);

        HotelSummaryResponse result = hotelService.createHotel(request);

        assertEquals(response, result);

        verify(hotelMapper).toEntity(request);
        verify(arrivalTime).setCheckOut(java.time.LocalTime.of(12, 0));
        verify(hotelRepository).save(hotel);
        verify(hotelMapper).toSummaryResponse(savedHotel);
    }

    @Test
    void addAmenities_shouldAddAmenitiesAndReturnHotelDetails() {
        Long hotelId = 1L;
        List<String> amenities = List.of("Wi-Fi", "Pool");

        Hotel hotel = mock(Hotel.class);
        Hotel savedHotel = mock(Hotel.class);
        HotelDetailsResponse response = mock(HotelDetailsResponse.class);

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(hotel.getAmenities()).thenReturn(new HashSet<>());
        when(hotelRepository.save(hotel)).thenReturn(savedHotel);
        when(hotelMapper.toDetailsResponse(savedHotel)).thenReturn(response);

        HotelDetailsResponse result = hotelService.addAmenities(hotelId, amenities);

        assertEquals(response, result);
        verify(hotelRepository).findById(hotelId);
        verify(hotelRepository).save(hotel);
        verify(hotelMapper).toDetailsResponse(savedHotel);
    }

    @Test
    void addAmenities_shouldThrowExceptionWhenHotelNotFound() {
        Long hotelId = 999L;

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        assertThrows(
                HotelNotFoundException.class,
                () -> hotelService.addAmenities(hotelId, List.of("Wi-Fi"))
        );

        verify(hotelRepository).findById(hotelId);
        verify(hotelRepository, never()).save(any());
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void getHistogram_shouldReturnBrandHistogram() {
        List<Object[]> results = List.of(
                new Object[]{"Marriott", 3L},
                new Object[]{"Hilton", 2L}
        );

        when(hotelRepository.countHotelsByBrand()).thenReturn(results);

        Map<String, Long> result = hotelService.getHistogram("brand");

        assertEquals(2, result.size());
        assertEquals(3L, result.get("Marriott"));
        assertEquals(2L, result.get("Hilton"));

        verify(hotelRepository).countHotelsByBrand();
    }

    @Test
    void getHistogram_shouldReturnCityHistogram() {
        List<Object[]> results = List.of(
                new Object[]{"New York", 4L},
                new Object[]{"London", 2L}
        );

        when(hotelRepository.countHotelsByCity()).thenReturn(results);

        Map<String, Long> result = hotelService.getHistogram("city");

        assertEquals(4L, result.get("New York"));
        assertEquals(2L, result.get("London"));

        verify(hotelRepository).countHotelsByCity();
    }

    @Test
    void getHistogram_shouldReturnCountryHistogram() {
        List<Object[]> results = List.of(
                new Object[]{"USA", 5L},
                new Object[]{"UK", 2L}
        );

        when(hotelRepository.countHotelsByCountry()).thenReturn(results);

        Map<String, Long> result = hotelService.getHistogram("country");

        assertEquals(5L, result.get("USA"));
        assertEquals(2L, result.get("UK"));

        verify(hotelRepository).countHotelsByCountry();
    }

    @Test
    void getHistogram_shouldReturnAmenitiesHistogram() {
        List<Object[]> results = List.of(
                new Object[]{"Wi-Fi", 5L},
                new Object[]{"Pool", 3L}
        );

        when(hotelRepository.countHotelsByAmenity()).thenReturn(results);

        Map<String, Long> result = hotelService.getHistogram("amenities");

        assertEquals(5L, result.get("Wi-Fi"));
        assertEquals(3L, result.get("Pool"));

        verify(hotelRepository).countHotelsByAmenity();
    }
}

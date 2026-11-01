package com.hotel.propertyview.controller;

import com.hotel.propertyview.dto.request.CreateHotelRequest;
import com.hotel.propertyview.dto.response.AddressResponse;
import com.hotel.propertyview.dto.response.ArrivalTimeResponse;
import com.hotel.propertyview.dto.response.ContactsResponse;
import com.hotel.propertyview.dto.response.HotelDetailsResponse;
import com.hotel.propertyview.dto.response.HotelSummaryResponse;
import com.hotel.propertyview.exception.HotelNotFoundException;
import com.hotel.propertyview.service.HotelService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HotelController.class)
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HotelService hotelService;

    @Test
    void getAllHotels_shouldReturnHotels() throws Exception {
        HotelSummaryResponse hotel = new HotelSummaryResponse(
                1L,
                "Grand Hotel",
                "Luxury hotel",
                "New York",
                "+123456789"
        );

        when(hotelService.getAllHotels()).thenReturn(List.of(hotel));

        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Grand Hotel"))
                .andExpect(jsonPath("$[0].description").value("Luxury hotel"))
                .andExpect(jsonPath("$[0].address").value("New York"))
                .andExpect(jsonPath("$[0].phone").value("+123456789"));

        verify(hotelService).getAllHotels();
    }

    @Test
    void getHotelById_shouldReturnHotelDetails() throws Exception {
        HotelDetailsResponse hotel = createHotelDetailsResponse();

        when(hotelService.getHotelById(1L)).thenReturn(hotel);

        mockMvc.perform(get("/hotels/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Grand Hotel"))
                .andExpect(jsonPath("$.brand").value("Hilton"))
                .andExpect(jsonPath("$.address.houseNumber").value(10))
                .andExpect(jsonPath("$.address.street").value("Broadway"))
                .andExpect(jsonPath("$.address.city").value("New York"))
                .andExpect(jsonPath("$.address.country").value("USA"))
                .andExpect(jsonPath("$.address.postCode").value("10001"))
                .andExpect(jsonPath("$.contacts.phone").value("+123456789"))
                .andExpect(jsonPath("$.contacts.email").value("hotel@example.com"))
                .andExpect(jsonPath("$.arrivalTime.checkIn").value("14:00:00"))
                .andExpect(jsonPath("$.arrivalTime.checkOut").value("12:00:00"))
                .andExpect(jsonPath("$.amenities").isArray())
                .andExpect(jsonPath("$.amenities.length()").value(2));

        verify(hotelService).getHotelById(1L);
    }

    @Test
    void getHotelById_shouldReturn404WhenHotelNotFound() throws Exception {
        when(hotelService.getHotelById(999L))
                .thenThrow(new HotelNotFoundException(
                        "Hotel not found with id: 999"
                ));

        mockMvc.perform(get("/hotels/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(hotelService).getHotelById(999L);
    }

    @Test
    void createHotel_shouldReturnCreatedHotel() throws Exception {
        HotelSummaryResponse response = new HotelSummaryResponse(
                1L,
                "Grand Hotel",
                "Luxury hotel",
                "New York",
                "+123456789"
        );

        when(hotelService.createHotel(any(CreateHotelRequest.class)))
                .thenReturn(response);

        String requestJson = """
                {
                    "name": "Grand Hotel",
                    "description": "Luxury hotel",
                    "brand": "Hilton",
                    "address": {
                        "houseNumber": 10,
                        "street": "Broadway",
                        "city": "New York",
                        "country": "USA",
                        "postCode": "10001"
                    },
                    "contacts": {
                        "phone": "+123456789",
                        "email": "hotel@example.com"
                    },
                    "arrivalTime": {
                        "checkIn": "14:00:00",
                        "checkOut": "12:00:00"
                    }
                }
                """;

        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Grand Hotel"))
                .andExpect(jsonPath("$.description").value("Luxury hotel"))
                .andExpect(jsonPath("$.address").value("New York"))
                .andExpect(jsonPath("$.phone").value("+123456789"));

        verify(hotelService).createHotel(any(CreateHotelRequest.class));
    }

    @Test
    void createHotel_shouldReturn400WhenRequestIsInvalid() throws Exception {
        String requestJson = """
                {
                    "name": "",
                    "description": "Luxury hotel",
                    "brand": "Hilton",
                    "address": {
                        "houseNumber": 10,
                        "street": "Broadway",
                        "city": "New York",
                        "country": "USA",
                        "postCode": "10001"
                    },
                    "contacts": {
                        "phone": "+123456789",
                        "email": "hotel@example.com"
                    },
                    "arrivalTime": {
                        "checkIn": "14:00:00",
                        "checkOut": "12:00:00"
                    }
                }
                """;

        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(hotelService);
    }

    @Test
    void addAmenities_shouldReturnUpdatedHotel() throws Exception {
        HotelDetailsResponse hotel = createHotelDetailsResponse();

        when(hotelService.addAmenities(
                1L,
                List.of("WiFi", "Swimming pool")
        )).thenReturn(hotel);

        mockMvc.perform(post("/hotels/{id}/amenities", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                ["WiFi", "Swimming pool"]
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amenities").isArray())
                .andExpect(jsonPath("$.amenities.length()").value(2));

        verify(hotelService).addAmenities(
                1L,
                List.of("WiFi", "Swimming pool")
        );
    }

    private HotelDetailsResponse createHotelDetailsResponse() {
        return new HotelDetailsResponse(
                1L,
                "Grand Hotel",
                "Luxury hotel",
                "Hilton",
                new AddressResponse(
                        10,
                        "Broadway",
                        "New York",
                        "USA",
                        "10001"
                ),
                new ContactsResponse(
                        "+123456789",
                        "hotel@example.com"
                ),
                new ArrivalTimeResponse(
                        LocalTime.of(14, 0),
                        LocalTime.of(12, 0)
                ),
                Set.of("WiFi", "Swimming pool")
        );
    }

    @Test
    void addAmenities_shouldReturn404WhenHotelNotFound() throws Exception {
        when(hotelService.addAmenities(999L, List.of("WiFi")))
                .thenThrow(new HotelNotFoundException(
                        "Hotel not found with id: 999"
                ));

        mockMvc.perform(post("/hotels/{id}/amenities", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            ["WiFi"]
                            """))
                .andExpect(status().isNotFound());

        verify(hotelService).addAmenities(999L, List.of("WiFi"));
    }
}
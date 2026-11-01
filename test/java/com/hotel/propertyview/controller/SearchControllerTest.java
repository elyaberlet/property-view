
package com.hotel.propertyview.controller;

import com.hotel.propertyview.dto.response.HotelSummaryResponse;
import com.hotel.propertyview.service.HotelService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HotelService hotelService;

    @Test
    void searchHotels_shouldReturnMatchingHotels() throws Exception {
        HotelSummaryResponse hotel = new HotelSummaryResponse(
                1L,
                "Grand Hotel",
                "Luxury hotel",
                "New York",
                "+123456789"
        );

        when(hotelService.searchHotels(
                "Grand Hotel",
                "Hilton",
                "New York",
                "USA",
                "WiFi"
        )).thenReturn(List.of(hotel));

        mockMvc.perform(get("/search")
                        .param("name", "Grand Hotel")
                        .param("brand", "Hilton")
                        .param("city", "New York")
                        .param("country", "USA")
                        .param("amenities", "WiFi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Grand Hotel"))
                .andExpect(jsonPath("$[0].description").value("Luxury hotel"))
                .andExpect(jsonPath("$[0].address").value("New York"))
                .andExpect(jsonPath("$[0].phone").value("+123456789"));

        verify(hotelService).searchHotels(
                "Grand Hotel",
                "Hilton",
                "New York",
                "USA",
                "WiFi"
        );
    }

    @Test
    void searchHotels_shouldReturnEmptyListWhenNoHotelsFound() throws Exception {
        when(hotelService.searchHotels(
                "Unknown Hotel",
                null,
                null,
                null,
                null
        )).thenReturn(List.of());

        mockMvc.perform(get("/search")
                        .param("name", "Unknown Hotel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(hotelService).searchHotels(
                "Unknown Hotel",
                null,
                null,
                null,
                null
        );
    }

    @Test
    void searchHotels_shouldReturnAllHotelsWhenNoFiltersProvided() throws Exception {
        HotelSummaryResponse hotel = new HotelSummaryResponse(
                1L,
                "Grand Hotel",
                "Luxury hotel",
                "New York",
                "+123456789"
        );

        when(hotelService.searchHotels(
                null,
                null,
                null,
                null,
                null
        )).thenReturn(List.of(hotel));

        mockMvc.perform(get("/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(hotelService).searchHotels(
                null,
                null,
                null,
                null,
                null
        );
    }
}

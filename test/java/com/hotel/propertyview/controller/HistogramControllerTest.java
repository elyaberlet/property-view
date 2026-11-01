
package com.hotel.propertyview.controller;

import com.hotel.propertyview.service.HotelService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HistogramController.class)
class HistogramControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HotelService hotelService;

    @Test
    void getHistogram_shouldReturnStatisticsByCity() throws Exception {
        Map<String, Long> histogram = Map.of(
                "New York", 5L,
                "London", 3L,
                "Paris", 2L
        );

        when(hotelService.getHistogram("city")).thenReturn(histogram);

        mockMvc.perform(get("/histogram/{param}", "city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['New York']").value(5))
                .andExpect(jsonPath("$.London").value(3))
                .andExpect(jsonPath("$.Paris").value(2));

        verify(hotelService).getHistogram("city");
    }

    @Test
    void getHistogram_shouldReturnStatisticsByBrand() throws Exception {
        Map<String, Long> histogram = Map.of(
                "Hilton", 4L,
                "Marriott", 3L
        );

        when(hotelService.getHistogram("brand")).thenReturn(histogram);

        mockMvc.perform(get("/histogram/{param}", "brand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Hilton").value(4))
                .andExpect(jsonPath("$.Marriott").value(3));

        verify(hotelService).getHistogram("brand");
    }

    @Test
    void getHistogram_shouldReturnEmptyMapWhenNoStatisticsFound() throws Exception {
        when(hotelService.getHistogram("country")).thenReturn(Map.of());

        mockMvc.perform(get("/histogram/{param}", "country"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(hotelService).getHistogram("country");
    }
}

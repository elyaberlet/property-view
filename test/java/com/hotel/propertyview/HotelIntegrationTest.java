
package com.hotel.propertyview;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class HotelIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String createHotelRequest() {
        return """
                {
                    "name": "Test Hotel",
                    "description": "Hotel for integration testing",
                    "brand": "Test Brand",
                    "address": {
                        "houseNumber": 10,
                        "street": "Main Street",
                        "city": "New York",
                        "country": "USA",
                        "postCode": "10001"
                    },
                    "contacts": {
                        "phone": "+1234567890",
                        "email": "test@hotel.com"
                    },
                    "arrivalTime": {
                        "checkIn": "14:00",
                        "checkOut": "11:00"
                    }
                }
                """;
    }

    private long createHotelAndGetId() throws Exception {
        MvcResult result = mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createHotelRequest()))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode response = objectMapper.readTree(
                result.getResponse().getContentAsString()
        );

        return response.get("id").asLong();
    }

    @Test
    void createHotel_shouldSaveHotelAndReturnIt() throws Exception {
        MvcResult result = mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createHotelRequest()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Hotel"))
                .andExpect(jsonPath("$.description")
                        .value("Hotel for integration testing"))
                .andExpect(jsonPath("$.phone").value("+1234567890"))
                .andReturn();

        JsonNode response = objectMapper.readTree(
                result.getResponse().getContentAsString()
        );

        long hotelId = response.get("id").asLong();

        mockMvc.perform(get("/hotels/{id}", hotelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(hotelId))
                .andExpect(jsonPath("$.name").value("Test Hotel"))
                .andExpect(jsonPath("$.brand").value("Test Brand"))
                .andExpect(jsonPath("$.address.city").value("New York"))
                .andExpect(jsonPath("$.contacts.email")
                        .value("test@hotel.com"))
                .andExpect(jsonPath("$.arrivalTime.checkIn")
                        .value("14:00:00"));
    }

    @Test
    void getAllHotels_shouldReturnSavedHotels() throws Exception {
        createHotelAndGetId();

        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value("Test Hotel"))
                .andExpect(jsonPath("$[0].description")
                        .value("Hotel for integration testing"))
                .andExpect(jsonPath("$[0].address")
                        .value("10 Main Street, New York, 10001, USA"))
                .andExpect(jsonPath("$[0].phone").value("+1234567890"));
    }

    @Test
    void getHotelById_shouldReturnHotelDetails() throws Exception {
        long hotelId = createHotelAndGetId();

        mockMvc.perform(get("/hotels/{id}", hotelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(hotelId))
                .andExpect(jsonPath("$.name").value("Test Hotel"))
                .andExpect(jsonPath("$.address.street")
                        .value("Main Street"))
                .andExpect(jsonPath("$.contacts.phone")
                        .value("+1234567890"));
    }

    @Test
    void getHotelById_shouldReturn404WhenHotelNotFound() throws Exception {
        mockMvc.perform(get("/hotels/{id}", 999999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void searchHotels_shouldFindHotelByName() throws Exception {
        createHotelAndGetId();

        mockMvc.perform(get("/search")
                        .param("name", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Hotel"));
    }

    @Test
    void searchHotels_shouldFindHotelByCity() throws Exception {
        createHotelAndGetId();

        mockMvc.perform(get("/search")
                        .param("city", "New York"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Hotel"));
    }

    @Test
    void searchHotels_shouldReturnEmptyListWhenNoHotelsFound()
            throws Exception {
        mockMvc.perform(get("/search")
                        .param("name", "Nonexistent Hotel"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void addAmenities_shouldSaveAmenities() throws Exception {
        long hotelId = createHotelAndGetId();

        String amenities = """
                [
                    "Free Wi-Fi",
                    "Swimming Pool",
                    "Parking"
                ]
                """;

        mockMvc.perform(post("/hotels/{id}/amenities", hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(amenities))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amenities.length()").value(3))
                .andExpect(jsonPath("$.amenities").isArray());

        mockMvc.perform(get("/hotels/{id}", hotelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amenities.length()").value(3))
                .andExpect(jsonPath("$.amenities").isArray());
    }

    @Test
    void addAmenities_shouldReturn404WhenHotelNotFound()
            throws Exception {
        mockMvc.perform(post("/hotels/{id}/amenities", 999999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"Free Wi-Fi\"]"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getHistogram_shouldReturnStatisticsByBrand() throws Exception {
        createHotelAndGetId();

        mockMvc.perform(get("/histogram/brand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Test Brand']").value(1));
    }

    @Test
    void getHistogram_shouldReturnStatisticsByCity() throws Exception {
        createHotelAndGetId();

        mockMvc.perform(get("/histogram/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['New York']").value(1));
    }

    @Test
    void getHistogram_shouldReturnStatisticsByCountry() throws Exception {
        createHotelAndGetId();

        mockMvc.perform(get("/histogram/country"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['USA']").value(1));
    }

    @Test
    void getHistogram_shouldReturnStatisticsByAmenities() throws Exception {
        long hotelId = createHotelAndGetId();

        mockMvc.perform(post("/hotels/{id}/amenities", hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"Free Wi-Fi\", \"Parking\"]"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/histogram/amenities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Free Wi-Fi']").value(1))
                .andExpect(jsonPath("$.['Parking']").value(1));
    }

    @Test
    void createHotelWithoutCheckOut_shouldSetDefaultTime()
            throws Exception {
        String request = """
                {
                    "name": "Hotel Without CheckOut",
                    "description": "Test description",
                    "brand": "Test Brand",
                    "address": {
                        "houseNumber": 10,
                        "street": "Main Street",
                        "city": "New York",
                        "country": "USA",
                        "postCode": "10001"
                    },
                    "contacts": {
                        "phone": "+1234567890",
                        "email": "test@example.com"
                    },
                    "arrivalTime": {
                        "checkIn": "14:00"
                    }
                }
                """;

        String response = mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long hotelId = objectMapper.readTree(response)
                .get("id").asLong();

        mockMvc.perform(get("/hotels/{id}", hotelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.arrivalTime.checkOut")
                        .value("12:00:00"));
    }

    @Test
    void addDuplicateAmenities_shouldNotCreateDuplicates()
            throws Exception {
        long hotelId = createHotelAndGetId();

        mockMvc.perform(post("/hotels/{id}/amenities", hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"Wi-Fi\", \"Pool\"]"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/hotels/{id}/amenities", hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"Wi-Fi\", \"Pool\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amenities.length()").value(2));
    }

    @Test
    void searchWithoutFilters_shouldReturnAllHotels() throws Exception {
        createHotelAndGetId();
        createHotelAndGetId();

        mockMvc.perform(get("/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getHistogramWithUnsupportedParameter_shouldReturnInternalServerError()
            throws Exception {
        mockMvc.perform(get("/histogram/unknown"))
                .andExpect(status().isInternalServerError());
    }
}

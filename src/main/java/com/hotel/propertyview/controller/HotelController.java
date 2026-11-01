
package com.hotel.propertyview.controller;

import com.hotel.propertyview.dto.request.CreateHotelRequest;
import com.hotel.propertyview.dto.response.HotelDetailsResponse;
import com.hotel.propertyview.dto.response.HotelSummaryResponse;
import com.hotel.propertyview.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
@Tag(
        name = "Hotels",
        description = "Operations for managing hotels"
)
public class HotelController {

    private final HotelService hotelService;

    @Operation(
            summary = "Get all hotels",
            description = "Returns a list of all hotels with brief information."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Hotels successfully retrieved"
    )
    @GetMapping
    public List<HotelSummaryResponse> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @Operation(
            summary = "Get hotel by ID",
            description = "Returns detailed information about a specific hotel, including address, contacts, arrival time and amenities."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Hotel successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Hotel not found"
            )
    })
    @GetMapping("/{id}")
    public HotelDetailsResponse getHotelById(
            @Parameter(
                    description = "Unique identifier of the hotel",
                    example = "1",
                    required = true
            )
            @PathVariable Long id
    ) {
        return hotelService.getHotelById(id);
    }

    @Operation(
            summary = "Create a new hotel",
            description = "Creates a hotel using the provided information and returns its brief details."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Hotel successfully created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid hotel data"
            )
    })
    @PostMapping
    public ResponseEntity<HotelSummaryResponse> createHotel(
            @Valid @RequestBody CreateHotelRequest request
    ) {
        HotelSummaryResponse response = hotelService.createHotel(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Add amenities to a hotel",
            description = "Adds a list of amenities to the specified hotel. The request body must contain a JSON array of strings."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Amenities successfully added"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Hotel not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            )
    })
    @PostMapping("/{id}/amenities")
    public HotelDetailsResponse addAmenities(
            @Parameter(
                    description = "Unique identifier of the hotel",
                    example = "1",
                    required = true
            )
            @PathVariable Long id,

            @RequestBody List<String> amenities
    ) {
        return hotelService.addAmenities(id, amenities);
    }
}


package com.hotel.propertyview.controller;

import com.hotel.propertyview.dto.response.HotelSummaryResponse;
import com.hotel.propertyview.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@Tag(
        name = "Hotel Search",
        description = "Search hotels using different criteria"
)
public class SearchController {

    private final HotelService hotelService;

    @Operation(
            summary = "Search hotels",
            description = """
                    Searches for hotels using optional filters.
                    Multiple filters can be combined in a single request.
                    Returns brief information about matching hotels.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Search completed successfully"
    )
    @GetMapping
    public List<HotelSummaryResponse> searchHotels(

            @Parameter(
                    description = "Hotel name",
                    example = "Grand Plaza Hotel"
            )
            @RequestParam(required = false) String name,

            @Parameter(
                    description = "Hotel brand",
                    example = "Marriott"
            )
            @RequestParam(required = false) String brand,

            @Parameter(
                    description = "City where the hotel is located",
                    example = "New York"
            )
            @RequestParam(required = false) String city,

            @Parameter(
                    description = "Country where the hotel is located",
                    example = "USA"
            )
            @RequestParam(required = false) String country,

            @Parameter(
                    description = "Hotel amenity",
                    example = "WiFi"
            )
            @RequestParam(required = false) String amenities
    ) {
        return hotelService.searchHotels(
                name,
                brand,
                city,
                country,
                amenities
        );
    }
}

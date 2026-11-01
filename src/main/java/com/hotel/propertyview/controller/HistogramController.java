
package com.hotel.propertyview.controller;

import com.hotel.propertyview.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/histogram")
@RequiredArgsConstructor
@Tag(
        name = "Hotel Statistics",
        description = "Endpoints for generating hotel statistics"
)
public class HistogramController {

    private final HotelService hotelService;

    @Operation(
            summary = "Get hotel statistics",
            description = """
                    Returns the number of hotels grouped by the specified parameter.
                    Supported parameters: brand, city, country, amenities.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Statistics successfully generated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid histogram parameter"
            )
    })
    @GetMapping("/{param}")
    public Map<String, Long> getHistogram(
            @Parameter(
                    description = "Parameter used to group hotels: brand, city, country or amenities",
                    example = "city",
                    required = true
            )
            @PathVariable String param
    ) {
        return hotelService.getHistogram(param);
    }
}

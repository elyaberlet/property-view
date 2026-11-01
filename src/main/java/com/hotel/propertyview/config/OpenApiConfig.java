
package com.hotel.propertyview.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Property View — Hotel Management API",
                version = "1.0.0",
                description = """
                        REST API for hotel management.

                        The API provides functionality for:
                        - Retrieving hotel information
                        - Searching hotels by different criteria
                        - Creating hotels
                        - Managing hotel amenities
                        - Generating hotel statistics
                        """,
                contact = @Contact(
                        name = "Property View"
                ),
                license = @License(
                        name = "MIT License"
                )
        )
)
public class OpenApiConfig {
}

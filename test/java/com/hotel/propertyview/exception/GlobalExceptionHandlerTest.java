package com.hotel.propertyview.exception;

import com.hotel.propertyview.dto.response.ApiErrorResponse;

import com.hotel.propertyview.exception.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler =
            new GlobalExceptionHandler();

    @Test
    void handleHotelNotFound_shouldReturn404() {
        HotelNotFoundException exception =
                new HotelNotFoundException("Hotel not found with id: 999");

        ResponseEntity<ApiErrorResponse> response =
                exceptionHandler.handleHotelNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().status());
        assertEquals("Not Found", response.getBody().error());
        assertEquals("Hotel not found with id: 999", response.getBody().message());
        assertTrue(response.getBody().validationErrors().isEmpty());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    void handleValidationErrors_shouldReturn400() {
        BindingResult bindingResult = new BeanPropertyBindingResult(
                new Object(),
                "createHotelRequest"
        );

        bindingResult.addError(
                new FieldError(
                        "createHotelRequest",
                        "name",
                        "",
                        false,
                        null,
                        null,
                        "must not be blank"
                )
        );

        bindingResult.addError(
                new FieldError(
                        "createHotelRequest",
                        "brand",
                        "",
                        false,
                        null,
                        null,
                        "must not be blank"
                )
        );

        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ApiErrorResponse> response =
                exceptionHandler.handleValidationErrors(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().status());
        assertEquals("Bad Request", response.getBody().error());
        assertEquals("Validation failed", response.getBody().message());
        assertEquals(2, response.getBody().validationErrors().size());
        assertEquals(
                "must not be blank",
                response.getBody().validationErrors().get("name")
        );
        assertEquals(
                "must not be blank",
                response.getBody().validationErrors().get("brand")
        );
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    void handleGeneralException_shouldReturn500() {
        Exception exception = new RuntimeException("Unexpected error");

        ResponseEntity<ApiErrorResponse> response =
                exceptionHandler.handleGeneralException(exception);

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode()
        );
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().status());
        assertEquals("Internal Server Error", response.getBody().error());
        assertEquals(
                "An unexpected error occurred",
                response.getBody().message()
        );
        assertTrue(response.getBody().validationErrors().isEmpty());
        assertNotNull(response.getBody().timestamp());
    }
}

package com.hotel.propertyview.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContactsRequest(

        @NotBlank
        @Size(max = 30)
        String phone,

        @NotBlank
        @Email
        @Size(max = 255)
        String email

) {
}
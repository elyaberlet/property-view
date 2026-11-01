package com.hotel.propertyview.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Address {

    @NotNull
    @Column(name = "house_number", nullable = false)
    private Integer houseNumber;

    @NotBlank
    @Size(max = 255)
    @Column(name = "street", nullable = false)
    private String street;

    @NotBlank
    @Size(max = 100)
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @NotBlank
    @Size(max = 100)
    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @NotBlank
    @Size(max = 20)
    @Column(name = "post_code", nullable = false, length = 20)
    private String postCode;
}
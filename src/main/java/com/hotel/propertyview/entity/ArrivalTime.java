package com.hotel.propertyview.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class ArrivalTime {

    @NotNull
    @Column(name = "check_in", nullable = false)
    private LocalTime checkIn;

    @NotNull
    @Column(name = "check_out", nullable = false)
    private LocalTime checkOut;
}
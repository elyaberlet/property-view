package com.hotel.propertyview.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String description;

    @NotBlank
    @Size(max = 100)
    @Column(name = "brand", nullable = false, length = 100)
    private String brand;

    @Valid
    @NotNull
    @Embedded
    private Address address;

    @Valid
    @NotNull
    @Embedded
    private Contacts contacts;

    @Valid
    @NotNull
    @Embedded
    private ArrivalTime arrivalTime;

    @ElementCollection
    @CollectionTable(
            name = "hotel_amenities",
            joinColumns = @JoinColumn(name = "hotel_id")
    )
    @Column(name = "amenity", nullable = false)
    private Set<@NotBlank @Size(max = 255) String> amenities = new HashSet<>();
}
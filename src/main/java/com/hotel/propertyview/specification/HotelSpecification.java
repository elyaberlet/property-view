package com.hotel.propertyview.specification;

import com.hotel.propertyview.entity.Hotel;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class HotelSpecification {

    public static Specification<Hotel> hasName(String name) {
        return likeIgnoreCase("name", name);
    }

    public static Specification<Hotel> hasBrand(String brand) {
        return likeIgnoreCase("brand", brand);
    }

    public static Specification<Hotel> hasCity(String city) {
        return likeIgnoreCase("address.city", city);
    }

    public static Specification<Hotel> hasCountry(String country) {
        return likeIgnoreCase("address.country", country);
    }

    public static Specification<Hotel> hasAmenities(String amenities) {
        return (root, query, cb) -> {

            if (amenities == null || amenities.isBlank()) {
                return null;
            }

            Join<Hotel, String> amenitiesJoin = root.join("amenities");

            query.distinct(true);

            return cb.equal(
                    cb.lower(amenitiesJoin),
                    amenities.toLowerCase(Locale.ROOT)
            );
        };
    }

    private static Specification<Hotel> likeIgnoreCase(
            String fieldPath, String value
    ) {
        return (root, query, cb) -> {

            if (value == null || value.isBlank()) {
                return null;
            }

            String[] parts = fieldPath.split("\\.");

            Path<String> path = root.get(parts[0]);

            for (int i = 1; i < parts.length; i++) {
                path = path.get(parts[i]);
            }

            return cb.like(
                    cb.lower(path),
                    "%" + value.toLowerCase(Locale.ROOT) + "%"
            );
        };
    }
}
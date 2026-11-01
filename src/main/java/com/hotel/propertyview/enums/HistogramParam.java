
package com.hotel.propertyview.enums;

import java.util.Locale;

public enum HistogramParam {

    BRAND,
    CITY,
    COUNTRY,
    AMENITIES;

    public static HistogramParam fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Histogram parameter cannot be null or empty"
            );
        }

        try {
            return HistogramParam.valueOf(
                    value.trim().toUpperCase(Locale.ROOT)
            );
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Unsupported histogram parameter: " + value
            );
        }
    }
}

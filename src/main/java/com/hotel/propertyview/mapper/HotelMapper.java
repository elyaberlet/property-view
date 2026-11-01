
package com.hotel.propertyview.mapper;

import com.hotel.propertyview.dto.request.AddressRequest;
import com.hotel.propertyview.dto.request.ArrivalTimeRequest;
import com.hotel.propertyview.dto.request.ContactsRequest;
import com.hotel.propertyview.dto.request.CreateHotelRequest;
import com.hotel.propertyview.dto.response.*;
import com.hotel.propertyview.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    @Mapping(
            target = "address",
            expression = "java(formatAddress(hotel.getAddress()))"
    )
    @Mapping(target = "phone", source = "contacts.phone")
    HotelSummaryResponse toSummaryResponse(Hotel hotel);

    HotelDetailsResponse toDetailsResponse(Hotel hotel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    Hotel toEntity(CreateHotelRequest request);

    AddressResponse toAddressResponse(Address address);

    ContactsResponse toContactsResponse(Contacts contacts);

    ArrivalTimeResponse toArrivalTimeResponse(ArrivalTime arrivalTime);

    Address toAddress(AddressRequest request);

    Contacts toContacts(ContactsRequest request);

    ArrivalTime toArrivalTime(ArrivalTimeRequest request);

    default String formatAddress(Address address) {
        if (address == null) {
            return null;
        }

        return String.format(
                "%d %s, %s, %s, %s",
                address.getHouseNumber(),
                address.getStreet(),
                address.getCity(),
                address.getPostCode(),
                address.getCountry()
        );
    }
}

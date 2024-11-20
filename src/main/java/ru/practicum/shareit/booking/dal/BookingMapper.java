package ru.practicum.shareit.booking.dal;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;


import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    Booking toBooking(BookingDto bookingDto);

    BookingDto bookingToBookingDto(Booking booking);

    Booking updateFromDto(BookingDto bookingDto, @MappingTarget Booking booking);

    List<BookingDto> listBookingToListBookingDto(List<Booking> listBookings);
}

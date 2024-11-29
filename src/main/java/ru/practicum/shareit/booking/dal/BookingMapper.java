package ru.practicum.shareit.booking.dal;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreated;
import ru.practicum.shareit.booking.dto.BookingDtoFull;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;


import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(source = "item", target = "item")
    @Mapping(source = "booker", target = "booker")
    @Mapping(target = "id", ignore = true)
    Booking bookingDtoCreatedltoBooking(User booker, Item item, BookingDtoCreated bookingDtoCreated);

    BookingDto boookingToBookingDto(Booking booking);
    List<BookingDto> listBookingToListBookingDto(List<Booking> listBookings);



/*    todo BookingDtoFull bookingDtoCreatedtoBookingDtoFull(BookingDtoCreated bookingDtoCreated);*/


/*    BookingDtoCreated bookingToBookingDtoCreated(Booking booking);

    Booking updateFromDto(BookingDtoCreated bookingDtoCreated, @MappingTarget Booking booking);

    List<BookingDtoCreated> listBookingToListBookingDto(List<Booking> listBookings);*/
}

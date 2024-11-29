package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreated;

import java.util.List;

public interface BookingService {

    BookingDto create(long userId, BookingDtoCreated bookingDtoCreated);

    BookingDto approve(long userId, long bookingId, boolean approved);

    BookingDto get(long userId, long bookingId);

    List<BookingDto> getAllByUser(long userId, State state);

    List<BookingDto> getAllByOwner(long userId, State state);
}

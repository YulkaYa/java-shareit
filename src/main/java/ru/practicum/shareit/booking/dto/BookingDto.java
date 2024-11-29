package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.Booking;

/**
 * TODO Sprint add-bookings.
 */
@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor
public class BookingDto extends Booking {
}

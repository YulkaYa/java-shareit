package ru.practicum.shareit.booking.dal;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;

public interface BookingDBRepository extends JpaRepository<Booking, Long> {
}

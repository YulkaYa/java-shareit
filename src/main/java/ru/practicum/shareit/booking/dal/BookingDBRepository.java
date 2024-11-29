package ru.practicum.shareit.booking.dal;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingDBRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long bookerId, Sort sort);
    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime start, LocalDateTime end, Sort sort);
    List<Booking> findByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime end, Sort sort);
    List<Booking> findByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime start, Sort sort);
    List<Booking> findByBookerIdAndStatus(Long bookerId, Status status, Sort sort);

    List<Booking> findByItemOwnerId(Long ownerId, Sort sort);

    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(long ownerId, LocalDateTime dateTimeNow, LocalDateTime dateTimeNow1, Sort sort);

    List<Booking> findByItemOwnerIdAndEndIsBefore(long ownerId, LocalDateTime dateTimeNow, Sort sort);

    List<Booking> findByItemOwnerIdAndStartIsAfter(long ownerId, LocalDateTime dateTimeNow, Sort sort);

    List<Booking> findByItemOwnerIdAndStatus(long ownerId, Status status, Sort sort);

    List<Booking> findByItemIdAndBookerIdAndEndIsBefore(Long itemId, Long bookerId, LocalDateTime end);
}

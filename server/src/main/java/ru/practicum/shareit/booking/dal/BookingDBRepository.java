package ru.practicum.shareit.booking.dal;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingDBRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long bookerId, Sort sort);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterAndStatusNotRejectedAndStatusNotCanceledIgnoreCase(Long bookerId, LocalDateTime date, LocalDateTime date1, Sort sort);

    List<Booking> findByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime date, Sort sort);

    List<Booking> findByBookerIdAndStartIsAfterAndStatusNotRejectedAndStatusNotCanceledIgnoreCase(Long bookerId, LocalDateTime date, Sort sort);

    List<Booking> findByBookerIdAndStatus(Long bookerId, Status status, Sort sort);

    List<Booking> findByItemOwnerId(Long ownerId, Sort sort);

    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfterAndStatusNotRejectedAndStatusNotCanceledIgnoreCase(long ownerId, LocalDateTime date, LocalDateTime date1, Sort sort);

    List<Booking> findByItemOwnerIdAndEndIsBefore(long ownerId, LocalDateTime date, Sort sort);

    List<Booking> findByItemOwnerIdAndStartIsAfterAndStatusNotRejectedAndStatusNotCanceledIgnoreCase(long ownerId, LocalDateTime date, Sort sort);

    List<Booking> findByItemOwnerIdAndStatus(long ownerId, Status status, Sort sort);

    List<Booking> findByItemIdAndBookerIdAndEndIsBefore(Long itemId, Long bookerId, LocalDateTime end);


    List<Booking> findByItemIdAndStartIsBefore(long itemId, LocalDateTime date, Sort sort);

    List<Booking> findByItemIdAndStartIsAfter(long itemId, LocalDateTime date, Sort sort);

    List<Booking> findByItemIdAndStartIsBeforeAndEndIsAfter(long itemId, LocalDateTime date, LocalDateTime date1, Sort sort);
}

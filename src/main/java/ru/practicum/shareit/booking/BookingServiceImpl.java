package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Order;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dal.BookingDBRepository;
import ru.practicum.shareit.booking.dal.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreated;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.item.ItemBaseRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserBaseRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookingServiceImpl implements BookingService {
    private final ItemBaseRepository itemRepository;
    private final UserBaseRepository userRepository;
    private final BookingDBRepository bookingDBRepository;
    private final BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);

    @Override
    public BookingDto create(long userId, BookingDtoCreated bookingDtoCreated) {
        long itemId = bookingDtoCreated.getItemId();
        User booker = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + "не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Товар с id=" + itemId + " не найден"));

        if (item.isAvailable()) {
            Booking booking = bookingMapper.bookingDtoCreatedltoBooking(booker, item, bookingDtoCreated);
            booking = bookingDBRepository.save(booking);
            return bookingMapper.boookingToBookingDto(booking);
        } else throw new ConditionsNotMetException("Товар с id = " + itemId + " недоступен для бронирования");
    }

    @Override
    public BookingDto approve(long userId, long bookingId, boolean approved) {
        Booking booking = bookingDBRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id=" + bookingId + " не найдено"));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new ConditionsNotMetException("Id владельца и id пользователя в запросе не совпадают");
        } else booking.setStatusByIsApproved(approved);
        return bookingMapper.boookingToBookingDto(bookingDBRepository.save(booking));
    }

    @Override
    public BookingDto get(long userId, long bookingId) {
        Booking booking = bookingDBRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id=" + bookingId + " не найдено"));
        if ((booking.getBooker().getId() != userId) && (booking.getItem().getOwner().getId() != userId)) {
            throw new ConditionsNotMetException("Id владельца/арендатора и id пользователя в запросе не совпадают");
        } else return bookingMapper.boookingToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllByUser(long userId, State state) {
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> bookingList = new ArrayList<>();
        LocalDateTime dateTimeNow = LocalDateTime.now();
        switch (state) {
            case ALL -> {
                bookingList = bookingDBRepository.findByBookerId(userId, sort);
                break;
            }
            case CURRENT -> {
                bookingList = bookingDBRepository
                        .findByBookerIdAndStartIsBeforeAndEndIsAfter(userId, dateTimeNow,
                                dateTimeNow, sort);
                break;
            }
            case PAST -> {
                bookingList = bookingDBRepository
                        .findByBookerIdAndEndIsBefore(userId, dateTimeNow, sort);
                break;
            }
            case FUTURE -> {
                bookingList = bookingDBRepository
                        .findByBookerIdAndStartIsAfter(userId, dateTimeNow, sort);
                break;
            }
            case WAITING -> {
                bookingList = bookingDBRepository
                        .findByBookerIdAndStatus(userId, Status.WAITING, sort);
                break;
            }
            case REJECTED -> {
                bookingList = bookingDBRepository
                        .findByBookerIdAndStatus(userId, Status.REJECTED, sort);
                break;
            }
        }
        return bookingMapper.listBookingToListBookingDto(bookingList);
    }

    @Override
    public List<BookingDto> getAllByOwner(long userId, State state) {
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> bookingList = new ArrayList<>();
        LocalDateTime dateTimeNow = LocalDateTime.now();
        switch (state) {
            case ALL -> {
                bookingList = bookingDBRepository.findByItemOwnerId(userId, sort);
                break;
            }
            case CURRENT -> {
                bookingList = bookingDBRepository
                        .findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(userId, dateTimeNow,
                                dateTimeNow, sort);
                break;
            }
            case PAST -> {
                bookingList = bookingDBRepository
                        .findByItemOwnerIdAndEndIsBefore(userId, dateTimeNow, sort);
                break;
            }
            case FUTURE -> {
                bookingList = bookingDBRepository
                        .findByItemOwnerIdAndStartIsAfter(userId, dateTimeNow, sort);
                break;
            }
            case WAITING -> {
                bookingList = bookingDBRepository
                        .findByItemOwnerIdAndStatus(userId, Status.WAITING, sort);
                break;
            }
            case REJECTED -> {
                bookingList = bookingDBRepository
                        .findByItemOwnerIdAndStatus(userId, Status.REJECTED, sort);
                break;
            }
        }
        return bookingMapper.listBookingToListBookingDto(bookingList);
    }
}
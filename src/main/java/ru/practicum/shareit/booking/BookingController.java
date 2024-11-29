package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreated;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@Slf4j
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @Validated(Create.class)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public Booking create(@RequestHeader(X_SHARER_USER_ID) long userId, @Valid
    @RequestBody BookingDtoCreated bookingDtoCreated) {
        Booking booking = bookingService.create(userId, bookingDtoCreated);
        BookingController.log.info("Создано новое бронирование с id={}", booking.getId());
        return booking;
    }

    @PatchMapping("/{bookingId}")
    @Validated(Update.class)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public BookingDto approve(@RequestHeader(X_SHARER_USER_ID) long userId,
                              @PathVariable final long bookingId,
                              @RequestParam(defaultValue = "false") final boolean approved) {
        BookingDto booking = bookingService.approve(userId, bookingId, approved);
        BookingController.log.info("Статус бронирования = {}", booking.getStatus());
        return booking;
    }

    @GetMapping
    public List<BookingDto> getAll(@RequestHeader(X_SHARER_USER_ID) long userId,
                                   @RequestParam(defaultValue = "ALL") final State state) {
        List<BookingDto> bookingList = bookingService.getAllByUser(userId, state);
        BookingController.log.info("Пользователь с id={} получил информацию о бронированиях", userId);
        return bookingList;
    }

    @GetMapping("/{bookingId}")
    public BookingDto get(@RequestHeader(X_SHARER_USER_ID) long userId, @PathVariable() final long bookingId) {
        BookingDto booking = bookingService.get(userId, bookingId);
        BookingController.log.info("Пользователь с id={} получил информацию о бронировании", userId);
        return booking;
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestHeader(X_SHARER_USER_ID) long userId,
                                          @RequestParam(defaultValue = "ALL") final State state) {
        List<BookingDto> bookingList = bookingService.getAllByOwner(userId, state);
        if (bookingList.isEmpty()) {
            throw new NotFoundException("Бронь для пользователя с id = " + userId + " не найдена");
        }
        BookingController.log.info("Пользователь с id={} получил информацию о бронированиях", userId);
        return bookingList;
    }
}

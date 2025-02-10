package ru.practicum.shareit.booking;

import java.util.Optional;

public enum BookingState {
    // Все
    ALL,
    // Текущие
    CURRENT,
    // Будущие
    FUTURE,
    // Завершенные
    PAST,
    // Отклоненные
    REJECTED,
    // Ожидающие подтверждения
    WAITING;

    public static Optional<BookingState> from(String stringState) {
        for (BookingState bookingState : values()) {
            if (bookingState.name().equalsIgnoreCase(stringState)) {
                return Optional.of(bookingState);
            }
        }
        return Optional.empty();
    }
}

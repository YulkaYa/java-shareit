package ru.practicum.shareit.booking;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.StorageData;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.Instant;
import java.time.LocalDateTime;

@SuperBuilder(toBuilder = true)
@Data
@Entity
@RequiredArgsConstructor
@Table(name = "bookings", schema = "public")
public class Booking extends StorageData {
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "booker_id")
    private User booker;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "start_date")
    private LocalDateTime start;
    @Column(name = "end_date")
    private LocalDateTime end;

    Booking setStatusByIsApproved(boolean isApproved) {
        if (isApproved) {
            this.setStatus(Status.APPROVED);
        } else {
            this.setStatus(Status.REJECTED);
        }
        return this;
    }
}

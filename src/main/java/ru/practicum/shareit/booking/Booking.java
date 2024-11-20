package ru.practicum.shareit.booking;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.StorageData;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.Instant;

@SuperBuilder(toBuilder = true)
@Data
@Entity
@RequiredArgsConstructor
@Table(name = "bookings", schema = "public")
public class Booking extends StorageData {
    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;
    @ManyToOne
    @JoinColumn(name = "booker_id")
    User booker;
    @Enumerated
    Status status;
    @Column(name = "start_date")
    private Instant startDate = Instant.now();
    @Column(name = "end_date")
    private Instant endDate;
}

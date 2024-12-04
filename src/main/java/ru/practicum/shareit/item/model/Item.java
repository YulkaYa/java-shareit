package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.StorageData;
import ru.practicum.shareit.user.User;

@SuperBuilder(toBuilder = true)
@Data
@Entity
@RequiredArgsConstructor
@Table(name = "items", schema = "public")
public class Item extends StorageData {
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    private String name;
    private String description;
    @Column(name = "is_available", nullable = false)
    private boolean available;
}

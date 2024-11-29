package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.StorageData;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@SuperBuilder(toBuilder = true)
@Data
@Entity
@RequiredArgsConstructor
@Table(name = "comments", schema = "public")
public class Comment extends StorageData {
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    @Column(name = "text", nullable = false)
    private String text;
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime created;
}

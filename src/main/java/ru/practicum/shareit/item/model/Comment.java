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
@Table(name = "comments", schema = "public")
public class Comment extends StorageData {
    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;
    @ManyToOne
    @JoinColumn(name = "author_id")
    User author;
    private String text;
}

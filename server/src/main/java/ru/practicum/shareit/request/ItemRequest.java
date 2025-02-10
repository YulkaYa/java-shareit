package ru.practicum.shareit.request;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.common.StorageData;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder(toBuilder = true)
@Data
@Entity
@RequiredArgsConstructor
@Table(name = "requests", schema = "public")
public class ItemRequest extends StorageData {
    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private User requestor;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "request") //todo проверить, что все ок выгружается
    private List<Item> items = new ArrayList<>();
}

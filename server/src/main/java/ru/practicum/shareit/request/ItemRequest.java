package ru.practicum.shareit.request;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.StorageData;
import ru.practicum.shareit.user.User;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
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
}

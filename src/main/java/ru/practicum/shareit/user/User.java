package ru.practicum.shareit.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.StorageData;

@SuperBuilder(toBuilder = true)
@Data
@Entity
@Table(name = "users", schema = "public")
@RequiredArgsConstructor
public class User extends StorageData {
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false, unique=true)
    private String email;
}

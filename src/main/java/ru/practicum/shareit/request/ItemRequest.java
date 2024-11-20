package ru.practicum.shareit.request;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.StorageData;

@SuperBuilder(toBuilder = true)
@Data
@Entity
@Table(name = "requests", schema = "public")
public class ItemRequest extends StorageData {
}

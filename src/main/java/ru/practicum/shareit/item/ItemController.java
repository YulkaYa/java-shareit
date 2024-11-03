package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @Validated(Create.class)
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid
    @RequestBody ItemDto itemDto) {
        ItemDto itemCreatedDto = itemService.create(userId, itemDto);
        ItemController.log.info("Создан новый товар с id={}", itemDto.getId());
        return itemCreatedDto;
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItemById(@PathVariable(name = "itemId") long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getItemsByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItemsByUserId(userId);
    }

    @PatchMapping("/{itemId}")
    @Validated({Update.class})
    @ResponseStatus(HttpStatus.OK)
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable final Long itemId, @Valid @RequestBody final ItemDto itemDto) {
        ItemController.log.info("Обновляем товар с id={}", itemId);
        return itemService.update(userId, itemId, itemDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public List<ItemDto> searchItemsByText(@RequestHeader("X-Sharer-User-Id") long userId, final String text) {
        return itemService.searchItemsByText(text);
    }
}

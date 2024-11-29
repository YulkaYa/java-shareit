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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoCreatedText;
import ru.practicum.shareit.item.dto.ItemDtoWithoutDates;
import ru.practicum.shareit.item.dto.ItemDtoFull;

import java.util.ArrayList;
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
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @Validated(Create.class)
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDtoWithoutDates create(@RequestHeader(X_SHARER_USER_ID) long userId, @Valid
    @RequestBody ItemDtoWithoutDates itemDtoWithoutDates) {
        ItemDtoWithoutDates itemCreatedDto = itemService.create(userId, itemDtoWithoutDates);
        ItemController.log.info("Создан новый товар с id={}", itemCreatedDto.getId());
        return itemCreatedDto;
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public <T extends ItemDtoWithoutDates> T getItemById(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                         @PathVariable(name = "itemId") long itemId) {
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDtoFull> getItemsByUserId(@RequestHeader(X_SHARER_USER_ID) long userId) {
        return itemService.getItemsByUserId(userId);
    }

    @PatchMapping("/{itemId}")
    @Validated({Update.class})
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoWithoutDates update(@RequestHeader(X_SHARER_USER_ID) long userId,
                                      @PathVariable final long itemId,
                                      @Valid @RequestBody final ItemDtoWithoutDates itemDtoWithoutDates) {
        ItemController.log.info("Обновляем товар с id={}", itemId);
        return itemService.update(userId, itemId, itemDtoWithoutDates);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public List<ItemDtoWithoutDates> searchItemsByText(@RequestHeader(X_SHARER_USER_ID) long userId, final String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemService.searchItemsByText(text);
    }

    @ResponseStatus(HttpStatus.OK)
    @Validated({Create.class})
    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(X_SHARER_USER_ID) long userId, @PathVariable final long itemId, @Valid @RequestBody final CommentDtoCreatedText commentDtoOnlyText) {
        return itemService.createComment(userId, itemId, commentDtoOnlyText);
    }
}

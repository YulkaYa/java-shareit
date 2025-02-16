package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;
import ru.practicum.shareit.item.dto.CommentDtoCreatedText;
import ru.practicum.shareit.item.dto.ItemDtoWithoutDates;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemClient itemClient;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @Validated(Create.class)
    public ResponseEntity<Object> create(@RequestHeader(X_SHARER_USER_ID) long userId, @Valid
    @RequestBody ItemDtoWithoutDates itemDtoWithoutDates) {
        return itemClient.create(userId, itemDtoWithoutDates);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                         @PathVariable(name = "itemId") long itemId) {
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByUserId(@RequestHeader(X_SHARER_USER_ID) long userId) {
        return itemClient.getItemsByUserId(userId);
    }

    @PatchMapping("/{itemId}")
    @Validated({Update.class})
    public ResponseEntity<Object> update(@RequestHeader(X_SHARER_USER_ID) long userId,
                                      @PathVariable final long itemId,
                                      @Valid @RequestBody final ItemDtoWithoutDates itemDtoWithoutDates) {
        return itemClient.update(userId, itemId, itemDtoWithoutDates);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemsByText(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                       @RequestParam(defaultValue = "") final String text) {
        return itemClient.searchItemsByText(userId, text);
    }

    @Validated({Create.class})
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(X_SHARER_USER_ID) long userId, @PathVariable final long itemId, @Valid @RequestBody final CommentDtoCreatedText commentDtoOnlyText) {
        return itemClient.createComment(userId, itemId, commentDtoOnlyText);
    }
}

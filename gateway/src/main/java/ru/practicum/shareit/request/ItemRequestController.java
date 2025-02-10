package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.request.dto.ItemRequestCreatedDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @Validated(Create.class)
    public ResponseEntity<Object> create(@RequestHeader(X_SHARER_USER_ID) long userId,
                                         @Valid @RequestBody ItemRequestCreatedDto itemRequestCreatedDto) {
        return itemRequestClient.create(userId, itemRequestCreatedDto);
    }

    //GET /requests/{requestId} — получить данные об одном конкретном запросе вместе с данными об ответах на него в том же формате, что и в эндпоинте GET /requests. Посмотреть данные об отдельном запросе может любой пользователь.
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestsByRequestId(@PathVariable(name = "requestId") long requestId) {
        return itemRequestClient.getItemsRequestsByRequestId(requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsByUserId(@RequestHeader(X_SHARER_USER_ID) long userId) {
        return itemRequestClient.getItemsRequestsByUserId(userId);
    }


    @GetMapping("/all")
    public ResponseEntity<Object> getRequestsFromOtherUsers(@RequestHeader(X_SHARER_USER_ID) long userId) { // todo проверить проверяется ли в optional значение на positive/negative/zero
        return itemRequestClient.getRequestsFromOtherUsers(userId);
    }

/*//todo нужно ли доделать пагинацию
    //GET /requests/all — получить список запросов, созданных другими пользователями. С помощью этого эндпоинта пользователи смогут просматривать существующие запросы, на которые они могли бы ответить. Запросы сортируются по дате создания от более новых к более старым.
    @GetMapping("/all")
    public List<ItemRequestDtoWithItems> getRequestsFromOtherUsers(@RequestHeader(X_SHARER_USER_ID) long userId,
                          @RequestParam (defaultValue = "0") @PositiveOrZero(message = "Параметр from не должен быть отрицательным") final int from,
                          @RequestParam @Positive(message = "Параметр count должен быть больше нуля") final int size) { // todo проверить проверяется ли в optional значение на positive/negative/zero
        List<ItemRequestDtoWithItems> itemRequestDtoWithItems = itemRequestService.getRequestsFromOtherUsers(userId, from, size);
        return itemRequestDtoWithItems;
    }

*/

}

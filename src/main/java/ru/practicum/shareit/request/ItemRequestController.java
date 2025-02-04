package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.request.dto.ItemRequestBaseDto;
import ru.practicum.shareit.request.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;

import java.util.List;
import java.util.Optional;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@Slf4j
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(path = "/requests")
public class ItemRequestController {
    // POST /requests — добавить новый запрос вещи.
    // Основная часть запроса — текст запроса, в котором пользователь описывает, какая именно вещь ему нужна.
    private final ItemRequestService itemRequestService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @Validated(Create.class)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ItemRequestBaseDto create(@RequestHeader(X_SHARER_USER_ID) long userId,
                                        @Valid @RequestBody ItemRequestCreatedDto itemRequestCreatedDto) {
        ItemRequestBaseDto itemRequestBaseDto = itemRequestService.create(userId, itemRequestCreatedDto);
        ItemRequestController.log.info("Создан новый запрос с id={}", itemRequestBaseDto.getId());
        return itemRequestBaseDto;
    }

    //GET /requests/{requestId} — получить данные об одном конкретном запросе вместе с данными об ответах на него в том же формате, что и в эндпоинте GET /requests. Посмотреть данные об отдельном запросе может любой пользователь.
    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDtoWithItems getRequestsByRequestId(@PathVariable(name = "requestId") long requestId) {
        return itemRequestService.getItemsRequestsByRequestId(requestId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDtoWithItems> getRequestsByUserId(@RequestHeader(X_SHARER_USER_ID) long userId) {
        return itemRequestService.getItemsRequestsByUserId(userId);
    }


    @GetMapping("/all")
    public List<ItemRequestDtoWithItems> getRequestsFromOtherUsers(@RequestHeader(X_SHARER_USER_ID) long userId) { // todo проверить проверяется ли в optional значение на positive/negative/zero
        List<ItemRequestDtoWithItems> itemRequestDtoWithItems = itemRequestService.getRequestsFromOtherUsers(userId);
        return itemRequestDtoWithItems;
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

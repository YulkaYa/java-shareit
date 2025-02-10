package ru.practicum.shareit.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import ru.practicum.shareit.request.dto.ItemRequestBaseDto;
import ru.practicum.shareit.request.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

interface ItemRequestService {
    ItemRequestBaseDto create(long userId, ItemRequestCreatedDto data);

    List<ItemRequestDtoWithItems> getItemsRequestsByUserId(long requestorId);

 /*   List<ItemRequestDtoWithItems> getRequestsFromOtherUsers(long requestorId, int from, int size);// todo надо ли*/

    List<ItemRequestDtoWithItems> getRequestsFromOtherUsers(long userId);

    ItemRequestDtoWithItems getItemsRequestsByRequestId(long requestId);
}
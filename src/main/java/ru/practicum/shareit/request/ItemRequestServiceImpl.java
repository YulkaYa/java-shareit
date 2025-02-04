package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.item.ItemBaseRepository;
import ru.practicum.shareit.item.dal.item.ItemMapper;
import ru.practicum.shareit.request.dal.ItemRequestDBRepository;
import ru.practicum.shareit.request.dal.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestBaseDto;
import ru.practicum.shareit.request.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserBaseRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemBaseRepository itemRepository;
    private final ItemRequestDBRepository itemRequestDBRepository;
    private final UserBaseRepository userRepository;
    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);
    private final ItemRequestMapper itemRequestMapper = Mappers.getMapper(ItemRequestMapper.class);


    @Override
    public ItemRequestBaseDto create(long userId, ItemRequestCreatedDto itemRequestCreatedDto) {
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + "не найден"));
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestCreatedDto, requestor);
        return itemRequestMapper.toItemRequestBaseDto(itemRequestDBRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDtoWithItems> getItemsRequestsByUserId(long requestorId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        List<ItemRequest> itemRequests = itemRequestDBRepository.findByRequestorId(requestorId, sort);

        List<ItemRequestDtoWithItems> itemRequestDtoWithItems = itemRequestMapper.toListItemRequestDtoWithItems(itemRequests);
        return itemRequestDtoWithItems;
    }

/*     todo надо ли @Override
    public List<ItemRequestDtoWithItems> getRequestsFromOtherUsers(long requestorId, int from, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
            PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size, sort);
        List<ItemRequest> itemRequests = itemRequestDBRepository.findAllByRequestorIdNot(requestorId, page); //todo https://practicum.yandex.ru/trainer/java-developer/lesson/6fc4672d-2f83-47c9-a2bd-2e0babb444db/?searchedText=PageRequest , https://github.com/praktikum-java/module-4-later-spring-only/blob/5_spring_data_repositories/src/main/java/ru/practicum/note/ItemNoteServiceImpl.java
        List<ItemRequestDtoWithItems> itemRequestDtoWithItems = itemRequestMapper.toListItemRequestDtoWithItems(itemRequests);
        return itemRequestDtoWithItems;
    }*/

    @Override
    public List<ItemRequestDtoWithItems> getRequestsFromOtherUsers(long requestorId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        List<ItemRequest> itemRequests = itemRequestDBRepository.findAllByRequestorIdNot(requestorId, sort); //todo https://practicum.yandex.ru/trainer/java-developer/lesson/6fc4672d-2f83-47c9-a2bd-2e0babb444db/?searchedText=PageRequest , https://github.com/praktikum-java/module-4-later-spring-only/blob/5_spring_data_repositories/src/main/java/ru/practicum/note/ItemNoteServiceImpl.java
        List<ItemRequestDtoWithItems> itemRequestDtoWithItems = itemRequestMapper.toListItemRequestDtoWithItems(itemRequests);
        return itemRequestDtoWithItems;
    }

    @Override
    public ItemRequestDtoWithItems getItemsRequestsByRequestId(long requestId) {
        ItemRequest itemRequest = itemRequestDBRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id = " + requestId + "не найден"));
        ItemRequestDtoWithItems itemRequestDtoWithItems = itemRequestMapper.toItemRequestDtoWithItems(itemRequest);
        return itemRequestDtoWithItems;
    }
}

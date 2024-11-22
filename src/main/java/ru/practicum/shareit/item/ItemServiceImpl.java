package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.item.ItemBaseRepository;
import ru.practicum.shareit.item.dal.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserBaseRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemServiceImpl implements ItemService {
    private final ItemBaseRepository itemRepository;
    private final UserBaseRepository userRepository;
    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + "не найден"));
        itemDto.setOwnerId(userId);
        Item item = itemMapper.toItem(itemDto, owner);
        return itemMapper.itemToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return itemMapper.itemToItemDto((itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Товар с id=" + itemId + " не найден"))));
    }

    @Override
    public List<ItemDto> getItemsByUserId(long userId) {
        return itemMapper.listItemToListItemDto(itemRepository.findByOwnerId(userId));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {

        Item itemToUpdate = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Товар с id=" + itemId + " не найден"));
        if (userId != itemToUpdate.getOwner().getId()) {
            throw new NotFoundException("Id владельца и id пользователя в запросе не совпадают");
        }
        itemToUpdate = itemMapper.updateFromDto(itemDto, itemToUpdate);
        return itemMapper.itemToItemDto(itemRepository.update(itemToUpdate));
    }

    @Override
    public List<ItemDto> searchItemsByText(String text) {
        return itemMapper.listItemToListItemDto(itemRepository.findByDescriptionOrName(text));
    }
}
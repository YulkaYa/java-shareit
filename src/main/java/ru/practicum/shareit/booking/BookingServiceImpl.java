package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dal.BookingMapper;
import ru.practicum.shareit.item.dal.item.ItemBaseRepository;
import ru.practicum.shareit.user.dal.UserBaseRepository;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookingServiceImpl implements BookingService {
    private final ItemBaseRepository itemRepository;
    private final UserBaseRepository userRepository;
    private final BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);
}
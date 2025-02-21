package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dal.UserDBRepository;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {

    private final UserServiceImpl userService;
    private final UserDBRepository userDBRepository;

    private final UserDto userDto = UserDto.builder()
            .name("name")
            .email("email@mail.com")
            .build();
    private UserDto savedUserDto;

    @BeforeEach
    void setUp() {
        savedUserDto = userService.create(userDto);
    }

    @AfterEach
    void tearDown() {
        userDBRepository.deleteAll();
    }

    @Test
    void createAndGetTest() {
        UserDto userDtoAfterSave = userService.get(savedUserDto.getId());

        assertEquals(savedUserDto.getId(), userDtoAfterSave.getId());
        assertEquals(userDto.getEmail(), userDtoAfterSave.getEmail());
        assertEquals(userDto.getName(), userDtoAfterSave.getName());
    }

    @Test
    void updateTest() {
        UserDto updateUserDto = userDto.toBuilder()
                .id(3L)
                .name("updateduser")
                //.email("update@email.com")
                .build();

        userService.update(savedUserDto.getId(), updateUserDto);
        UserDto afterUpdateUserDto = userService.get(savedUserDto.getId());

        assertEquals(1, userDBRepository.count());
        assertEquals(savedUserDto.getId(), afterUpdateUserDto.getId());
        assertEquals(savedUserDto.getEmail(), afterUpdateUserDto.getEmail());
        assertEquals(updateUserDto.getName(), afterUpdateUserDto.getName());
    }

    @Test
    void deleteTest() {
        assertNotNull(userDBRepository.findById(savedUserDto.getId()));

        userService.delete(savedUserDto.getId());

        assertEquals(0, userDBRepository.count());
    }
}
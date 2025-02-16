package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final Long userId = 1L;

    private final UserDto userDto = UserDto.builder()
            .id(userId)
            .name("userName")
            .email("email@email.ru")
            .build();

    private final UserDto userDtoUpdated = UserDto.builder()
            .id(userId)
            .name("userName2")
            .email("email2@email.ru")
            .build();

    @SneakyThrows
    @Test
    void createTest() {

        when(userService.create(userDto)).thenReturn(userDto);

        String result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService, times(1)).create(any(UserDto.class));
        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }


    @SneakyThrows
    @Test
    void updateTest() {

        when(userService.update(userId, userDto)).thenReturn(userDtoUpdated);

        String result = mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService, times(1)).update(any(Long.class), any(UserDto.class));
        assertEquals(objectMapper.writeValueAsString(userDtoUpdated), result);
    }

    @SneakyThrows
    @Test
    void getTest() {

        when(userService.get(userId)).thenReturn(userDto);

        String result = mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService, times(1)).get(any(Long.class));
        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void deleteTest() {

        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).delete(any(Long.class));
    }
}
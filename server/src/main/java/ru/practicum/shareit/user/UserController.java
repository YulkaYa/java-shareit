package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;
import ru.practicum.shareit.user.dto.UserDto;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@Slf4j
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService userService;

    @PostMapping
    @Validated(Create.class)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody final UserDto userDto) {
        UserDto userCreatedDto = userService.create(userDto);
        UserController.log.info("Создан новый пользователь с id={}", userCreatedDto.getId());
        return userCreatedDto;
    }

    @PatchMapping("/{userId}")
    @Validated(Update.class)
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@PathVariable final long userId, @Valid @RequestBody final UserDto userDto) {
        UserController.log.info("Обновляем пользователя с id={}", userId);
        return userService.update(userId, userDto);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto get(@PathVariable final long userId) {
        UserController.log.info("Получаем пользователя с id={}", userId);
        return this.userService.get(userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final long userId) {
        UserController.log.info("Удаляем пользователя с id={}", userId);
        this.userService.delete(userId);
    }

}

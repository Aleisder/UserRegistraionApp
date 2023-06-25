package com.tsarenko.demo.controller;

import com.tsarenko.demo.model.User;
import com.tsarenko.demo.model.UserDTO;
import com.tsarenko.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@Tag(
        name = "Пользователи",
        description = "Контроллер для работы с профилем пользователя и его аватаркой"
)
public final class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/profile")
    @Operation(
            summary = "Получение профиля пользователя",
            description = "Сервер возаращает http-код 200 и json с профилем пользователя, если получение данных прошло успешно, или код 404, если пользователь с указанным id не найден"
    )
    public UserDTO getUser(@RequestParam long id) {
        return service.getUser(id);
    }

    @PostMapping("/profile")
    @Operation(
            summary = "Cоздание/изменение профиля пользователя",
            description = "Если id пользователя заполнен, то выполняется обновление существующего профиля, если id осутствует - создаётся новый профиль. Возвращаемое значение: http-код 200 и id пользователя или код 404, если пользователь с указанным id отсутствует."
    )
    public Long createOrUpdateUser(@Valid @RequestBody User user) {
        return service.createOrUpdateUser(user);
    }

    @DeleteMapping("/profile")
    @Operation(
            summary = "Удаление профиля пользователя",
            description = ""
    )
    public void deleteUser(@RequestParam long id) {
        service.deleteUser(id);
    }

    @GetMapping(
            value = "/avatar",
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_JPEG_VALUE}
    )
    @Operation(summary = "Получить аватарку пользователя")
    public @ResponseBody byte[] getUserAvatar(@RequestParam long id) {
        return service.getUserAvatar(id);
    }

    @PostMapping(
            value = "/avatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Загрузить аватарку пользователя")
    public void uploadAvatar(@RequestParam long id, @RequestParam MultipartFile file) {
        service.uploadAvatar(id, file);
    }

    @DeleteMapping("/avatar")
    @Operation(summary = "Удалить аватарку пользователя")
    public Long deleteAvatar(@RequestParam long id) {
        return service.deleteAvatar(id);
    }

    @PostMapping(
            value = "/fullprofile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Cохранение профиля пользователя вместе с аватаркой")
    public Long saveFullUser(@RequestBody User user, @RequestBody MultipartFile file) {
        return service.saveFullUser(user, file);
    }

}

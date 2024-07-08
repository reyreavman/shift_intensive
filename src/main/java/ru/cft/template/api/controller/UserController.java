package ru.cft.template.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.template.api.dto.UserDTO;
import ru.cft.template.api.payload.user.PatchUserPayload;
import ru.cft.template.api.payload.user.UserPayload;
import ru.cft.template.common.Paths;
import ru.cft.template.core.service.user.UserService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(Paths.USERS_PATH)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDTO createUser(@RequestBody UserPayload userPayload) {
        return userService.createUser(userPayload);
    }

    @GetMapping
    public UserDTO getUserInfo(@RequestParam(required = false) Long userId,
                               @RequestParam(required = false) String phoneNumber) {
        if (Objects.nonNull(userId)) return userService.findUserById(userId);
        return userService.findUserByPhoneNumber(phoneNumber);
    }

    @GetMapping
    public List<UserDTO> getUsersInfo() {
        return this.userService.findAllUsers();
    }

    /*
    После настройки секьюрити параметр id отсюда уйдет
    * */
    @PatchMapping
    public UserDTO patchUser(@RequestParam long id, @RequestBody PatchUserPayload userPayload) {
        return userService.patchUser(id, userPayload);
    }
}

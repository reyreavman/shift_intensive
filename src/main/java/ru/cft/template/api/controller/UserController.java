package ru.cft.template.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.template.api.dto.user.CreateUserDTO;
import ru.cft.template.api.dto.user.PatchUserDTO;
import ru.cft.template.api.dto.user.UserDTO;
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
    public UserDTO createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        return userService.createUser(createUserDTO);
    }

    @GetMapping
    public UserDTO getUserInfo(@RequestParam(required = false) Long userId,
                               @RequestParam(required = false) String phoneNumber) {
        if (Objects.nonNull(userId)) return userService.findUserById(userId);
        return userService.findUserByPhoneNumber(phoneNumber);
    }

    @GetMapping("all")
    public List<UserDTO> getAllUsers() {
        return this.userService.findAllUsers();
    }

    /*
    После настройки секьюрити параметр id отсюда уйдет
    * */
    @PatchMapping
    public UserDTO patchUser(@RequestParam long id,
                             @Valid @RequestBody PatchUserDTO userPayload) {
        return userService.patchUser(id, userPayload);
    }
}

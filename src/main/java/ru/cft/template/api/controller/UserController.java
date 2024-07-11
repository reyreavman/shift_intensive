package ru.cft.template.api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.template.api.dto.user.CreateUserDTO;
import ru.cft.template.api.dto.user.PatchUserDTO;
import ru.cft.template.api.dto.user.UserDTO;
import ru.cft.template.core.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/kartoshka-api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        return userService.createUser(createUserDTO);
    }

    @GetMapping("{userId:\\d+}")
    public UserDTO getUserInfo(@Min(1L) @PathVariable(name = "userId") long userId) {
        return userService.findUserById(userId);
    }

    @GetMapping("all")
    public List<UserDTO> getAllUsers() {
        return this.userService.findAllUsers();
    }

    /*
    После настройки секьюрити параметр id отсюда уйдет
    * */
    @PatchMapping
    public UserDTO patchUser(@Min(1L) @RequestParam(name = "id") long id,
                             @Valid @RequestBody PatchUserDTO userPayload) {
        return userService.patchUser(id, userPayload);
    }
}

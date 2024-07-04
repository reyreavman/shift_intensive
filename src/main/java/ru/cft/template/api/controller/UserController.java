package ru.cft.template.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import ru.cft.template.common.Paths;
import ru.cft.template.api.dto.UserDTO;
import ru.cft.template.api.payload.NewUserPayload;
import ru.cft.template.api.payload.PatchUserPayload;
import ru.cft.template.core.exception.EmptyResultDataAccessException;
import ru.cft.template.core.exception.MultipleParamsException;
import ru.cft.template.core.service.user.UserService;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping(Paths.USERS_PATH)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody NewUserPayload userPayload, UriComponentsBuilder uriComponentsBuilder) {
        UserDTO userDTO = this.userService.createUser(userPayload);
        return ResponseEntity
                .created(uriComponentsBuilder
                        .replacePath(Paths.USERS_PATH.concat("/{userId}"))
                        .build(Map.of("userId", userDTO.id())))
                .body(userDTO);
    }

    @PatchMapping
    public ResponseEntity<UserDTO> patchUser(@RequestBody PatchUserPayload userPayload) {
        UserDTO userDTO = this.userService.patchUser(userPayload);
        return ResponseEntity.ok().body(userDTO);
    }

    @GetMapping
    public ResponseEntity<?> getUsersInfo(@RequestParam Map<String, String> params) {
        Supplier<Stream<Map.Entry<String, String>>> supplier = () -> params.entrySet().stream().filter(entry -> Objects.nonNull(entry.getValue()));
        if (supplier.get().count() > 1) throw new MultipleParamsException(supplier.get().map(Map.Entry::getKey).toList());

        try {
            if (Objects.nonNull(params.get("id"))) return ResponseEntity.ok().body(this.userService.findUserById(Long.parseLong(params.get("id"))));
            else if (Objects.nonNull(params.get("email"))) return ResponseEntity.ok().body(this.userService.findUserByEmail(params.get("email")));
            else if (Objects.nonNull(params.get("phoneNumber"))) return ResponseEntity.ok().body(this.userService.findUserByPhoneNumber(params.get("phoneNumber")));
        } catch (EmptyResultDataAccessException ex) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(this.userService.findAllUsers());
    }
}

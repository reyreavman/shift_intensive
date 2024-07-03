package ru.cft.template.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import ru.cft.template.Paths;
import ru.cft.template.api.dto.UserDTO;
import ru.cft.template.api.payload.NewUserPayload;
import ru.cft.template.api.payload.PatchUserPayload;
import ru.cft.template.core.exception.EmptyResultDataAccessException;
import ru.cft.template.core.exception.MultipleParametersException;
import ru.cft.template.core.service.UserService;

import java.util.Objects;
import java.util.stream.Stream;

@RestController
@RequestMapping(Paths.USERS_PATH)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody NewUserPayload userPayload, UriComponentsBuilder uriComponentsBuilder) {
        UserDTO userDTO = this.userService.createUser(
                userPayload.firstName(),
                userPayload.middleName(),
                userPayload.lastName(),
                userPayload.phoneNumber(),
                userPayload.email(),
                userPayload.birthdate(),
                userPayload.password());
        return ResponseEntity.created(uriComponentsBuilder.build(Paths.USERS_PATH.concat("/{userId}"))).body(userDTO);
    }

    @PatchMapping
    public ResponseEntity<UserDTO> patchUser(@RequestBody PatchUserPayload userPayload, UriComponentsBuilder uriComponentsBuilder) {
        UserDTO userDTO = this.userService.patchUser(
                userPayload.firstName(),
                userPayload.middleName(),
                userPayload.lastName(),
                userPayload.birthdate()
        );
        return ResponseEntity.ok().body(userDTO);
    }

    @GetMapping
    public ResponseEntity<?> getUsersInfo(@RequestParam(value = "id", required = false) String id,
                                          @RequestParam(value = "email", required = false) String email,
                                          @RequestParam(value = "phoneNumber", required = false) String phoneNumber) {
        if (Stream.of(id, email, phoneNumber).filter(Objects::nonNull).count() > 1) {
            throw new MultipleParametersException("More than one parameter is specified. Please provide only one parameter: ID, phone number or email.");
        }

        try {
            if (Objects.nonNull(id))
                return ResponseEntity.ok().body(this.userService.findUserById(Integer.parseInt(id)));
            else if (Objects.nonNull(email)) return ResponseEntity.ok().body(this.userService.findUserByEmail(email));
            else if (Objects.nonNull(phoneNumber))
                return ResponseEntity.ok().body(this.userService.findUserByPhoneNumber(phoneNumber));
        } catch (EmptyResultDataAccessException ex) {
            return ResponseEntity.notFound().build();
        }
        //TODO: Убрать трай кетчи, буду обрабатывать пробросы в глобал хендлере
        return ResponseEntity.ok().body(this.userService.findAllUsers());
    }
}

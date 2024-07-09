package ru.cft.template.core.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.cft.template.api.dto.user.CreateUserDTO;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class UserValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    CreateUserDTO createUserDTOFullyGivenInfo = CreateUserDTO.builder()
            .firstName("Радмир")
            .middleName("Рустамович")
            .lastName("Хурум")
            .phoneNumber("79999999999")
            .email("rKhurum@example.com")
            .birthdate(LocalDate.of(2004, 6, 1))
            .password("Example1!")
            .build();
    CreateUserDTO createUserDTOWithoutAnyValidatingInfo = CreateUserDTO.builder()
            .middleName("Рустамович")
            .build();
    CreateUserDTO createUserDTOWithWrongInfo = CreateUserDTO.builder()
            .firstName("адмир")
            .middleName("устамович")
            .lastName("урум")
            .phoneNumber("9999999999")
            .email("rKhurum!example.com")
            .birthdate(LocalDate.of(2004, 6, 1))
            .password("Exae!")
            .build();

    @Test
    void newUserPayload_withFullyGivenInfo_ValidationSuccessful() {
        Set<ConstraintViolation<CreateUserDTO>> violations = validator.validate(createUserDTOFullyGivenInfo);

        assertTrue(violations.isEmpty());
    }

    @Test
    void newUserPayload_withOnlyMiddleName_ValidationFailed() {
        Set<Path> expectedViolationsPaths = Set.of(
                PathImpl.createPathFromString("firstName"),
                PathImpl.createPathFromString("lastName"),
                PathImpl.createPathFromString("recipientPhoneNumber"),
                PathImpl.createPathFromString("email"),
                PathImpl.createPathFromString("birthdate"),
                PathImpl.createPathFromString("password")
        );

        Set<Path> violationsPaths = validator.validate(createUserDTOWithoutAnyValidatingInfo).stream().map(ConstraintViolation::getPropertyPath).collect(Collectors.toUnmodifiableSet());

        assertEquals(expectedViolationsPaths, violationsPaths);
    }

    @Test
    void newUserPayload_withAllButNotValidFields_ValidationFailed() {
        Set<Path> expectedViolationsPaths = Set.of(
                PathImpl.createPathFromString("firstName"),
                PathImpl.createPathFromString("middleName"),
                PathImpl.createPathFromString("lastName"),
                PathImpl.createPathFromString("recipientPhoneNumber"),
                PathImpl.createPathFromString("email"),
                PathImpl.createPathFromString("birthdate"),
                PathImpl.createPathFromString("password")
        );

        Set<Path> violationsPaths = validator.validate(createUserDTOWithWrongInfo).stream().map(ConstraintViolation::getPropertyPath).collect(Collectors.toUnmodifiableSet());

        assertEquals(expectedViolationsPaths, violationsPaths);
    }
}

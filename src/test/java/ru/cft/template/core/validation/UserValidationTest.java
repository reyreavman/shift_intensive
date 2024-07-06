package ru.cft.template.core.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.cft.template.api.payload.NewUserPayload;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class UserValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    NewUserPayload userPayloadFullyGivenInfo = NewUserPayload.builder()
            .firstName("Радмир")
            .middleName("Рустамович")
            .lastName("Хурум")
            .phoneNumber("79999999999")
            .email("rKhurum@example.com")
            .birthdate("2004-01-01")
            .password("Example1!")
            .build();
    NewUserPayload userPayloadWithoutAnyValidatingInfo = NewUserPayload.builder()
            .middleName("Рустамович")
            .build();
    NewUserPayload userPayloadWithWrongInfo = NewUserPayload.builder()
            .firstName("адмир")
            .middleName("устамович")
            .lastName("урум")
            .phoneNumber("9999999999")
            .email("rKhurum!example.com")
            .birthdate("20-01-01")
            .password("Exae!")
            .build();

    @Test
    void newUserPayload_withFullyGivenInfo_ValidationSuccessful() {
        Set<ConstraintViolation<NewUserPayload>> violations = validator.validate(userPayloadFullyGivenInfo);

        assertTrue(violations.isEmpty());
    }

    @Test
    void newUserPayload_withOnlyMiddleName_ValidationFailed() {
        Set<Path> expectedViolationsPaths = Set.of(
                PathImpl.createPathFromString("firstName"),
                PathImpl.createPathFromString("lastName"),
                PathImpl.createPathFromString("phoneNumber"),
                PathImpl.createPathFromString("email"),
                PathImpl.createPathFromString("birthdate"),
                PathImpl.createPathFromString("password")
        );

        Set<Path> violationsPaths = validator.validate(userPayloadWithoutAnyValidatingInfo).stream().map(ConstraintViolation::getPropertyPath).collect(Collectors.toUnmodifiableSet());

        assertEquals(expectedViolationsPaths, violationsPaths);
    }

    @Test
    void newUserPayload_withAllButNotValidFields_ValidationFailed() {
        Set<Path> expectedViolationsPaths = Set.of(
                PathImpl.createPathFromString("firstName"),
                PathImpl.createPathFromString("middleName"),
                PathImpl.createPathFromString("lastName"),
                PathImpl.createPathFromString("phoneNumber"),
                PathImpl.createPathFromString("email"),
                PathImpl.createPathFromString("birthdate"),
                PathImpl.createPathFromString("password")
        );

        Set<Path> violationsPaths = validator.validate(userPayloadWithWrongInfo).stream().map(ConstraintViolation::getPropertyPath).collect(Collectors.toUnmodifiableSet());

        assertEquals(expectedViolationsPaths, violationsPaths);
    }
}

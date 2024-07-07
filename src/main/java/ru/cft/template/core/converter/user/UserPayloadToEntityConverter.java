package ru.cft.template.core.converter.user;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.cft.template.api.payload.user.NewUserPayload;
import ru.cft.template.core.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Component
public class UserPayloadToEntityConverter implements Converter<NewUserPayload, User> {
    @Override
    public User convert(NewUserPayload userPayload) {
        return User.builder()
                .id(null)
                .firstName(userPayload.firstName())
                .middleName(userPayload.middleName())
                .lastName(userPayload.lastName())
                .phoneNumber(userPayload.phoneNumber())
                .email(userPayload.email())
                .birthdate(LocalDate.parse(userPayload.birthdate(), DateTimeFormatter.ISO_DATE))
                .passwordHash(userPayload.password())
                .registrationDateTime(LocalDateTime.now())
                .lastUpdateDateTime(null)
                .build();
    }
}

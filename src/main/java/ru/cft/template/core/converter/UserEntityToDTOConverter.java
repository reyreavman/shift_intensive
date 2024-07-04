package ru.cft.template.core.converter;

import org.springframework.core.convert.converter.Converter;
import ru.cft.template.api.dto.UserDTO;
import ru.cft.template.core.entity.User;

public class UserEntityToDTOConverter implements Converter<User, UserDTO> {
    @Override
    public UserDTO convert(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .birthdate(user.getBirthdate())
                .build();
    }
}

package ru.cft.template.api.dto.session;

public record SessionInfoDTO(
        Long id,
        Integer expirationTime
) {
}

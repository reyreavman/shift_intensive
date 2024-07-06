package ru.cft.template.api.dto;

public record SessionInfoDTO(
        Long id,
        Integer expirationTime
) {
}

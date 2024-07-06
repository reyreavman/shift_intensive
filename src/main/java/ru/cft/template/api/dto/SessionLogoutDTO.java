package ru.cft.template.api.dto;

import ru.cft.template.common.SessionsStatus;

public record SessionLogoutDTO(
        Long id,
        SessionsStatus status
) {
}

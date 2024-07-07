package ru.cft.template.api.dto.session;

import ru.cft.template.common.SessionsStatus;

public record SessionLogoutDTO(
        Long id,
        SessionsStatus status
) {
}

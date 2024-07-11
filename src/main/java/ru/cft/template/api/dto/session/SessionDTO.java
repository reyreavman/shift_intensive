package ru.cft.template.api.dto.session;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SessionDTO(
        String token,
        LocalDateTime expirationTime,
        boolean active
) {
}

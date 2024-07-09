package ru.cft.template.api.dto.session;

import lombok.Builder;

@Builder
public record CreatedSessionDTO(
        Long id,
        Integer accessTokenExpirationTime,
        String accessToken,
        Integer refreshTokenExpirationTime,
        String refreshToken
) {
}

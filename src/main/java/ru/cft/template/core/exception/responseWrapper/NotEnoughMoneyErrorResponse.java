package ru.cft.template.core.exception.responseWrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class NotEnoughMoneyErrorResponse {
    private final HttpStatus status;
    private final String message;
    private final Long senderId;
    private final Long balance;
    private final Long transferAmount;
    private final LocalDateTime dateTime = LocalDateTime.now();
}

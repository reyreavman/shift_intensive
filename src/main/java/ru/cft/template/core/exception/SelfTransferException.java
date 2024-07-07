package ru.cft.template.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SelfTransferException extends RuntimeException {
    private final Long senderId;
    private final Long recipientId;
}

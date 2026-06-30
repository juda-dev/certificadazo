package dev.juda.auth_service.messaging.dto.out;

import dev.juda.auth_service.util.enums.ReplyStatus;

public record Reply<T>(
    ReplyStatus status,
    String message,
    T body
) {
}

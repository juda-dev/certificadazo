package dev.juda.auth_service.model.dto.messaging;

import dev.juda.auth_service.model.enums.ReplyStatus;

public record Reply<T>(
    ReplyStatus status,
    String message,
    T body
) {
}

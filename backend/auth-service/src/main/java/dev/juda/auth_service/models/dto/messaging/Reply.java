package dev.juda.auth_service.models.dto.messaging;

import dev.juda.auth_service.models.enums.ReplyStatus;

public record Reply<T>(
    ReplyStatus status,
    String message,
    T body
) {
}

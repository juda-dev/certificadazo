package dev.juda.users_service.models.dto.messaging;

import dev.juda.users_service.models.enums.ReplyStatus;

public record Reply<T>(
    ReplyStatus status,
    String message,
    T body
) {
} 
package dev.juda.users_service.messaging.dto.in;

import dev.juda.users_service.util.enums.ReplyStatus;

public record Reply<T>(
    ReplyStatus status,
    String message,
    T body
) {
} 
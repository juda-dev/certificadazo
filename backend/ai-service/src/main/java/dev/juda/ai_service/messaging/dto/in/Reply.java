package dev.juda.ai_service.messaging.dto.in;

import dev.juda.ai_service.util.enums.ReplyStatus;

public record Reply<T>(
        ReplyStatus status,
        String message,
        T body) {
}

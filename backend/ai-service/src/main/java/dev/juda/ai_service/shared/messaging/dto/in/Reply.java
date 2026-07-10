package dev.juda.ai_service.shared.messaging.dto.in;

import dev.juda.ai_service.shared.util.enums.ReplyStatus;

public record Reply<T>(
        ReplyStatus status,
        String message,
        T body) {
}

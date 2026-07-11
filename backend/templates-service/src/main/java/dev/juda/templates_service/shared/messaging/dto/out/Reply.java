package dev.juda.templates_service.shared.messaging.dto.out;

import dev.juda.templates_service.shared.util.enums.ReplyStatus;

public record Reply<T>(
        ReplyStatus status,
        String message,
        T body) {
}
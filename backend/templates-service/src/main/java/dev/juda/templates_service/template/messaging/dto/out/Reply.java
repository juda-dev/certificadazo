package dev.juda.templates_service.template.messaging.dto.out;

import dev.juda.templates_service.template.util.enums.ReplyStatus;

public record Reply<T>(
        ReplyStatus status,
        String message,
        T body) {
}
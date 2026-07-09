package dev.juda.ai_service.messaging.dto.out;

import java.util.UUID;

import dev.juda.ai_service.util.enums.CommandType;

public record Command<T>(CommandType type,
        UUID id,
        T body) {
}

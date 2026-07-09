package dev.juda.templates_service.template.messaging.dto.in;

import java.util.UUID;

import dev.juda.templates_service.template.util.enums.CommandType;

public record Command<T>(CommandType type,
        UUID id,
        T body) {
}

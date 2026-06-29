package dev.juda.auth_service.model.dto.messaging;

import java.util.UUID;

import dev.juda.auth_service.model.enums.CommandType;

public record Command<T>(CommandType type,
    UUID id,
    T body
) {}

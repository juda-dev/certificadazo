package dev.juda.users_service.models.dto.messaging;

import java.util.UUID;

import dev.juda.users_service.models.enums.CommandType;

public record Command<T>(CommandType type,
    UUID id,
    T body
) {}
    

package dev.juda.auth_service.messaging.dto;

import java.util.UUID;

import dev.juda.auth_service.util.enums.CommandType;

public record Command<T>(CommandType type,
    UUID id,
    T body
) {}

package dev.juda.templates_service.shared.messaging.dto.in;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record TemplateAiResponse(
                @NotBlank String name,
                @NotBlank String desing,
                @NotNull UUID departmentId,
                @NotBlank String previewSrc,
                @NotEmpty Set<@Valid String> fields,
                @NotEmpty Map<String, @Valid Object> imagesSrc) {

}

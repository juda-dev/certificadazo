package dev.juda.templates_service.template.presentation.dto.request;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record TemplateRequest(
                @NotBlank String name,
                @NotBlank String desing,
                @NotNull UUID departmentId,
                @NotBlank String previewSrc,
                @NotEmpty List<@Valid String> fields,
                @NotEmpty Map<String, @Valid Object> imagesSrc) {

}

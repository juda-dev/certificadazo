package dev.juda.ai_service.shared.util.enums;

import java.util.Set;

public enum SupportedFileType {
    IMAGE_PDF(Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "application/pdf")),
    CSV(Set.of(
            "text/csv",
            "text/plain",
            "application/csv"));

    private final Set<String> allowedTypes;

    private SupportedFileType(Set<String> allowedTypes) {
        this.allowedTypes = allowedTypes;
    }

    public Set<String> getAllowedTypes() {
        return allowedTypes;
    }

}

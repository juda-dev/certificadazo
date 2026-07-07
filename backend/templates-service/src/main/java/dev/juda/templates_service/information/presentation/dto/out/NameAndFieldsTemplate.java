package dev.juda.templates_service.information.presentation.dto.out;

import java.util.List;

public record NameAndFieldsTemplate(
        String name,
        List<String> fields) {

}

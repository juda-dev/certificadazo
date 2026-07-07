package dev.juda.templates_service.information.presentation.dto.out;

import java.util.Set;

public record NameAndFieldsTemplate(
                String name,
                Set<String> fields) {

}

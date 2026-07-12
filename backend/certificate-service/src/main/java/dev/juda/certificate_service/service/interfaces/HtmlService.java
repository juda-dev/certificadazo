package dev.juda.certificate_service.service.interfaces;

import java.util.Map;

public interface HtmlService {
    byte[] generatePdf(String desing, Map<String, Object> variables);
}

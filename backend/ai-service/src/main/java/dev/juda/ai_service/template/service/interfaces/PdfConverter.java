package dev.juda.ai_service.template.service.interfaces;

public interface PdfConverter {
    byte[] pdfToImage(byte[] pdfBytes);
}

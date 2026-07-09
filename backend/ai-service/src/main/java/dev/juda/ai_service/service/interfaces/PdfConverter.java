package dev.juda.ai_service.service.interfaces;

public interface PdfConverter {
    byte[] pdfToImage(byte[] pdfBytes);
}

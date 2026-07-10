package dev.juda.ai_service.shared.service.implementation;

import java.io.IOException;
import java.util.Set;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.juda.ai_service.shared.service.interfaces.FileValidator;
import dev.juda.ai_service.template.presentation.exception.InvalidFileTypeException;

@Service
public class FileValidatorImpl implements FileValidator {

    private static final Set<String> ALLOWED_IMAGE_PDF_MIME_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "application/pdf");

    @Override
    public String validateIsImageOrPdf(MultipartFile file) {

        Tika tika = new Tika();

        try {
            String detectedType = tika.detect(file.getInputStream());

            if (!ALLOWED_IMAGE_PDF_MIME_TYPES.contains(detectedType)) {
                throw new InvalidFileTypeException();
            }

            return detectedType;
        } catch (IOException e) {
            throw new InvalidFileTypeException();
        }
    }

}

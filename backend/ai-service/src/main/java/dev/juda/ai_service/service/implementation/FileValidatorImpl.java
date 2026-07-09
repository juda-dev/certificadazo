package dev.juda.ai_service.service.implementation;

import java.io.IOException;
import java.util.Set;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.juda.ai_service.presentation.exception.InvalidFileTypeException;
import dev.juda.ai_service.service.interfaces.FileValidator;

@Service
public class FileValidatorImpl implements FileValidator {

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "application/pdf");

    @Override
    public String validate(MultipartFile file) {

        Tika tika = new Tika();

        try {
            String detectedType = tika.detect(file.getInputStream());

            if (!ALLOWED_TYPES.contains(detectedType)) {
                throw new InvalidFileTypeException();
            }

            return detectedType;
        } catch (IOException e) {
            throw new InvalidFileTypeException();
        }
    }

}

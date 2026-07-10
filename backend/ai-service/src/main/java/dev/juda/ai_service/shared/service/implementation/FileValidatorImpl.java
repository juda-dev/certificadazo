package dev.juda.ai_service.shared.service.implementation;

import java.io.IOException;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.juda.ai_service.shared.service.interfaces.FileValidator;
import dev.juda.ai_service.shared.util.enums.SupportedFileType;
import dev.juda.ai_service.template.presentation.exception.InvalidFileTypeException;

@Service
public class FileValidatorImpl implements FileValidator {

    @Override
    public String validateIsImageOrPdf(MultipartFile file, SupportedFileType expectedType) {

        Tika tika = new Tika();

        try {
            String detectedType = tika.detect(file.getInputStream());

            switch (expectedType) {
                case SupportedFileType.IMAGE_PDF -> {
                    if (!SupportedFileType.IMAGE_PDF.getAllowedTypes().contains(detectedType)) {
                        throw new InvalidFileTypeException();
                    }
                    return detectedType;
                }

                case SupportedFileType.CSV -> {
                    if (!SupportedFileType.CSV.getAllowedTypes().contains(detectedType)) {
                        throw new InvalidFileTypeException();
                    }
                    return detectedType;
                }

                default -> {
                    throw new InvalidFileTypeException();
                }
            }
        } catch (IOException e) {
            throw new InvalidFileTypeException();
        }
    }

}

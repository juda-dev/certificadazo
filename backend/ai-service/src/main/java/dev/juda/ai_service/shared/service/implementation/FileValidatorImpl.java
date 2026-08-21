package dev.juda.ai_service.shared.service.implementation;

import dev.juda.ai_service.shared.service.interfaces.FileValidator;
import dev.juda.ai_service.shared.util.enums.SupportedFileType;
import dev.juda.ai_service.template.service.exception.InvalidFileTypeException;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileValidatorImpl implements FileValidator {

    private static final Logger LOG = LoggerFactory.getLogger(FileValidatorImpl.class);

    @Override
    public String validate(MultipartFile file, SupportedFileType expectedType) {
        LOG.trace("Starting file validation");
        Tika tika = new Tika();

        try {
            String detectedType = tika.detect(file.getInputStream());

            switch (expectedType) {
                case SupportedFileType.IMAGE_PDF -> {
                    if (!SupportedFileType.IMAGE_PDF.getAllowedTypes().contains(detectedType)) {
                        LOG.error("The uploaded file is of an invalid type");
                        throw new InvalidFileTypeException();
                    }

                    LOG.info("The uploaded file is valid");
                    return detectedType;
                }

                case SupportedFileType.CSV -> {
                    if (!SupportedFileType.CSV.getAllowedTypes().contains(detectedType)) {
                        LOG.error("The uploaded file is of an invalid type");
                        throw new InvalidFileTypeException();
                    }
                    LOG.info("The uploaded file is valid");
                    return detectedType;
                }

                default -> {
                    LOG.error("The uploaded file is of an invalid type");
                    throw new InvalidFileTypeException();
                }
            }
        } catch (IOException e) {
            LOG.error("Error trying to validate the file: {}", e.getMessage());
            throw new InvalidFileTypeException();
        }
    }

}

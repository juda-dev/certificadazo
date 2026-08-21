package dev.juda.ai_service.template.service.implementation;

import dev.juda.ai_service.shared.service.interfaces.FileValidator;
import dev.juda.ai_service.shared.util.enums.SupportedFileType;
import dev.juda.ai_service.template.configuration.storage.StorageProperties;
import dev.juda.ai_service.template.service.exception.DirectoryNotCreatedException;
import dev.juda.ai_service.template.service.exception.InvalidFileTypeException;
import dev.juda.ai_service.template.service.exception.ResourceNotFoundException;
import dev.juda.ai_service.template.service.exception.UnsavedPreviewImageException;
import dev.juda.ai_service.template.service.interfaces.FileStorageService;
import dev.juda.ai_service.template.service.interfaces.PdfConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path root;
    private final FileValidator fileValidator;
    private final PdfConverter pdfConverter;
    private static final Logger LOG = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    public FileStorageServiceImpl(StorageProperties properties, FileValidator fileValidator,
                                  PdfConverter pdfConverter) {
        this.root = Paths.get(properties.location());
        this.fileValidator = fileValidator;
        this.pdfConverter = pdfConverter;
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            LOG.error("Error trying to create the directory {}", root);
            throw new DirectoryNotCreatedException();
        }
    }

    @Override
    public String savePreview(MultipartFile file) {
        LOG.trace("Starting to save the preview image");
        String fileType = fileValidator.validate(file, SupportedFileType.IMAGE_PDF);

        byte[] imageBytes;

        switch (fileType) {
            case "application/pdf" -> {
                try {
                    imageBytes = pdfConverter.pdfToImage(file.getBytes());
                } catch (IOException e) {
                    LOG.error("Error trying to convert file to image: {}", e.getMessage());
                    throw new InvalidFileTypeException();
                }
            }

            default -> {
                try {
                    imageBytes = file.getBytes();
                } catch (IOException e) {
                    LOG.error("The file received as an image is invalid: {}", e.getMessage());
                    throw new InvalidFileTypeException();
                }
            }
        }

        String filename = UUID.randomUUID() + ".png";

        Path destination = root.resolve(filename);

        try {
            Files.write(destination, imageBytes);
        } catch (IOException e) {
            LOG.error("Error trying to save the preview image to the storage directory");
            throw new UnsavedPreviewImageException();
        }

        LOG.info("Preview image saved successfully");
        return filename;
    }

    @Override
    public ResponseEntity<Resource> getImagePreview(String filename) {
        LOG.trace("Starting to obtain the preview image");
        Path file = root.resolve(filename);

        try {
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists()) {
                LOG.error("The image that was attempted to be obtained does not exist.");
                throw new ResourceNotFoundException();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
        } catch (MalformedURLException e) {
            LOG.error("Error trying to obtain the preview image");
            throw new ResourceNotFoundException();
        }

    }

}

package dev.juda.ai_service.template.service.implementation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.juda.ai_service.shared.service.interfaces.FileValidator;
import dev.juda.ai_service.shared.util.enums.SupportedFileType;
import dev.juda.ai_service.template.configuration.storage.StorageProperties;
import dev.juda.ai_service.template.service.exception.DirectoryNotCreatedException;
import dev.juda.ai_service.template.service.exception.InvalidFileTypeException;
import dev.juda.ai_service.template.service.exception.ResourceNotFoundException;
import dev.juda.ai_service.template.service.exception.UnsavedPreviewImageException;
import dev.juda.ai_service.template.service.interfaces.FileStorageService;
import dev.juda.ai_service.template.service.interfaces.PdfConverter;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path root;
    private final FileValidator fileValidator;
    private final PdfConverter pdfConverter;

    public FileStorageServiceImpl(StorageProperties properties, FileValidator fileValidator,
            PdfConverter pdfConverter) {
        this.root = Paths.get(properties.location());
        this.fileValidator = fileValidator;
        this.pdfConverter = pdfConverter;
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new DirectoryNotCreatedException();
        }
    }

    @Override
    public String savePreview(MultipartFile file) {
        String fileType = fileValidator.validate(file, SupportedFileType.IMAGE_PDF);

        byte[] imageBytes;

        switch (fileType) {
            case "application/pdf" -> {
                try {
                    imageBytes = pdfConverter.pdfToImage(file.getBytes());
                } catch (IOException e) {
                    throw new InvalidFileTypeException();
                }
            }

            default -> {
                try {
                    imageBytes = file.getBytes();
                } catch (IOException e) {
                    throw new InvalidFileTypeException();
                }
            }
        }

        String filename = UUID.randomUUID() + ".png";

        Path destination = root.resolve(filename);

        try {
            Files.write(destination, imageBytes);
        } catch (IOException e) {
            throw new UnsavedPreviewImageException();
        }

        return filename;
    }

    @Override
    public ResponseEntity<Resource> getImagePreview(String filename) {

        Path file = root.resolve(filename);

        try {
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists())
                throw new ResourceNotFoundException();

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException();
        }

    }

}

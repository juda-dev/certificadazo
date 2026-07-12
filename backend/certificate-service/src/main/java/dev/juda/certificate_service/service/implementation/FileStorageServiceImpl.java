package dev.juda.certificate_service.service.implementation;

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

import dev.juda.certificate_service.configuration.storage.StorageProperties;
import dev.juda.certificate_service.service.exception.DirectoryNotCreatedException;
import dev.juda.certificate_service.service.exception.ResourceNotFoundException;
import dev.juda.certificate_service.service.exception.UnsavedCertificateException;
import dev.juda.certificate_service.service.interfaces.FileStorageService;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path root;

    public FileStorageServiceImpl(StorageProperties properties) {
        this.root = Paths.get(properties.location());
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new DirectoryNotCreatedException();
        }
    }

    @Override
    public String saveCertificate(byte[] pdfCertificate) {
        String filename = UUID.randomUUID() + ".pdf";

        Path destination = root.resolve(filename);

        try {
            Files.write(destination, pdfCertificate);
        } catch (IOException e) {
            throw new UnsavedCertificateException();
        }

        return filename;
    }

    @Override
    public ResponseEntity<Resource> getCertificate(String filename) {
        Path file = root.resolve(filename);

        try {
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists())
                throw new ResourceNotFoundException();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException();
        }
    }

}

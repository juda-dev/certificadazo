package dev.juda.certificate_service.service.implementation;

import dev.juda.certificate_service.configuration.storage.StorageProperties;
import dev.juda.certificate_service.service.exception.DirectoryNotCreatedException;
import dev.juda.certificate_service.service.exception.ResourceNotFoundException;
import dev.juda.certificate_service.service.exception.UnsavedCertificateException;
import dev.juda.certificate_service.service.interfaces.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path root;
    private static final Logger LOG = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    public FileStorageServiceImpl(StorageProperties properties) {
        this.root = Paths.get(properties.location());
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            LOG.error("Cannot create directory {}", root);
            throw new DirectoryNotCreatedException();
        }
    }

    @Override
    public String saveCertificate(byte[] pdfCertificate) {
        LOG.trace("Starting certificate saving");
        String filename = UUID.randomUUID() + ".pdf";

        Path destination = root.resolve(filename);

        try {
            Files.write(destination, pdfCertificate);
            LOG.info("Certificate saving completed");
        } catch (IOException e) {
            LOG.error("Cannot save certificate {}", filename);
            throw new UnsavedCertificateException();
        }

        return filename;
    }

    @Override
    public ResponseEntity<Resource> getCertificate(String filename) {
        LOG.trace("Starting certificate recovery for the view");
        Path file = root.resolve(filename);

        try {
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists()) {
                LOG.warn("Certificate file {} does not exist", filename);
                throw new ResourceNotFoundException();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (MalformedURLException e) {
            LOG.error("Certificate file {} does not exist", filename);
            throw new ResourceNotFoundException();
        }
    }

}

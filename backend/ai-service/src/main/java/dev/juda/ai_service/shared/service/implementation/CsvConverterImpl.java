package dev.juda.ai_service.shared.service.implementation;

import dev.juda.ai_service.shared.service.interfaces.CsvConverter;
import dev.juda.ai_service.template.service.exception.InvalidFileTypeException;
import org.apache.commons.io.input.BOMInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Service
public class CsvConverterImpl implements CsvConverter {

    private static final Logger LOG = LoggerFactory.getLogger(CsvConverterImpl.class);

    @Override
    public String toPlainText(MultipartFile file) {
        LOG.trace("Starting conversion of csv file to plain text");
        try (InputStream bomInputStream = BOMInputStream.builder()
                .setInputStream(file.getInputStream())
                .get();
             Reader reader = new InputStreamReader(bomInputStream, StandardCharsets.UTF_8);) {

            LOG.info("CSV file successfully converted to plain text");
            return FileCopyUtils.copyToString(reader);
        } catch (Exception e) {
            LOG.error("Error trying to convert CSV file to plain text: {}", e.getMessage(), e);
            throw new InvalidFileTypeException();
        }
    }

}

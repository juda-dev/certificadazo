package dev.juda.ai_service.shared.service.implementation;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.input.BOMInputStream;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import dev.juda.ai_service.shared.service.interfaces.CsvConverter;
import dev.juda.ai_service.template.presentation.exception.InvalidFileTypeException;

@Service
public class CsvConverterImpl implements CsvConverter {

    @Override
    public String toPlainText(MultipartFile file) {
        try (InputStream bomInputStream = BOMInputStream.builder()
                .setInputStream(file.getInputStream())
                .get();
                Reader reader = new InputStreamReader(bomInputStream, StandardCharsets.UTF_8);) {

            return FileCopyUtils.copyToString(reader);
        } catch (Exception e) {
            throw new InvalidFileTypeException();
        }
    }

}

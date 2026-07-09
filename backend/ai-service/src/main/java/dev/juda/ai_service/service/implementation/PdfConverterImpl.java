package dev.juda.ai_service.service.implementation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import dev.juda.ai_service.presentation.exception.InvalidFileTypeException;
import dev.juda.ai_service.service.interfaces.PdfConverter;

@Service
public class PdfConverterImpl implements PdfConverter {

    @Override
    public byte[] pdfToImage(byte[] pdfBytes) {

        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            PDFRenderer renderer = new PDFRenderer(document);

            BufferedImage image = renderer.renderImageWithDPI(0, 300);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(image, "png", baos);

            return baos.toByteArray();

        } catch (IOException e) {
            throw new InvalidFileTypeException();
        }
    }

}

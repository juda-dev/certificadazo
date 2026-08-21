package dev.juda.ai_service.template.service.implementation;

import dev.juda.ai_service.template.service.exception.InvalidFileTypeException;
import dev.juda.ai_service.template.service.interfaces.PdfConverter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfConverterImpl implements PdfConverter {

    private static final Logger LOG = LoggerFactory.getLogger(PdfConverterImpl.class);

    @Override
    public byte[] pdfToImage(byte[] pdfBytes) {
        LOG.trace("Entering pdfToImage()");

        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            PDFRenderer renderer = new PDFRenderer(document);

            BufferedImage image = renderer.renderImageWithDPI(0, 300);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(image, "png", baos);

            return baos.toByteArray();

        } catch (IOException e) {
            LOG.error("Error trying to convert PDF to image: {}", e.getMessage(), e);
            throw new InvalidFileTypeException();
        }
    }

}

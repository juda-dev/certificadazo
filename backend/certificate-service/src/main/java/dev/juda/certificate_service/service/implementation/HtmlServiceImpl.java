package dev.juda.certificate_service.service.implementation;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import dev.juda.certificate_service.service.exception.FailedPdfGeneration;
import dev.juda.certificate_service.service.interfaces.HtmlService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class HtmlServiceImpl implements HtmlService {

    private static final Logger LOG = LoggerFactory.getLogger(HtmlServiceImpl.class);
    private static final Pattern ISO_DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    private final SpringTemplateEngine templateEngine;

    public HtmlServiceImpl() {
        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCacheable(false);

        this.templateEngine = new SpringTemplateEngine();
        this.templateEngine.setTemplateResolver(resolver);
    }

    @Override
    public byte[] generatePdf(String desing, Map<String, Object> variables) {
        LOG.trace("Starting PDF certificate generation");
        String xHtml = toWellFormedXhtml(render(desing, variables));

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();

            builder.withHtmlContent(xHtml, "");
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();
        } catch (Exception e) {
            LOG.error("Error generating the pdf: {}",e.getMessage(), e);
            throw new FailedPdfGeneration();
        }

    }

    private String render(String desing, Map<String, Object> variables) {
        LOG.trace("Starting rendering of the certificate design with Thymeleaf context");
        Context context = new Context(Locale.forLanguageTag("es"));
        context.setVariables(normalizeDates(variables));

        LOG.info("The certificate design was rendered using the Thymeleaf context.");
        return templateEngine.process(desing, context);
    }

    private String toWellFormedXhtml(String html) {
        Document jsoupDoc = Jsoup.parse(html);
        jsoupDoc.outputSettings()
                .syntax(Document.OutputSettings.Syntax.xml)
                .escapeMode(Entities.EscapeMode.xhtml)
                .charset(StandardCharsets.UTF_8)
                .prettyPrint(false);

        return jsoupDoc.html();
    }

    private Map<String, Object> normalizeDates(Map<String, Object> variables) {
        LOG.trace("Starting normalized dates for certificate view");
        Map<String, Object> normalized = new HashMap<>(variables);
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            if (entry.getValue() instanceof String value && ISO_DATE_PATTERN.matcher(value).matches()) {
                try {
                    normalized.put(entry.getKey(), LocalDate.parse(value));
                } catch (DateTimeParseException e) {
                    LOG.error("The entered date could not be parsed");
                }
            }
        }
        return normalized;
    }

}
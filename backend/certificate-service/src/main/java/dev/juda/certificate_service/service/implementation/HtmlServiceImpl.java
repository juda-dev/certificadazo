package dev.juda.certificate_service.service.implementation;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import dev.juda.certificate_service.service.exception.FailedPdfGeneration;
import dev.juda.certificate_service.service.interfaces.HtmlService;

@Service
public class HtmlServiceImpl implements HtmlService {

    private final TemplateEngine templateEngine;

    public HtmlServiceImpl() {
        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCacheable(false);

        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(resolver);
    }

    @Override
    public byte[] generatePdf(String desing, Map<String, Object> variables) {
        String xHtml = toWellFormedXhtml(render(desing, variables));

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();

            builder.withHtmlContent(xHtml, "");
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new FailedPdfGeneration();
        }

    }

    private String render(String desing, Map<String, Object> variables) {
        Context context = new Context(Locale.forLanguageTag("es"));
        context.setVariables(variables);

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

}

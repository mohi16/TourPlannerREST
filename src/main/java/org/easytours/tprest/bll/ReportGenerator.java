package org.easytours.tprest.bll;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.lowagie.text.DocumentException;
import org.easytours.tpmodel.Tour;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportGenerator {
    private static class Localizer {
        Locale locale;

        public Localizer(Locale locale) {
            this.locale = locale;
        }

        public String get(String key) {
            ResourceBundle rb = ResourceBundle.getBundle("org/easytours/tprest/report_string", locale);
            return rb.getString(key);
        }
    }

    private String parseHTMLSingle(String resource, Tour tour, Locale locale) {
        resource = "org/easytours/tprest/" + resource;
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("tour", tour);

        context.setVariable("_", new Localizer(locale));

        return templateEngine.process(resource, context);
    }

    private byte[] generatePdfFromHTML(String html) throws DocumentException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    public byte[] singleReport(Tour tour, Locale locale) throws DocumentException {
        return generatePdfFromHTML(parseHTMLSingle("single_report", tour, locale));
        /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);
        Paragraph paragraph = new Paragraph(tour.getName())
                .setFontSize(14);
        Paragraph description = new Paragraph(tour.getDescription());
        Paragraph from = new Paragraph(tour.getFrom());
        Paragraph to = new Paragraph(tour.getTo());

        doc.add(paragraph);
        doc.close();

        return byteArrayOutputStream.toByteArray();*/
    }

    public byte[] summaryReport(Tour[] tours) {
        return null;
    }
}

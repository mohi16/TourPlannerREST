package org.easytours.tprest.pl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.easytours.tpmodel.Tour;
import org.easytours.tpmodel.TourLog;
import org.easytours.tpmodel.http.HttpMethod;
import org.easytours.tpmodel.http.HttpStatusCode;
import org.easytours.tprest.bll.BusinessLogic;
import org.easytours.tprest.utils.Pair;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public final class HttpHandlerCreator {
    private final BusinessLogic bl;

    public HttpHandlerCreator(BusinessLogic bl) {
        this.bl = bl;
    }

    public HttpHandler addTourHandler() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                System.out.println("ADD Tour");

                String method = httpExchange.getRequestMethod();
                if (!HttpMethod.POST.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    bl.addTour(objectMapper.readValue(httpExchange.getRequestBody(), Tour.class));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.BAD_REQUEST.getCode(), -1);
                    return;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), -1);
                    return;
                }

                httpExchange.sendResponseHeaders(HttpStatusCode.CREATED.getCode(), -1);
            }
        };
    }

    public HttpHandler deleteTourHandler() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                System.out.println("DELETE Tour");

                String method = httpExchange.getRequestMethod();
                if (!HttpMethod.DELETE.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                try {
                    bl.deleteTour(processPathString(httpExchange.getRequestURI().getPath(), "/delete/"));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.BAD_REQUEST.getCode(), -1);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), -1);
                    return;
                }

                httpExchange.sendResponseHeaders(HttpStatusCode.OK.getCode(), -1);
            }
        };
    }

    public HttpHandler editTourHandler() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                System.out.println("EDIT Tour");

                String method = httpExchange.getRequestMethod();
                if (!HttpMethod.PUT.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    bl.editTour(
                            processPathString(httpExchange.getRequestURI().getPath(), "/edit/"),
                            objectMapper.readValue(httpExchange.getRequestBody(), Tour.class)
                    );
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.BAD_REQUEST.getCode(), -1);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), -1);
                    return;
                }

                httpExchange.sendResponseHeaders(HttpStatusCode.OK.getCode(), -1);
            }
        };
    }

    public HttpHandler getTourHandler() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                System.out.println("GET Tour");

                String method = httpExchange.getRequestMethod();
                if (!HttpMethod.GET.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                Tour tour = null;
                try {
                    String decode = processPathString(httpExchange.getRequestURI().getPath(), "/tours/");
                    System.out.println(decode);
                    tour = bl.getTour(processPathString(httpExchange.getRequestURI().getPath(), "/tours/"));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.BAD_REQUEST.getCode(), -1);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), -1);
                    return;
                }

                ObjectMapper objectMapper = new ObjectMapper();
                String body = objectMapper.writeValueAsString(tour);
                httpExchange.sendResponseHeaders(HttpStatusCode.OK.getCode(), body.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(body.getBytes());
                os.close();
            }
        };
    }

    private String getSearchString(String query) {
        if (null != query && query.contains("search=")) {
            int idx = query.indexOf("search=") + 7;
            return URLDecoder.decode(query.substring(idx,  Math.max(query.indexOf('&', idx), query.length())), StandardCharsets.UTF_8);
        } else {
            return null;
        }
    }

    public HttpHandler getTourNamesHandler() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                System.out.println("GET TourNames");

                String method = httpExchange.getRequestMethod();
                if (!HttpMethod.GET.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                String[] tournames = null;
                try {
                    tournames = bl.getTourNames(getSearchString(httpExchange.getRequestURI().getQuery()));
                } catch (Exception e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), -1);
                    return;
                }

                ObjectMapper objectMapper = new ObjectMapper();
                String body = objectMapper.writeValueAsString(tournames);
                httpExchange.sendResponseHeaders(HttpStatusCode.OK.getCode(), body.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(body.getBytes());
                os.close();

            }
        };
    }

    public HttpHandler getTourWithImageHandler() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                System.out.println("GET TourWithImage");

                String method = httpExchange.getRequestMethod();
                if (!HttpMethod.GET.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }


                Tour tour = null;
                try {
                    String decode = processPathString(httpExchange.getRequestURI().getPath(), "/tourimage/");
                    System.out.println(decode);
                    tour = bl.getTourWithImage(processPathString(httpExchange.getRequestURI().getPath(), "/tourimage/"));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.BAD_REQUEST.getCode(), -1);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), -1);
                    return;
                }

                ObjectMapper objectMapper = new ObjectMapper();
                String body = objectMapper.writeValueAsString(tour);
                httpExchange.sendResponseHeaders(HttpStatusCode.OK.getCode(), body.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(body.getBytes());
                os.flush();
                os.close();
            }
        };
    }

    private String processPathString(String path, String route) {
        path = path.replace(route, "");
        path = path.substring(0, path.length() - 1);

        return URLDecoder.decode(path, StandardCharsets.UTF_8);
    }

    public HttpHandler addTourLogHandler() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                System.out.println("ADD Tourlog");

                String method = httpExchange.getRequestMethod();
                if (!HttpMethod.POST.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                ObjectMapper objectMapper = new ObjectMapper();
                int id;
                try {
                    id = bl.addTourLog(processPathString(httpExchange.getRequestURI().getPath(),"/addLog/"), objectMapper.readValue(httpExchange.getRequestBody(), TourLog.class));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.BAD_REQUEST.getCode(), -1);
                    return;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), -1);
                    return;
                }

                String body = objectMapper.writeValueAsString(id);
                httpExchange.sendResponseHeaders(HttpStatusCode.CREATED.getCode(), body.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(body.getBytes());
                os.close();
            }
        };

    }

    public HttpHandler editTourLogHandler() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                System.out.println("EDIT Tour");

                String method = httpExchange.getRequestMethod();
                if (!HttpMethod.PUT.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    bl.editTourLog(
                            Integer.parseInt(processPathString(httpExchange.getRequestURI().getPath(), "/editLog/")),
                            objectMapper.readValue(httpExchange.getRequestBody(), TourLog.class)
                    );
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.BAD_REQUEST.getCode(), -1);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), -1);
                    return;
                }

                httpExchange.sendResponseHeaders(HttpStatusCode.OK.getCode(), -1);
            }
        };
    }

    public HttpHandler deleteTourLogHandler(){
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                System.out.println("DELETE Tour");

                String method = httpExchange.getRequestMethod();
                if (!HttpMethod.DELETE.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                try {
                    bl.deleteTourLog(Integer.parseInt(processPathString(httpExchange.getRequestURI().getPath(), "/deleteLog/")));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.BAD_REQUEST.getCode(), -1);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), -1);
                    return;
                }

                httpExchange.sendResponseHeaders(HttpStatusCode.OK.getCode(), -1);
            }
        };

    }

    public HttpHandler getTourLogHandler() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                System.out.println("GET Tour");

                String method = httpExchange.getRequestMethod();
                if (!HttpMethod.GET.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                TourLog tourLog = null;
                try {
                    String decode = processPathString(httpExchange.getRequestURI().getPath(), "/logs/");
                    System.out.println(decode);
                    tourLog = bl.getTourLog(Integer.parseInt(processPathString(httpExchange.getRequestURI().getPath(), "/logs/")));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.BAD_REQUEST.getCode(), -1);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), -1);
                    return;
                }

                ObjectMapper objectMapper = new ObjectMapper();
                String body = objectMapper.writeValueAsString(tourLog);
                httpExchange.sendResponseHeaders(HttpStatusCode.OK.getCode(), body.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(body.getBytes());
                os.close();
            }
        };
    }

    private Locale getLocale(String query) {
        if (null != query && query.contains("lang=")) {
            int idx = query.indexOf("lang=") + 5;
            String loc = URLDecoder.decode(query.substring(idx,  Math.max(query.indexOf('&', idx), query.length())), StandardCharsets.UTF_8);

            if (Locale.GERMAN.getLanguage().equals(loc)) {
                return Locale.GERMAN;
            } else {
                return Locale.ENGLISH;
            }
        } else {
            return Locale.ENGLISH;
        }
    }

    public HttpHandler getSingleReportHandler(){
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                System.out.println("GET Single Report");

                String method = httpExchange.getRequestMethod();
                if (!HttpMethod.GET.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                PdfWriter writer = new PdfWriter(byteArrayOutputStream);
                PdfDocument pdfDoc = new PdfDocument(writer);
                Document doc = new Document(pdfDoc);
                Paragraph paragraph = new Paragraph("igrnedine text")
                        .setFontSize(14);
                doc.add(paragraph);
                doc.close();*/
                byte[] bytes;
                try {
                    bytes = bl.generateSingleReport(processPathString(httpExchange.getRequestURI().getPath(), "/singleReport/"), getLocale(httpExchange.getRequestURI().getQuery()));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.BAD_REQUEST.getCode(), -1);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), -1);
                    return;
                }

/*                ObjectMapper objectMapper = new ObjectMapper();
                String body = objectMapper.writeValueAsString(tourLog);*/
                httpExchange.getResponseHeaders().set("Content-Type", "application/pdf");

                httpExchange.sendResponseHeaders(HttpStatusCode.OK.getCode(), bytes.length);
                OutputStream os = httpExchange.getResponseBody();
                os.write(bytes);
                os.close();
            }
        };
    }

    public HttpHandler getSummaryReportHandler(){
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                System.out.println("GET Summary Report");

                String method = httpExchange.getRequestMethod();
                if (!HttpMethod.GET.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                byte[] bytes;
                try {
                    bytes = bl.generateSummaryReport(getLocale(httpExchange.getRequestURI().getQuery()));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.BAD_REQUEST.getCode(), -1);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), -1);
                    return;
                }

                httpExchange.getResponseHeaders().set("Content-Type", "application/pdf");

                httpExchange.sendResponseHeaders(HttpStatusCode.OK.getCode(), bytes.length);
                OutputStream os = httpExchange.getResponseBody();
                os.write(bytes);
                os.close();
            }
        };
    }

    public HttpHandler getImportHandler(){
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                System.out.println("POST Import");

                String method = httpExchange.getRequestMethod();
                if (!HttpMethod.POST.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    bl.importTours(objectMapper.readValue(httpExchange.getRequestBody(), Tour[].class));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.BAD_REQUEST.getCode(), -1);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), -1);
                    return;
                }

                httpExchange.sendResponseHeaders(HttpStatusCode.CREATED.getCode(), -1);
            }
        };
    }

    public HttpHandler getExportHandler(){
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                System.out.println("GET Export");

                String method = httpExchange.getRequestMethod();
                if (!HttpMethod.GET.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                Tour[] tours;
                try {
                    tours = bl.exportTours();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.BAD_REQUEST.getCode(), -1);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), -1);
                    return;
                }
                ObjectMapper objectMapper = new ObjectMapper();
                String body = objectMapper.writeValueAsString(tours);

                httpExchange.sendResponseHeaders(HttpStatusCode.OK.getCode(), body.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(body.getBytes());
                os.close();
            }
        };
    }
}


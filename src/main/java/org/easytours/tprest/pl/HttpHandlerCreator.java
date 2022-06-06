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
import org.easytours.tpmodel.logging.LoggerFactory;
import org.easytours.tprest.bll.BusinessLogic;
import org.easytours.tprest.dal.logging.LogManager;
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

    private void logMethod(String method, String route) {
        LogManager.getLogger().info("HTTP Request incoming on: " + method + " " + route);
    }

    public HttpHandler addTourHandler() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {


                String method = httpExchange.getRequestMethod();
                String route = httpExchange.getRequestURI().getPath();
                logMethod(method, route);
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


                String method = httpExchange.getRequestMethod();
                String route = httpExchange.getRequestURI().getPath();
                logMethod(method, route);
                if (!HttpMethod.DELETE.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                try {
                    bl.deleteTour(processPathString(route, "/delete/"));
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

                String method = httpExchange.getRequestMethod();
                String route = httpExchange.getRequestURI().getPath();
                logMethod(method, route);
                if (!HttpMethod.PUT.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    bl.editTour(
                            processPathString(route, "/edit/"),
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
                String method = httpExchange.getRequestMethod();
                String route = httpExchange.getRequestURI().getPath();
                logMethod(method, route);
                if (!HttpMethod.GET.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                Tour tour = null;
                try {
                    tour = bl.getTour(processPathString(route, "/tours/"));
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

                String method = httpExchange.getRequestMethod();
                String route = httpExchange.getRequestURI().getPath();
                logMethod(method, route);
                if (!HttpMethod.GET.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                String[] tournames = null;
                try {
                    String query = httpExchange.getRequestURI().getQuery();
                    LogManager.getLogger().debug("search query: " + query);
                    LogManager.getLogger().debug("processed query: " + getSearchString(query));
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

                String method = httpExchange.getRequestMethod();
                String route = httpExchange.getRequestURI().getPath();
                logMethod(method, route);
                if (!HttpMethod.GET.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }


                Tour tour = null;
                try {
                    tour = bl.getTourWithImage(processPathString(route, "/tourimage/"));
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

                String method = httpExchange.getRequestMethod();
                String route = httpExchange.getRequestURI().getPath();
                logMethod(method, route);
                if (!HttpMethod.POST.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                ObjectMapper objectMapper = new ObjectMapper();
                int id;
                try {
                    id = bl.addTourLog(processPathString(route,"/addLog/"), objectMapper.readValue(httpExchange.getRequestBody(), TourLog.class));
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

                String method = httpExchange.getRequestMethod();
                String route = httpExchange.getRequestURI().getPath();
                logMethod(method, route);
                if (!HttpMethod.PUT.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    bl.editTourLog(
                            Integer.parseInt(processPathString(route, "/editLog/")),
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

                String method = httpExchange.getRequestMethod();
                String route = httpExchange.getRequestURI().getPath();
                logMethod(method, route);
                if (!HttpMethod.DELETE.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                try {
                    bl.deleteTourLog(Integer.parseInt(processPathString(route, "/deleteLog/")));
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

                String method = httpExchange.getRequestMethod();
                String route = httpExchange.getRequestURI().getPath();
                logMethod(method, route);
                if (!HttpMethod.GET.name().equals(method)) {
                    httpExchange.sendResponseHeaders(HttpStatusCode.NOT_ALLOWED.getCode(), -1);
                    return;
                }

                TourLog tourLog = null;
                try {
                    tourLog = bl.getTourLog(Integer.parseInt(processPathString(route, "/logs/")));
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
                String method = httpExchange.getRequestMethod();
                String route = httpExchange.getRequestURI().getPath();
                logMethod(method, route);
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
                    bytes = bl.generateSingleReport(processPathString(route, "/singleReport/"), getLocale(httpExchange.getRequestURI().getQuery()));
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
                String method = httpExchange.getRequestMethod();
                String route = httpExchange.getRequestURI().getPath();
                logMethod(method, route);
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
                String method = httpExchange.getRequestMethod();
                String route = httpExchange.getRequestURI().getPath();
                logMethod(method, route);
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
                String method = httpExchange.getRequestMethod();
                String route = httpExchange.getRequestURI().getPath();
                logMethod(method, route);
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


package org.easytours.tprest.pl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.easytours.tpmodel.Tour;
import org.easytours.tpmodel.http.HttpMethod;
import org.easytours.tpmodel.http.HttpStatusCode;
import org.easytours.tprest.bll.BusinessLogic;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

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

    private String processPathString(String path, String route) {
        path = path.replace(route, "");
        path = path.substring(0, path.length() - 1);

        return URLDecoder.decode(path, StandardCharsets.UTF_8);
    }
}

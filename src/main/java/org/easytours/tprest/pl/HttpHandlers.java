package org.easytours.tprest.pl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public final class HttpHandlers {
    public static HttpHandler addTourHandler() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                System.out.println("ADD Tour");

                String method = httpExchange.getRequestMethod();
                if (!"POST".equals(method)) {
                    httpExchange.sendResponseHeaders(400, -1);
                }

                httpExchange.sendResponseHeaders(200, -1);
            }
        };
    }

    public static HttpHandler deleteTourHandler() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                System.out.println("DELETE Tour");

                httpExchange.sendResponseHeaders(200, -1);
            }
        };
    }
}

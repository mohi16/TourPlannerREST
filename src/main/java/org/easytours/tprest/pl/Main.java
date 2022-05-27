package org.easytours.tprest.pl;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public final class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(50001), 10);

        server.createContext("/add/", HttpHandlers.addTourHandler());
        server.createContext("/delete/", HttpHandlers.deleteTourHandler());

        System.out.println("STARTING Server at " + server.getAddress().getAddress() + " with Port " + server.getAddress().getPort());
        server.start();
        System.out.println("AFTER Start");
    }
}

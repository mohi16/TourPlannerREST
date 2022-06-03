package org.easytours.tprest.pl;

import com.sun.net.httpserver.HttpServer;
import org.easytours.tprest.bll.SimpleBusinessLogic;
import org.easytours.tprest.config.Config;
import org.easytours.tprest.dal.Database;
import org.easytours.tprest.dal.dao.BasicTourDAO;
import org.easytours.tprest.dal.dao.BasicTourLogDAO;
import org.easytours.tprest.dal.dao.TourDAO;
import org.easytours.tprest.dal.dao.TourLogDAO;

import java.io.IOException;
import java.net.InetSocketAddress;

public final class Main {
    public static void main(String[] args) throws IOException {
        Config.load();
        Database db = null;
        try {
            db = new Database();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        TourLogDAO tourLogDao = new BasicTourLogDAO(db);
        TourDAO tourDao = new BasicTourDAO(db, tourLogDao);

        HttpHandlerCreator creator = new HttpHandlerCreator(new SimpleBusinessLogic(tourDao, tourLogDao));
        HttpServer server = HttpServer.create(new InetSocketAddress(Config.getConfig().getPort()), 10);

        server.createContext("/add/", creator.addTourHandler());
        server.createContext("/delete/", creator.deleteTourHandler());
        server.createContext("/edit/", creator.editTourHandler());
        server.createContext("/tours/", creator.getTourHandler());
        server.createContext("/tournames/", creator.getTourNamesHandler());
        server.createContext("/tourimage/", creator.getTourWithImageHandler());
        server.createContext("/addLog/", creator.addTourLogHandler());
        server.createContext("/deleteLog/", creator.deleteTourLogHandler());
        server.createContext("/editLog/", creator.editTourLogHandler());
        server.createContext("/logs/", creator.getTourLogHandler());

        System.out.println("STARTING Server at " + server.getAddress().getAddress() + " with Port " + server.getAddress().getPort());
        server.start();
        System.out.println("AFTER Start");
    }
}

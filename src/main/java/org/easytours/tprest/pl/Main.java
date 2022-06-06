package org.easytours.tprest.pl;

import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.Logger;
import org.easytours.tpmodel.logging.LoggerFactory;
import org.easytours.tpmodel.logging.LoggerWrapper;
import org.easytours.tprest.bll.ReportGenerator;
import org.easytours.tprest.bll.SimpleBusinessLogic;
import org.easytours.tprest.config.Config;
import org.easytours.tprest.dal.Database;
import org.easytours.tprest.dal.dao.BasicTourDAO;
import org.easytours.tprest.dal.dao.BasicTourLogDAO;
import org.easytours.tprest.dal.dao.TourDAO;
import org.easytours.tprest.dal.dao.TourLogDAO;
import org.easytours.tprest.dal.logging.LogManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Locale;

public final class Main {
    public static void main(String[] args) throws IOException {
        try {
            LogManager.getLogger().info("Attempt to load config...");
            Config.load();
            LogManager.getLogger().info("Config loaded");
        } catch(IOException e){
            BufferedWriter bw = new BufferedWriter(new FileWriter("./appconfig.yaml"));
            bw.write("""
                    port: 5001
                    db:
                      host: jdbc:postgresql://localhost:5432/tpdb
                      user: user
                      password: tp
                    mapquestStaticMapUrl: https://www.mapquestapi.com/staticmap/v5/map
                    mapquestDirectionsUrl: http://www.mapquestapi.com/directions/v2/route
                    mapquestApiKey: key""");
            bw.close();
            LogManager.getLogger().info("Config file created, Please fill out missing fields.");
            return;
        }
        Database db = null;
        try {
            db = new Database();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        TourLogDAO tourLogDao = new BasicTourLogDAO(db);
        TourDAO tourDao = new BasicTourDAO(db, tourLogDao);
        ReportGenerator reportGenerator = new ReportGenerator();

        HttpHandlerCreator creator = new HttpHandlerCreator(new SimpleBusinessLogic(tourDao, tourLogDao, reportGenerator));
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
        server.createContext("/singleReport/", creator.getSingleReportHandler());
        server.createContext("/summaryReport/", creator.getSummaryReportHandler());
        server.createContext("/import/", creator.getImportHandler());
        server.createContext("/export/", creator.getExportHandler());

        server.start();
        LogManager.getLogger().info("Server running at " + server.getAddress().getAddress() + " on Port " + server.getAddress().getPort());
    }
}

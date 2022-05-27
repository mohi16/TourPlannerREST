package org.easytours.tprest.config;

import org.easytours.tpmodel.config.ConfigLoader;

import java.io.IOException;

public class Config {
    private static AppConfig appConfig;

    public static void load() throws IOException {
        appConfig = ConfigLoader.load(AppConfig.class, "./appconfig.yaml");
    }

    public static AppConfig getConfig(){
        return appConfig;
    }




}

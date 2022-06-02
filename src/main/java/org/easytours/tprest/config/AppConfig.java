package org.easytours.tprest.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Locale;

@Getter
public class AppConfig {
    private DBConfig db;

    private short port;
    private String mapquestStaticMapUrl;
    private String mapquestDirectionsUrl;
    private String mapquestApiKey;

    public AppConfig(short port, DBConfig db, String mapquestStaticMapUrl, String mapquestDirectionsUrl, String mapquestApiKey) {
        this.port = port;
        this.db = db;
        this.mapquestStaticMapUrl = mapquestStaticMapUrl;
        this.mapquestDirectionsUrl = mapquestDirectionsUrl;
        this.mapquestApiKey = mapquestApiKey;
    }

    public AppConfig() {}

}

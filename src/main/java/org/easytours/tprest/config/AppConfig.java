package org.easytours.tprest.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Locale;

@Getter
public class AppConfig {
    private DBConfig db;

    private short port;

    public AppConfig(short port, DBConfig db) {
        this.port = port;
        this.db = db;

    }

    public AppConfig() {}

}

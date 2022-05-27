package org.easytours.tprest.config;

import lombok.Getter;

@Getter
public class DBConfig {
    private String host;
    private String user;
    private String password;


    public DBConfig(String host, String user, String password) {
        this.host = host;
        this.user = user;
        this.password = password;
    }

    public DBConfig() {
    }
}

package org.easytours.tprest.dal;

import org.easytours.tprest.config.Config;
import org.easytours.tprest.dal.logging.LogManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    public Database() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(
                Config.getConfig().getDb().getHost(),
                Config.getConfig().getDb().getUser(),
                Config.getConfig().getDb().getPassword()
        );
    }
}

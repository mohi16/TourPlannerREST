package org.easytours.tprest.dal.dao;

import org.easytours.tpmodel.Tour;
import org.easytours.tprest.dal.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BasicTourDAO implements TourDAO {
    Database db;

    public BasicTourDAO(Database db) {
        this.db = db;
    }

    @Override
    public void create(Tour tour) throws Exception {
        Connection con = null;
        try {
            con = db.connect();
            String query = "INSERT INTO tours (t_name, t_description, t_from, t_to, t_transport_type, t_distance, t_est_time, t_route_info) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, tour.getName());
            preparedStatement.setString(2, tour.getDescription());
            preparedStatement.setString(3, tour.getFrom());
            preparedStatement.setString(4, tour.getTo());
            preparedStatement.setString(5, tour.getTransportType());
            preparedStatement.setDouble(6, tour.getDistance());
            preparedStatement.setLong(7, tour.getEstTime());
            preparedStatement.setString(8, tour.getRouteInfo());

            preparedStatement.execute();

            if (preparedStatement.getUpdateCount() <= 0) {
                throw new Exception("Can not add Tour");
            }
        } catch (SQLException e) {
            throw e;
        }
        finally{
            if(con != null) {
                con.close();
            }
        }

    }

    @Override
    public Tour read(String name) {
        return null;
    }

    @Override
    public void update(String name, Tour newTour) {

    }

    @Override
    public void delete(String name) {

    }
}

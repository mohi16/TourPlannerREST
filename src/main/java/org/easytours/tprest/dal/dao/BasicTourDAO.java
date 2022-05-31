package org.easytours.tprest.dal.dao;

import org.easytours.tpmodel.Tour;
import org.easytours.tprest.dal.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BasicTourDAO implements TourDAO {
    Database db;

    public BasicTourDAO(Database db) {
        this.db = db;
    }

    private void fillPsWithTour(PreparedStatement ps, Tour tour) throws SQLException {
        ps.setString(1, tour.getName());
        ps.setString(2, tour.getDescription());
        ps.setString(3, tour.getFrom());
        ps.setString(4, tour.getTo());
        ps.setString(5, tour.getTransportType());
        ps.setDouble(6, tour.getDistance());
        ps.setLong(7, tour.getEstTime());
        ps.setString(8, tour.getRouteInfo());
    }

    @Override
    public void create(Tour tour) throws Exception {

        try (Connection con = db.connect()) {
            String query = "INSERT INTO tours (t_name, t_description, t_from, t_to, t_transport_type, t_distance, t_est_time, t_route_info, t_map) VALUES (?,?,?,?,?,?,?,?,'')";
            PreparedStatement ps = con.prepareStatement(query);
            /*ps.setString(1, tour.getName());
            ps.setString(2, tour.getDescription());
            ps.setString(3, tour.getFrom());
            ps.setString(4, tour.getTo());
            ps.setString(5, tour.getTransportType());
            ps.setDouble(6, tour.getDistance());
            ps.setLong(7, tour.getEstTime());
            ps.setString(8, tour.getRouteInfo());*/
            fillPsWithTour(ps, tour);


            ps.execute();

            if (ps.getUpdateCount() <= 0) {
                throw new Exception("Can not add Tour");
            }
        } catch (SQLException e) {
            throw e;
        }

    }

    @Override
    public Tour read(String name) throws Exception {
        try (Connection con = db.connect()) {
            String query = "SELECT t_name, t_description, t_from, t_to, t_transport_type, t_distance, t_est_time, t_route_info FROM tours WHERE t_name = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new Exception("Cannot get tour");
            }
            Tour tour = new Tour(
                rs.getString(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getDouble(6),
                rs.getLong(7),
                rs.getString(5),
                rs.getString(8)
            );
            rs.close();
            return tour;
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void update(String name, Tour newTour) throws Exception  {
        try(Connection con = db.connect()){
            String query = "UPDATE tours SET t_name = ?, t_description = ?, t_from = ?, t_to = ?, t_transport_type = ?, t_distance = ?, t_est_time = ?, t_route_info = ? WHERE t_name = ?";
            PreparedStatement ps = con.prepareStatement(query);
            /*ps.setString(1, newTour.getName());
            ps.setString(2, newTour.getDescription());
            ps.setString(3, newTour.getFrom());
            ps.setString(4, newTour.getTo());
            ps.setString(5, newTour.getTransportType());
            ps.setDouble(6, newTour.getDistance());
            ps.setLong(7, newTour.getEstTime());
            ps.setString(8, newTour.getRouteInfo());*/
            fillPsWithTour(ps, newTour);
            ps.setString(9, name);
            ps.execute();
            if (ps.getUpdateCount() <= 0) {
                throw new Exception("Can not edit Tour");
            }
        }
        catch(SQLException e){
            throw e;
        }
    }

    @Override
    public void delete(String name) throws Exception {
        try (Connection con = db.connect()) {
            String query = "DELETE FROM tours WHERE t_name = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.execute();
            if (ps.getUpdateCount() <= 0) {
                throw new Exception("Can not delete Tour");
            }
        }
        catch (SQLException e) {
            throw e;
        }
    }
}

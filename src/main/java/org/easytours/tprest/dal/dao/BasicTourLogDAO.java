package org.easytours.tprest.dal.dao;

import org.easytours.tpmodel.TourLog;
import org.easytours.tprest.dal.Database;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BasicTourLogDAO implements TourLogDAO {
    Database db;

    public BasicTourLogDAO(Database db) {
        this.db = db;
    }

    private int getTourId(String tourname) throws Exception {
        System.out.print("Tourname: ");
        System.out.println(tourname);
        try (Connection con = db.connect()) {
            String query = "SELECT t_id FROM tours WHERE t_name = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, tourname);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new Exception("Can not get Id");
            }
            int id = rs.getInt(1);
            rs.close();

            return id;
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void create(String tourName, TourLog tourLog) throws Exception {
        int id = getTourId(tourName);

        try (Connection con = db.connect()) {
            String query = "INSERT INTO logs (l_date, l_comment, l_difficulty, l_total_time, l_rating, t_id) VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, tourLog.getDateTime());
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.parse(tourLog.getDateTime(), DateTimeFormatter.ofPattern("d.M.y H:m:s"))));
            ps.setString(2, tourLog.getComment());
            ps.setInt(3, tourLog.getDifficulty());
            ps.setLong(4, tourLog.getTotalTime());
            ps.setInt(5, tourLog.getRating());
            ps.setInt(6, id);

            ps.execute();

            if (ps.getUpdateCount() <= 0) {
                throw new Exception("Can not add Tour");
            }
        } catch (SQLException e) {
            throw e;
        }

    }

    @Override
    public TourLog read(int id) throws Exception {
        try (Connection con = db.connect()){
            String query = "SELECT l_date, l_comment, l_difficulty, l_total_time, l_rating FROM logs WHERE l_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if(!rs.next()) {
                throw new Exception("Cannot get logs");
            }
            TourLog tourLog = new TourLog(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getInt(3),
                    rs.getLong(4),
                    rs.getInt(5)
            );
            rs.close();
            return tourLog;
        } catch(SQLException e){
            throw e;
        }
    }

    @Override
    public void update(int id, TourLog newLog) throws Exception {
        try (Connection con = db.connect()) {
            String query = "UPDATE logs SET l_date = ?, l_comment = ?, l_difficulty = ?, l_total_time = ?, l_rating = ? WHERE l_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.parse(newLog.getDateTime(), DateTimeFormatter.ofPattern("d.M.y H:m:s"))));
            ps.setString(2, newLog.getComment());
            ps.setInt(3, newLog.getDifficulty());
            ps.setLong(4, newLog.getTotalTime());
            ps.setInt(5, newLog.getRating());
            ps.setInt(6, id);
            ps.execute();
            if(ps.getUpdateCount() <= 0){
                throw new Exception("Can not edit log");
            }
        } catch(SQLException e){
            throw e;
        }

    }

    @Override
    public void delete(int id) throws Exception {
        try (Connection con = db.connect()) {
            String query = "DELETE FROM logs WHERE l_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ps.execute();
            if(ps.getUpdateCount() <= 0){
                throw new Exception("Can not edit log");
            }
        } catch(SQLException e) {
            throw e;
        }
    }

    @Override
    public TourLog[] readAll(String tourName) throws Exception {
        List<TourLog> tourLogs = new ArrayList<>();
        try (Connection con = db.connect()){
            String query = "SELECT l_date, l_comment, l_difficulty, l_total_time, l_rating, l_id FROM logs WHERE t_id = (SELECT t_id FROM tours WHERE t_name = ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, tourName);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TourLog tourLog = new TourLog(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getLong(4),
                        rs.getInt(5)
                );
                tourLog.setId(rs.getInt(6));

                tourLogs.add(tourLog);
            }

            rs.close();
        } catch(SQLException e){
            throw e;
        }

        return tourLogs.toArray(new TourLog[]{});
    }
}

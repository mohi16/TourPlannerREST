package org.easytours.tprest.dal.dao;

import org.easytours.tpmodel.TourLog;
import org.easytours.tprest.dal.Database;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class TestTourLogDAO {
    Database db;
    Connection con;
    TourLogDAO tourLogDao;

    @Before
    public void init() throws SQLException {
        db = mock(Database.class);
        tourLogDao = new BasicTourLogDAO(db);
        con = mock(Connection.class);
        when(db.connect()).thenReturn(con);
    }

    private TourLog getTourLog() {
        return new TourLog(
                "06.05.2022 12:13:14",
                "Some comment",
                2,
                2001,
                4
        );
    }

    @Test
    public void testCreate() throws SQLException {
        int expectedId = 1;
        PreparedStatement ps = mock(PreparedStatement.class);
        PreparedStatement psId = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        ResultSet rsId = mock(ResultSet.class);
        when(con.prepareStatement("INSERT INTO logs (l_date, l_comment, l_difficulty, l_total_time, l_rating, t_id) VALUES (?,?,?,?,?,?) RETURNING l_id")).thenReturn(ps);
        when(con.prepareStatement("SELECT t_id FROM tours WHERE t_name = ?")).thenReturn(psId);
        when(ps.executeQuery()).thenReturn(rs);
        when(psId.executeQuery()).thenReturn(rsId);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(expectedId);
        when(rsId.next()).thenReturn(true);
        when(rsId.getInt(1)).thenReturn(10);

        int id = 0;
        try {
            id = tourLogDao.create("Tourname", getTourLog());
        } catch (Exception e) {
            fail();
        }

        verify(ps).executeQuery();
        assertEquals(expectedId, id);
    }

    @Test
    public void testCreateFail() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        PreparedStatement psId = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        ResultSet rsId = mock(ResultSet.class);
        when(con.prepareStatement("INSERT INTO logs (l_date, l_comment, l_difficulty, l_total_time, l_rating, t_id) VALUES (?,?,?,?,?,?) RETURNING l_id")).thenReturn(ps);
        when(con.prepareStatement("SELECT t_id FROM tours WHERE t_name = ?")).thenReturn(psId);
        when(ps.executeQuery()).thenReturn(rs);
        when(psId.executeQuery()).thenReturn(rsId);
        when(rs.next()).thenReturn(false);
        when(rsId.next()).thenReturn(true);
        when(rsId.getInt(1)).thenReturn(10);

        boolean isThrown = false;
        try {
            tourLogDao.create("Tourname", getTourLog());
        } catch (Exception e) {
            isThrown = true;
        }

        verify(ps).executeQuery();
        assertTrue(isThrown);
    }

    @Test
    public void testRead() throws SQLException {
        TourLog expectedLog = getTourLog();
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        when(con.prepareStatement("SELECT l_date, l_comment, l_difficulty, l_total_time, l_rating FROM logs WHERE l_id = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getString(1)).thenReturn("2022-05-06 12:13:14");
        when(rs.getString(2)).thenReturn("Some comment");
        when(rs.getInt(3)).thenReturn(2);
        when(rs.getLong(4)).thenReturn(2001L);
        when(rs.getInt(5)).thenReturn(4);

        TourLog log = null;
        try {
            log = tourLogDao.read(1 );
        } catch (Exception e) {
            fail();
        }

        verify(ps).executeQuery();
        assertEquals(expectedLog, log);
    }

    @Test
    public void testReadFail() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        when(con.prepareStatement("SELECT l_date, l_comment, l_difficulty, l_total_time, l_rating FROM logs WHERE l_id = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        boolean isThrown = false;
        try {
            tourLogDao.read(1);
        } catch (Exception e) {
            isThrown = true;
        }

        verify(ps).executeQuery();
        assertTrue(isThrown);
    }
}

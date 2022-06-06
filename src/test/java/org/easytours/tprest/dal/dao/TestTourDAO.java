package org.easytours.tprest.dal.dao;

import org.easytours.tpmodel.Tour;
import org.easytours.tprest.dal.Database;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static org.mockito.Mockito.*;

public class TestTourDAO {
    Database db;
    Connection con;
    TourDAO tourDao;
    TourLogDAO tourLogDao;

    @Before
    public void init() throws SQLException {
        db = mock(Database.class);
        tourDao = new BasicTourDAO(db, tourLogDao);
        con = mock(Connection.class);
        when(db.connect()).thenReturn(con);
    }

    private Tour getTour() {
        return new Tour(
                "Tourname",
                "Description",
                "From",
                "To",
                0,
                0,
                "Transport Type",
                "Route Info",
                ""
        );
    }

    @Test
    public void testCreate() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        when(con.prepareStatement("INSERT INTO tours (t_name, t_description, t_from, t_to, t_transport_type, t_distance, t_est_time, t_route_info, t_map) VALUES (?,?,?,?,?,?,?,?,'')")).thenReturn(ps);
        when(ps.execute()).thenReturn(true);
        when(ps.getUpdateCount()).thenReturn(1);

        try {
            tourDao.create(getTour());
        } catch (Exception e) {
            fail();
        }

        verify(ps).execute();
        verify(ps).getUpdateCount();
    }

    @Test
    public void testCreateFail() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        when(con.prepareStatement("INSERT INTO tours (t_name, t_description, t_from, t_to, t_transport_type, t_distance, t_est_time, t_route_info, t_map) VALUES (?,?,?,?,?,?,?,?,'')")).thenReturn(ps);
        when(ps.execute()).thenReturn(true);
        when(ps.getUpdateCount()).thenReturn(0);

        boolean isThrown = false;
        try {
            tourDao.create(getTour());
        } catch (Exception e) {
            isThrown = true;
        }

        verify(ps).execute();
        verify(ps).getUpdateCount();
        assertTrue(isThrown);
    }

    @Test
    public void testRead() throws SQLException {
        Tour expectedTour = getTour();
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        when(con.prepareStatement("SELECT t_name, t_description, t_from, t_to, t_transport_type, t_distance, t_est_time, t_route_info FROM tours WHERE t_name = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getString(1)).thenReturn("Tourname");
        when(rs.getString(2)).thenReturn("Description");
        when(rs.getString(3)).thenReturn("From");
        when(rs.getString(4)).thenReturn("To");
        when(rs.getString(5)).thenReturn("Transport Type");
        when(rs.getDouble(6)).thenReturn(0.0);
        when(rs.getLong(7)).thenReturn(0L);
        when(rs.getString(8)).thenReturn("Route Info");

        Tour tour = null;
        try {
            tour = tourDao.read("Tourname");
        } catch (Exception e) {
            fail();
        }

        verify(ps).setString(1, "Tourname");
        verify(ps).executeQuery();
        assertEquals(expectedTour, tour);
    }

    @Test
    public void testReadFail() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        when(con.prepareStatement("SELECT t_name, t_description, t_from, t_to, t_transport_type, t_distance, t_est_time, t_route_info FROM tours WHERE t_name = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        boolean isThrown = false;
        try {
            tourDao.read("Tourname");
        } catch (Exception e) {
            isThrown = true;
        }

        verify(ps).setString(1, "Tourname");
        verify(ps).executeQuery();
        assertTrue(isThrown);
    }

    @Test
    public void testUpdate() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        when(con.prepareStatement("UPDATE tours SET t_name = ?, t_description = ?, t_from = ?, t_to = ?, t_transport_type = ?, t_distance = ?, t_est_time = ?, t_route_info = ? WHERE t_name = ?")).thenReturn(ps);
        when(ps.execute()).thenReturn(true);
        when(ps.getUpdateCount()).thenReturn(1);

        try {
            tourDao.update("Tourname", getTour());
        } catch (Exception e) {
            fail();
        }

        verify(ps).setString(9, "Tourname");
        verify(ps).execute();
        verify(ps).getUpdateCount();
    }

    @Test
    public void testUpdateFail() throws Exception {
        PreparedStatement ps = mock(PreparedStatement.class);
        when(con.prepareStatement("UPDATE tours SET t_name = ?, t_description = ?, t_from = ?, t_to = ?, t_transport_type = ?, t_distance = ?, t_est_time = ?, t_route_info = ? WHERE t_name = ?")).thenReturn(ps);
        when(ps.execute()).thenReturn(true);
        when(ps.getUpdateCount()).thenReturn(0);

        boolean isThrown = false;
        try {
            tourDao.update("Tourname", getTour());
        } catch (Exception e) {
            isThrown = true;
        }

        verify(ps).setString(9, "Tourname");
        verify(ps).execute();
        verify(ps).getUpdateCount();
        assertTrue(isThrown);
    }

    @Test
    public void testDelete() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        when(con.prepareStatement("DELETE FROM tours WHERE t_name = ?")).thenReturn(ps);
        when(ps.execute()).thenReturn(true);
        when(ps.getUpdateCount()).thenReturn(1);

        try {
            tourDao.delete("Tourname");
        } catch (Exception e) {
            fail();
        }

        verify(ps).setString(1, "Tourname");
        verify(ps).execute();
        verify(ps).getUpdateCount();
    }

    @Test
    public void testDeleteFail() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        when(con.prepareStatement("DELETE FROM tours WHERE t_name = ?")).thenReturn(ps);
        when(ps.execute()).thenReturn(true);
        when(ps.getUpdateCount()).thenReturn(0);

        boolean isThrown = false;
        try {
            tourDao.delete("Tourname");
        } catch (Exception e) {
            isThrown = true;
        }

        verify(ps).setString(1, "Tourname");
        verify(ps).execute();
        verify(ps).getUpdateCount();
        assertTrue(isThrown);
    }

    @Test
    public void testReadTourNames() throws SQLException {
        String[] expectedNames = new String[] {"Tourname1", "Tourname2", "Tourname3"};
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        when(con.prepareStatement("SELECT t_name FROM tours")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.getString(1)).thenReturn("Tourname1", "Tourname2", "Tourname3");
        when(rs.next()).thenReturn(true, true, true, false);

        String[] names = null;
        try {
            names = tourDao.readTourNames();
        } catch (Exception e) {
            fail();
        }

        verify(ps).executeQuery();
        assertArrayEquals(expectedNames, names);
    }

    @Test
    public void testReadTourNamesFail() throws SQLException {
        String[] expectedNames = new String[] {};
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        when(con.prepareStatement("SELECT t_name FROM tours")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        String[] names = null;
        try {
            names = tourDao.readTourNames();
        } catch (Exception e) {
            fail();
        }

        verify(ps).executeQuery();
        assertArrayEquals(expectedNames, names);
    }

    @Test
    public void testReadTourNamesWithFilter() throws SQLException {
        String[] expectedNames = new String[] {"Tourname1", "Tourname2", "Tourname3"};
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        when(con.prepareStatement("SELECT t_name FROM tours WHERE " +
                                    "(UPPER(t_name) LIKE UPPER(?) ESCAPE '!') OR " +
                                    "(UPPER(t_description) LIKE UPPER(?) ESCAPE '!') OR " +
                                    "(UPPER(t_from) LIKE UPPER(?) ESCAPE '!') OR " +
                                    "(UPPER(t_to) LIKE UPPER(?) ESCAPE '!') OR " +
                                    "(UPPER(t_transport_type) LIKE UPPER(?) ESCAPE '!') OR " +
                                    "(UPPER(t_route_info) LIKE UPPER(?) ESCAPE '!') OR " +
                                    "0 < (SELECT COUNT(*) FROM logs WHERE UPPER(l_comment) LIKE UPPER(?) ESCAPE '!')"))
                .thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.getString(1)).thenReturn("Tourname1", "Tourname2", "Tourname3");
        when(rs.next()).thenReturn(true, true, true, false);

        String[] names = null;
        try {
            names = tourDao.readTourNames("Tourname");
        } catch (Exception e) {
            fail();
        }

        verify(ps).executeQuery();
        assertArrayEquals(expectedNames, names);
    }

    @Test
    public void testReadTourNamesWithFilterFail() throws SQLException {
        String[] expectedNames = new String[] {};
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        when(con.prepareStatement("SELECT t_name FROM tours WHERE " +
                "(UPPER(t_name) LIKE UPPER(?) ESCAPE '!') OR " +
                "(UPPER(t_description) LIKE UPPER(?) ESCAPE '!') OR " +
                "(UPPER(t_from) LIKE UPPER(?) ESCAPE '!') OR " +
                "(UPPER(t_to) LIKE UPPER(?) ESCAPE '!') OR " +
                "(UPPER(t_transport_type) LIKE UPPER(?) ESCAPE '!') OR " +
                "(UPPER(t_route_info) LIKE UPPER(?) ESCAPE '!') OR " +
                "0 < (SELECT COUNT(*) FROM logs WHERE UPPER(l_comment) LIKE UPPER(?) ESCAPE '!')"))
                .thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        String[] names = null;
        try {
            names = tourDao.readTourNames("Tourname");
        } catch (Exception e) {
            fail();
        }

        verify(ps).executeQuery();
        assertArrayEquals(expectedNames, names);
    }
}

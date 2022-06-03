package org.easytours.tprest.bll;

import org.easytours.tpmodel.Tour;
import org.easytours.tpmodel.TourLog;
import org.easytours.tprest.dal.dao.TourDAO;
import org.easytours.tprest.dal.dao.TourLogDAO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestSimpleBusinessLogic {
    private TourDAO tourDao;
    private TourLogDAO tourLogDao;
    private BusinessLogic bl;

    @Before
    public void init() {
        tourDao = mock(TourDAO.class);
        tourLogDao = mock(TourLogDAO.class);
        bl = new SimpleBusinessLogic(tourDao, tourLogDao);
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

    private Tour getBadTour() {
        return new Tour(
                "Tourname",
                "Description",
                "",
                "To",
                0,
                0,
                "Transport Type",
                "Route Info",
                ""
        );
    }

    private TourLog getTourLog() {
        return new TourLog(
                "2022-05-06T12:13:14",
                "Some comment",
                2,
                2001,
                4
        );
    }

    private TourLog getBadTourLog() {
        return new TourLog(
                "2022-05-06T12:13:14",
                "Some comment",
                2,
                2001,
                0
        );
    }

    @Test
    public void testAddTour() {
        Tour tour = getTour();

        try {
            bl.addTour(tour);
        } catch (Exception e) {
            fail();
        }

        try {
            verify(tourDao).create(tour);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddTourFailDAO() {
        Tour tour = getTour();
        try {
            doThrow(new Exception()).when(tourDao).create(tour);
        } catch (Exception e) {
            fail();
        }

        try {
            bl.addTour(tour);
            fail();
        } catch (Exception e) {

        }

        try {
            verify(tourDao).create(tour);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddTourFailVerify() {
        Tour tour = getBadTour();

        try {
            bl.addTour(tour);
            fail();
        } catch (IllegalArgumentException e) {
            //assertTrue(true);
        } catch (Exception e) {
            fail();
        }

        try {
            verify(tourDao, times(0)).create(tour);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testDeleteTour() {
        String tourname = "Tourname";

        try {
            bl.deleteTour(tourname);
        } catch (Exception e) {
            fail();
        }

        try {
            verify(tourDao).delete(tourname);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testDeleteTourFailDAO() {
        String tourname = "Tourname";
        try {
            doThrow(new Exception()).when(tourDao).delete(tourname);
        } catch (Exception e) {
            fail();
        }

        try {
            bl.deleteTour(tourname);
            fail();
        } catch (Exception e) {

        }

        try {
            verify(tourDao).delete(tourname);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testDeleteTourFailVerify() {
        String tourname = "";

        try {
            bl.deleteTour(tourname);
            fail();
        } catch (IllegalArgumentException e) {
            //assertTrue(true);
        } catch (Exception e) {
            fail();
        }

        try {
            verify(tourDao, times(0)).delete(tourname);
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testEditTour(){
        Tour tour = getTour();
        String tourname = "Tourname";

        try {
            bl.editTour(tourname, tour);
        } catch (Exception e) {
            fail();
        }

        try {
            verify(tourDao).update(tourname, tour);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testEditTourFailDAO(){
        String tourname = "Tourname";
        Tour tour = getTour();
        try {
            doThrow(new Exception()).when(tourDao).update(tourname, tour);
        } catch (Exception e) {
            fail();
        }

        try {
            bl.editTour(tourname, tour);
            fail();
        } catch (Exception e) {

        }

        try {
            verify(tourDao).update(tourname, tour);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testEditTourFailVerify() {
        String tourname = "";
        Tour tour = getBadTour();
        try {
            bl.editTour(tourname, tour);
            fail();
        } catch (IllegalArgumentException e) {
            //assertTrue(true);
        } catch (Exception e) {
            fail();
        }

        try {
            verify(tourDao, times(0)).update(tourname, tour);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testGetTour() {
        String tourname = "Tourname";
        Tour expectedTour = getTour();
        try {
            when(tourDao.read(tourname)).thenReturn(expectedTour);
        } catch (Exception e) {
            fail();
        }

        Tour tour = null;
        try {
            tour = bl.getTour(tourname);
        } catch (Exception e) {
            fail();
        }

        try {
            verify(tourDao).read(tourname);
        } catch (Exception e) {
            fail();
        }
        assertEquals(expectedTour, tour);
    }

    @Test
    public void testGetTourFailDAO(){
        String tourname = "Tourname";

        try {
            doThrow(new Exception()).when(tourDao).read(tourname);
        } catch (Exception e) {
            fail();
        }

        Tour tour = null;
        try {
            tour = bl.getTour(tourname);
            fail();
        } catch (Exception e) {

        }

        try {
            verify(tourDao).read(tourname);
        } catch (Exception e) {
            fail();
        }
        assertNull(tour);
    }

    @Test
    public void testGetTourFailVerify() {
        String tourname = "";

        Tour tour = null;
        try {
            tour = bl.getTour(tourname);
            fail();
        } catch (IllegalArgumentException e) {
            //assertTrue(true);
        } catch (Exception e) {
            fail();
        }

        try {
            verify(tourDao, times(0)).read(tourname);
        } catch (Exception e) {
            fail();
        }
        assertNull(tour);
    }

    @Test
    public void testGetTourNames() {
        String[] expectedTourNames = new String[] {"Tour1", "Tour2", "Tour3"};
        try {
            when(tourDao.readTourNames()).thenReturn(expectedTourNames);
        } catch (Exception e) {
            fail();
        }

        String[] tournames = null;
        try {
            tournames = bl.getTourNames();
        } catch (Exception e) {
            fail();
        }

        try {
            verify(tourDao).readTourNames();
        } catch (Exception e) {
            fail();
        }
        assertArrayEquals(expectedTourNames, tournames);
    }

    @Test
    public void testGetTourNamesFailDAO(){
        try {
            when(tourDao.readTourNames()).thenThrow(new Exception());
        } catch (Exception e) {
            fail();
        }

        try {
            bl.getTourNames();
            fail();
        } catch (Exception e) {
            //assertTrue
        }

        try {
            verify(tourDao).readTourNames();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testGetTourWithImage() {
        String tourname = "Tourname";
        Tour expectedTour = getTour();
        expectedTour.setDistance(10.5);
        expectedTour.setEstTime(1002);
        try {
            when(tourDao.readTourWithImage(tourname)).thenReturn(expectedTour);
        } catch (Exception e) {
            fail();
        }

        Tour tour = null;
        try {
            tour = bl.getTourWithImage(tourname);
        } catch (Exception e) {
            fail();
        }

        try {
            verify(tourDao).readTourWithImage(tourname);
        } catch (Exception e) {
            fail();
        }
        assertEquals(expectedTour, tour);
    }

    @Test
    public void testGetTourWithImageFailDAO(){
        String tourname = "Tourname";

        try {
            doThrow(new Exception()).when(tourDao).readTourWithImage(tourname);
        } catch (Exception e) {
            fail();
        }

        Tour tour = null;
        try {
            tour = bl.getTourWithImage(tourname);
            fail();
        } catch (Exception e) {

        }

        try {
            verify(tourDao).readTourWithImage(tourname);
        } catch (Exception e) {
            fail();
        }
        assertNull(tour);
    }

    @Test
    public void testGetTourWithImageFailVerify() {
        String tourname = "";

        Tour tour = null;
        try {
            tour = bl.getTourWithImage(tourname);
            fail();
        } catch (IllegalArgumentException e) {
            //assertTrue(true);
        } catch (Exception e) {
            fail();
        }

        try {
            verify(tourDao, times(0)).readTourWithImage(tourname);
        } catch (Exception e) {
            fail();
        }
        assertNull(tour);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testAddTourLog() {
        String tourname = "Tourname";
        TourLog tourLog = getTourLog();

        try {
            bl.addTourLog(tourname, tourLog);
        } catch (Exception e) {
            fail();
        }

        try {
            verify(tourLogDao).create(tourname, tourLog);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddTourLogFailDAO() {
        String tourname = "Tourname";
        TourLog tourLog = getTourLog();
        try {
            doThrow(new Exception()).when(tourLogDao).create(tourname, tourLog);
        } catch (Exception e) {
            fail();
        }

        try {
            bl.addTourLog(tourname, tourLog);
            fail();
        } catch (Exception e) {

        }

        try {
            verify(tourLogDao).create(tourname, tourLog);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddTourLogFailVerify() {
        String tourname = "Tourname";
        TourLog tourLog = getBadTourLog();

        try {
            bl.addTourLog(tourname, tourLog);
            fail();
        } catch (IllegalArgumentException e) {
            //assertTrue(true);
        } catch (Exception e) {
            fail();
        }

        try {
            verify(tourLogDao, times(0)).create(tourname, tourLog);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testDeleteTourLog() {
        int id = 1001;

        try {
            bl.deleteTourLog(id);
        } catch (Exception e) {
            fail();
        }

        try {
            verify(tourLogDao).delete(id);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testDeleteTourLogFailDAO() {
        int id = 1001;
        try {
            doThrow(new Exception()).when(tourLogDao).delete(id);
        } catch (Exception e) {
            fail();
        }

        try {
            bl.deleteTourLog(id);
            fail();
        } catch (Exception e) {

        }

        try {
            verify(tourLogDao).delete(id);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testEditTourLog(){
        TourLog tourLog = getTourLog();
        int id = 1001;

        try {
            bl.editTourLog(id, tourLog);
        } catch (Exception e) {
            fail();
        }

        try {
            verify(tourLogDao).update(id, tourLog);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testEditTourLogFailDAO(){
        int id = 1001;
        TourLog tourLog = getTourLog();
        try {
            doThrow(new Exception()).when(tourLogDao).update(id, tourLog);
        } catch (Exception e) {
            fail();
        }

        try {
            bl.editTourLog(id, tourLog);
            fail();
        } catch (Exception e) {

        }

        try {
            verify(tourLogDao).update(id, tourLog);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testEditTourLogFailVerify() {
        int id = 1001;
        TourLog tourLog = getBadTourLog();
        try {
            bl.editTourLog(id, tourLog);
            fail();
        } catch (IllegalArgumentException e) {
            //assertTrue(true);
        } catch (Exception e) {
            fail();
        }

        try {
            verify(tourLogDao, times(0)).update(id, tourLog);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testGetTourLog() {
        int id = 1001;
        TourLog expectedTourLog = getTourLog();
        try {
            when(tourLogDao.read(id)).thenReturn(expectedTourLog);
        } catch (Exception e) {
            fail();
        }

        TourLog tourLog = null;
        try {
            tourLog = bl.getTourLog(id);
        } catch (Exception e) {
            fail();
        }

        try {
            verify(tourLogDao).read(id);
        } catch (Exception e) {
            fail();
        }
        assertEquals(expectedTourLog, tourLog);
    }

    @Test
    public void testGetTourLogFailDAO(){
        int id = 1001;

        try {
            doThrow(new Exception()).when(tourLogDao).read(id);
        } catch (Exception e) {
            fail();
        }

        TourLog tourLog = null;
        try {
            tourLog = bl.getTourLog(id);
            fail();
        } catch (Exception e) {

        }

        try {
            verify(tourLogDao).read(id);
        } catch (Exception e) {
            fail();
        }
        assertNull(tourLog);
    }
}

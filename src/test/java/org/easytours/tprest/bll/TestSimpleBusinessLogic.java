package org.easytours.tprest.bll;

import org.easytours.tpmodel.Tour;
import org.easytours.tprest.dal.dao.TourDAO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestSimpleBusinessLogic {
    private TourDAO tourDao;
    private BusinessLogic bl;

    @Before
    public void init() {
        tourDao = mock(TourDAO.class);
        bl = new SimpleBusinessLogic(tourDao);
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
}

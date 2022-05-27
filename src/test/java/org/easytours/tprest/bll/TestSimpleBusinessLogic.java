package org.easytours.tprest.bll;

import org.easytours.tpmodel.Tour;
import org.easytours.tprest.dal.dao.TourDAO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;
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
                10.5,
                4200,
                "Transport Type",
                "Route Info"
        );
    }

    private Tour getBadTour() {
        return new Tour(
                "Tourname",
                "Description",
                "From",
                "To",
                -10.5,
                4200,
                "Transport Type",
                "Route Info"
        );
    }

    @Test
    public void testAddTour() {
        Tour tour = getTour();

        bl.addTour(tour);

        verify(tourDao).create(tour);
    }

    @Test
    public void testAddTourFailDAO() {
        Tour tour = getTour();
        doThrow(new IllegalArgumentException()).when(tourDao).create(tour);

        try {
            bl.addTour(tour);
            fail();
        } catch (IllegalArgumentException e) { // other Exception?
            //assertTrue(true);
        }

        verify(tourDao).create(tour);
    }

    @Test
    public void testAddTourFailVerify() {
        Tour tour = getBadTour();

        try {
            bl.addTour(tour);
            fail();
        } catch (IllegalArgumentException e) {
            //assertTrue(true);
        }

        verify(tourDao, times(0)).create(tour);
    }
}

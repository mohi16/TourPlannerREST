package org.easytours.tprest.bll;

import org.easytours.tpmodel.Tour;
import org.easytours.tprest.dal.dao.TourDAO;

public class SimpleBusinessLogic implements BusinessLogic {
    TourDAO tourDao;

    public SimpleBusinessLogic(TourDAO tourDao) {
        this.tourDao = tourDao;
    }

    @Override
    public void addTour(Tour tour) throws Exception {
        if (!tour.isValid()) {
            throw new IllegalArgumentException("The tour is not valid");
        }

        tourDao.create(tour);
    }

    @Override
    public void deleteTour(String name) throws Exception {
        if (null == name || name.isEmpty()) {
            throw new IllegalArgumentException("The name is null or not valid");
        }

        tourDao.delete(name);
    }

    @Override
    public void editTour(String name, Tour newTour) throws Exception {
        if(null == name || name.isEmpty() || !newTour.isValid()){
            throw new IllegalArgumentException("The name or the tour is not valid");
        }
        tourDao.update(name, newTour);
    }

    @Override
    public Tour getTour(String name) throws Exception {
        if (null == name || name.isEmpty()) {
            throw new IllegalArgumentException("The name is null or not valid");
        }

        return tourDao.read(name);
    }
}

package org.easytours.tprest.bll;

import org.easytours.tpmodel.Tour;
import org.easytours.tprest.dal.dao.TourDAO;

public class SimpleBusinessLogic implements BusinessLogic {
    TourDAO tourDao;

    public SimpleBusinessLogic(TourDAO tourDao) {
        this.tourDao = tourDao;
    }

    @Override
    public void addTour(Tour tour) {

    }

    @Override
    public void deleteTour(String name) {

    }

    @Override
    public void editTour(String name, Tour newTour) {

    }

    @Override
    public Tour getTour(String name) {
        return null;
    }
}

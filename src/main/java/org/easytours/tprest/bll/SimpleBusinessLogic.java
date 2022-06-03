package org.easytours.tprest.bll;

import org.easytours.tpmodel.Tour;
import org.easytours.tpmodel.TourLog;
import org.easytours.tprest.dal.dao.TourDAO;
import org.easytours.tprest.dal.dao.TourLogDAO;
import org.easytours.tprest.utils.Pair;

public class SimpleBusinessLogic implements BusinessLogic {
    TourDAO tourDao;
    TourLogDAO tourLogDao;

    public SimpleBusinessLogic(TourDAO tourDao, TourLogDAO tourLogDao) {
        this.tourDao = tourDao;
        this.tourLogDao = tourLogDao;
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

    @Override
    public String[] getTourNames() throws Exception {
        return tourDao.readTourNames();
    }

    @Override
    public Tour getTourWithImage(String name) throws Exception {
        if (null == name || name.isEmpty()) {
            throw new IllegalArgumentException("The name is null or not valid");
        }

        return tourDao.readTourWithImage(name);
    }

    @Override
    public void addTourLog(String tourName, TourLog tourLog) throws Exception {
        if (!tourLog.isValid() || null == tourName || tourName.isEmpty()) {
            throw new IllegalArgumentException("The tour is not valid");
        }

        tourLogDao.create(tourName, tourLog);
    }

    @Override
    public void editTourLog(int id, TourLog newLog) throws Exception {
        if(!newLog.isValid()){
            throw new IllegalArgumentException("The tour is not valid");
        }
        tourLogDao.update(id, newLog);
    }

    @Override
    public void deleteTourLog(int id) throws Exception {
        tourLogDao.delete(id);
    }

    @Override
    public TourLog getTourLog(int id) throws Exception {
        return tourLogDao.read(id);
    }
}

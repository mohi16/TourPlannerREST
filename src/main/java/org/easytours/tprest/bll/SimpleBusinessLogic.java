package org.easytours.tprest.bll;

import org.easytours.tpmodel.Tour;
import org.easytours.tpmodel.TourLog;
import org.easytours.tprest.dal.dao.TourDAO;
import org.easytours.tprest.dal.dao.TourLogDAO;
import org.easytours.tprest.utils.Pair;

import java.util.Locale;

public class SimpleBusinessLogic implements BusinessLogic {
    TourDAO tourDao;
    TourLogDAO tourLogDao;
    ReportGenerator reportGenerator;

    public SimpleBusinessLogic(TourDAO tourDao, TourLogDAO tourLogDao, ReportGenerator reportGenerator) {
        this.tourDao = tourDao;
        this.tourLogDao = tourLogDao;
        this.reportGenerator = reportGenerator;
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
    public int addTourLog(String tourName, TourLog tourLog) throws Exception {
        if (!tourLog.isValid() || null == tourName || tourName.isEmpty()) {
            throw new IllegalArgumentException("The tour is not valid");
        }

        return tourLogDao.create(tourName, tourLog);
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

    @Override
    public byte[] generateSingleReport(String tourName, Locale locale) throws Exception {
        if(null == tourName || tourName.isEmpty()) {
            throw new IllegalArgumentException("no tourname!");
        }

        Tour tour = tourDao.readTourWithImage(tourName);
        return reportGenerator.singleReport(tour, locale);
    }

    @Override
    public byte[] generateSummaryReport(Locale locale) throws Exception {
        Tour[] tours = tourDao.readAll();
        return reportGenerator.summaryReport(tours, locale);
    }

    @Override
    public void importTours(Tour[] tours) throws Exception {
        tourDao.createAll(tours);
    }

    @Override
    public Tour[] exportTours() throws Exception {
        return tourDao.readAll();
    }

    @Override
    public String[] getTourNames(String filter) throws Exception {
        if (null == filter) {
            return tourDao.readTourNames();
        } else {
            return tourDao.readTourNames(filter);
        }
    }
}

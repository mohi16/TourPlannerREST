package org.easytours.tprest.bll;

import org.easytours.tpmodel.Tour;
import org.easytours.tpmodel.TourLog;
import org.easytours.tprest.utils.Pair;

public interface BusinessLogic {
    //Tour
    void addTour(Tour tour) throws Exception;
    void deleteTour(String name) throws Exception;
    void editTour(String name, Tour newTour) throws Exception;
    Tour getTour(String name) throws Exception;
    String[] getTourNames() throws Exception;
    Tour getTourWithImage(String name) throws Exception;

    //TourLog
    void addTourLog(String tourName, TourLog tourLog) throws Exception;
    void editTourLog(int id, TourLog newLog) throws Exception;
    void deleteTourLog(int id) throws Exception;
    TourLog getTourLog(int id) throws Exception;

}

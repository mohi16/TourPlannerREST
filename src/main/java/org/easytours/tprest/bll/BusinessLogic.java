package org.easytours.tprest.bll;

import org.easytours.tpmodel.Tour;

public interface BusinessLogic {
    void addTour(Tour tour) throws Exception;
    void deleteTour(String name) throws Exception;
    void editTour(String name, Tour newTour) throws Exception;
    Tour getTour(String name) throws Exception;
}

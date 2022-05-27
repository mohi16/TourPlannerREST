package org.easytours.tprest.bll;

import org.easytours.tpmodel.Tour;

public interface BusinessLogic {
    void addTour(Tour tour);
    void deleteTour(String name);
    void editTour(String name, Tour newTour);
    Tour getTour(String name);
}

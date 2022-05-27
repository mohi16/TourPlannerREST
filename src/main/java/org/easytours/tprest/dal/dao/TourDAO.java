package org.easytours.tprest.dal.dao;

import org.easytours.tpmodel.Tour;

public interface TourDAO {
    void create(Tour tour);
    Tour read(String name);
    void update(String name, Tour newTour);
    void delete(String name);
}

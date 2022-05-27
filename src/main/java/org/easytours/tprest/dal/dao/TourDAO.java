package org.easytours.tprest.dal.dao;

import org.easytours.tpmodel.Tour;

import java.sql.SQLException;

public interface TourDAO {
    void create(Tour tour) throws Exception;
    Tour read(String name);
    void update(String name, Tour newTour);
    void delete(String name);
}

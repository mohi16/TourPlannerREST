package org.easytours.tprest.dal.dao;

import org.easytours.tpmodel.Tour;

import java.sql.SQLException;

public interface TourDAO {
    void create(Tour tour) throws Exception;
    Tour read(String name) throws Exception;
    void update(String name, Tour newTour) throws Exception;
    void delete(String name) throws Exception;
}

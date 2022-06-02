package org.easytours.tprest.dal.dao;

import org.easytours.tpmodel.Tour;
import org.easytours.tprest.utils.Pair;

import java.sql.SQLException;

public interface TourDAO {
    void create(Tour tour) throws Exception;
    Tour read(String name) throws Exception;
    void update(String name, Tour newTour) throws Exception;
    void delete(String name) throws Exception;
    String[] readTourNames() throws Exception;
    Tour readTourWithImage(String name) throws Exception;
}

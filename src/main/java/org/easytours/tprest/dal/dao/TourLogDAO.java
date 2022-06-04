package org.easytours.tprest.dal.dao;

import org.easytours.tpmodel.TourLog;

public interface TourLogDAO {
    int create(String tourName, TourLog tourLog) throws Exception;
    TourLog read(int id) throws Exception;
    void update(int id, TourLog newLog) throws Exception;
    void delete(int id) throws Exception;
    TourLog[] readAll(String tourName) throws Exception;
}

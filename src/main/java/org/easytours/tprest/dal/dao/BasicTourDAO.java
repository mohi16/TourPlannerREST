package org.easytours.tprest.dal.dao;

import org.easytours.tpmodel.Tour;
import org.easytours.tprest.dal.Database;

import java.sql.Connection;

public class BasicTourDAO implements TourDAO {
    Database db;

    public BasicTourDAO(Database db) {
        this.db = db;
    }

    @Override
    public void create(Tour tour) {

    }

    @Override
    public Tour read(String name) {
        return null;
    }

    @Override
    public void update(String name, Tour newTour) {

    }

    @Override
    public void delete(String name) {

    }
}

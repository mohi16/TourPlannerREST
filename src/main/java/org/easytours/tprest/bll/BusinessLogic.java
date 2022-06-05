package org.easytours.tprest.bll;

import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.kernel.pdf.PdfDocument;
import org.easytours.tpmodel.Tour;
import org.easytours.tpmodel.TourLog;
import org.easytours.tprest.utils.Pair;

import java.util.Locale;

public interface BusinessLogic {
    //Tour
    void addTour(Tour tour) throws Exception;
    void deleteTour(String name) throws Exception;
    void editTour(String name, Tour newTour) throws Exception;
    Tour getTour(String name) throws Exception;
    String[] getTourNames() throws Exception;
    Tour getTourWithImage(String name) throws Exception;

    //TourLog
    int addTourLog(String tourName, TourLog tourLog) throws Exception;
    void editTourLog(int id, TourLog newLog) throws Exception;
    void deleteTourLog(int id) throws Exception;
    TourLog getTourLog(int id) throws Exception;

    //Report
    byte[] generateSingleReport(String tourName, Locale locale) throws Exception;
    byte[] generateSummaryReport(Locale locale) throws Exception;

    void importTours(Tour[] tours) throws Exception;
    Tour[] exportTours() throws Exception;

    String[] getTourNames(String filter) throws Exception;

}

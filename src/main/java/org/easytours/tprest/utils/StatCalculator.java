package org.easytours.tprest.utils;

import org.easytours.tpmodel.Tour;
import org.easytours.tpmodel.TourLog;
import org.easytours.tpmodel.utils.TimeUtils;
import org.easytours.tpmodel.utils.Triple;

public class StatCalculator {
    public String averageTime(Tour tour) {
        long sum = 0;
        int count = 0;
        for (TourLog t : tour.getTourLogs()) {
            sum += t.getTotalTime();
            ++count;
        }

        if (0 == count) {
            return "N/A";
        }
        long avg = sum / count;

        Triple<Integer, Integer, Integer> time = TimeUtils.deconstructTime(avg);
        return String.format("%02d:%02d:%02d", time.getValue1(), time.getValue2(), time.getValue3());
    }

    public String rating(Tour tour) {
        int sum = 0;
        int count = 0;
        for (TourLog t : tour.getTourLogs()) {
            sum += t.getRating();
            ++count;
        }

        if (0 == count) {
            return "N/A";
        }
        int avg = sum / count;

        return String.valueOf(avg);
    }
}

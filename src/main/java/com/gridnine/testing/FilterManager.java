package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Модуль, который занимается фильтрацией набора перелётов согласно различным правилам
 */
public class FilterManager {

    /**
     * Получить отфильтрованный набор перелетов
     *
     * @param flights Набор перелетов
     * @param filters Набор фильтров (правил), по которым нужно фильтровать
     * @return Отфильтрованный набор перелетов
     */
    static List<Flight> getFiltered(List<Flight> flights, List<Filter> filters) {
        List<Flight> result = flights;

        for (Filter filter : filters) {
            result = switch (filter) {
                case DEPARTURE_BEFORE_NOW -> filterDepartureBeforeNow(result);
                case ARRIVAL_BEFORE_DEPARTURE -> filterArrivalBeforeDeparture(result);
                case TOTAL_SURFACE_HOURS_MORE_TWO -> filterTotalSurfaceHoursMoreTwo(result);
            };
        }

        return result;
    }

    static private List<Flight> filterDepartureBeforeNow(List<Flight> flights) {
        LocalDateTime now = LocalDateTime.now();
        ArrayList<Flight> result = new ArrayList<>();
        for (Flight flight : flights) {
            List<Segment> segments = flight.getSegments();
            if (segments.isEmpty()) {
                result.add(flight);
                continue;
            }

            Segment segment = segments.get(0);
            if (!segment.getDepartureDate().isBefore(now)) {
                result.add(flight);
            }
        }
        return result;
    }

    static private List<Flight> filterArrivalBeforeDeparture(List<Flight> flights) {
        ArrayList<Flight> result = new ArrayList<>();
        for (Flight flight : flights) {
            List<Segment> segments = flight.getSegments();
            boolean ok = true;
            for (Segment segment : segments) {
                if (segment.getArrivalDate().isBefore(segment.getDepartureDate())) {
                    ok = false;
                    break;
                }
            }

            if (ok) {
                result.add(flight);
            }
        }

        return result;
    }

    static private List<Flight> filterTotalSurfaceHoursMoreTwo(List<Flight> flights) {
        ArrayList<Flight> result = new ArrayList<>();
        for (Flight flight : flights) {
            List<Segment> segments = flight.getSegments();
            long totalSurfaceSeconds = 0;
            LocalDateTime lastArrival = null;
            for (Segment segment : segments) {
                if (lastArrival != null) {
                    totalSurfaceSeconds += Duration.between(lastArrival, segment.getDepartureDate()).getSeconds();
                }

                lastArrival = segment.getArrivalDate();
            }

            if (totalSurfaceSeconds <= 2 * 60 * 60) {
                result.add(flight);
            }
        }

        return result;
    }

    /**
     * Типы фильтров
     */
    public enum Filter {
        /**
         * Вылет до текущего момента времени
         */
        DEPARTURE_BEFORE_NOW,

        /**
         * Сегменты с датой прилёта раньше даты вылета
         */
        ARRIVAL_BEFORE_DEPARTURE,

        /**
         * Перелеты, где общее время, проведённое на земле, превышает два часа (время на земле — это интервал между прилётом одного сегмента и вылетом следующего за ним)
         */
        TOTAL_SURFACE_HOURS_MORE_TWO
    }
}

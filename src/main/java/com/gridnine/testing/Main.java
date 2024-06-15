package com.gridnine.testing;

import java.util.List;

import static com.gridnine.testing.FilterManager.Filter.*;

public class Main {
    public static void main(String[] args) {
        List<Flight> flights = FlightBuilder.createFlights();
        System.out.println("Начальный набор:");
        System.out.println(flights);
        System.out.println();

        List<Flight> filteredDepartureBeforeNow = FilterManager.getFiltered(flights, List.of(DEPARTURE_BEFORE_NOW));
        System.out.println("Отфильтрованы вылет до текущего момента времени:");
        System.out.println(filteredDepartureBeforeNow);
        System.out.println();

        List<Flight> filteredArrivalBeforeDeparture = FilterManager.getFiltered(flights, List.of(ARRIVAL_BEFORE_DEPARTURE));
        System.out.println("Отфильтрованы сегменты с датой прилёта раньше даты вылета:");
        System.out.println(filteredArrivalBeforeDeparture);
        System.out.println();

        List<Flight> filteredTotalSurfaceHoursMoreTwo = FilterManager.getFiltered(flights, List.of(TOTAL_SURFACE_HOURS_MORE_TWO));
        System.out.println("Отфильтрованы перелеты, где общее время, проведённое на земле, превышает два часа:");
        System.out.println(filteredTotalSurfaceHoursMoreTwo);
        System.out.println();

        List<Flight> filteredAll = FilterManager.getFiltered(flights, List.of(DEPARTURE_BEFORE_NOW, ARRIVAL_BEFORE_DEPARTURE, TOTAL_SURFACE_HOURS_MORE_TWO));
        System.out.println("Отфильтрованы тремя фильтрами:");
        System.out.println(filteredAll);
        System.out.println();
    }
}
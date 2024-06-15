package com.gridnine.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.gridnine.testing.FilterManager.Filter.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilterManagerTest {

    private Flight aNormalFlight;
    private Flight aFlightDepartingInThePast;
    private Flight aFlightThatDepartsBeforeItArrives;
    private Flight aFlightWithMoreThanTwoSurfaceHours;
    List<Flight> list;

    @BeforeEach
    void setUp() {
        LocalDateTime threeDaysFromNow = LocalDateTime.now().plusDays(3);

        aNormalFlight = new Flight(List.of(new Segment(threeDaysFromNow, threeDaysFromNow.plusHours(2)), new Segment(threeDaysFromNow.plusHours(3), threeDaysFromNow.plusHours(5))));
        aFlightDepartingInThePast = new Flight(List.of(new Segment(threeDaysFromNow.minusDays(6), threeDaysFromNow)));
        aFlightThatDepartsBeforeItArrives = new Flight(List.of(new Segment(threeDaysFromNow, threeDaysFromNow.minusHours(6))));
        aFlightWithMoreThanTwoSurfaceHours = new Flight(List.of(new Segment(threeDaysFromNow, threeDaysFromNow.plusHours(2)), new Segment(threeDaysFromNow.plusHours(5), threeDaysFromNow.plusHours(6))));

        list = List.of(aNormalFlight, aFlightDepartingInThePast, aFlightThatDepartsBeforeItArrives, aFlightWithMoreThanTwoSurfaceHours);
    }

    @Test
    void shouldFilterFlightDepartingInThePast() {
        List<Flight> filtered = FilterManager.getFiltered(list, List.of(DEPARTURE_BEFORE_NOW));
        assertEquals(3, filtered.size());
        assertEquals(aNormalFlight, filtered.get(0));
        assertEquals(aFlightThatDepartsBeforeItArrives, filtered.get(1));
        assertEquals(aFlightWithMoreThanTwoSurfaceHours, filtered.get(2));
    }

    @Test
    void shouldFilterFlightThatDepartsBeforeItArrives() {
        List<Flight> filtered = FilterManager.getFiltered(list, List.of(ARRIVAL_BEFORE_DEPARTURE));
        assertEquals(3, filtered.size());
        assertEquals(aNormalFlight, filtered.get(0));
        assertEquals(aFlightDepartingInThePast, filtered.get(1));
        assertEquals(aFlightWithMoreThanTwoSurfaceHours, filtered.get(2));
    }

    @Test
    void shouldFilterFlightWithMoreThanTwoHoursGroundTime() {
        List<Flight> filtered = FilterManager.getFiltered(list, List.of(TOTAL_SURFACE_HOURS_MORE_TWO));
        assertEquals(3, filtered.size());
        assertEquals(aNormalFlight, filtered.get(0));
        assertEquals(aFlightDepartingInThePast, filtered.get(1));
        assertEquals(aFlightThatDepartsBeforeItArrives, filtered.get(2));
    }

    @Test
    void shouldFilterAllExceptNormal() {
        List<Flight> filtered = FilterManager.getFiltered(list, List.of(DEPARTURE_BEFORE_NOW, ARRIVAL_BEFORE_DEPARTURE, TOTAL_SURFACE_HOURS_MORE_TWO));
        assertEquals(1, filtered.size());
        assertEquals(aNormalFlight, filtered.get(0));
    }
}
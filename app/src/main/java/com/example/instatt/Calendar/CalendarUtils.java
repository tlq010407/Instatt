package com.example.instatt.Calendar;

import com.example.instatt.Class.Event;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarUtils {
    public static LocalDate selectedDate; // Store the currently selected date
    public static ArrayList<Event> eventsList = new ArrayList<>(); // Store a list of events

    // Format a LocalTime object as a string in "hh:mm" format
    public static String formattedTime(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm"); // Use "HH" for 24-hour format
        return time.format(formatter);
    }

    // Format a LocalDate object as a string in "MMMM yyyy" format (e.g., "January 2023")
    public static String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    // Generate an ArrayList of LocalDates for the days in the month of the given date
    public static ArrayList<LocalDate> daysInMonthArray(LocalDate date) {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = date.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek)
                daysInMonthArray.add(null); // Add null for days outside the current month
            else
                daysInMonthArray.add(LocalDate.of(date.getYear(), date.getMonth(), i - dayOfWeek));
            // Add the LocalDate for days within the current month
        }
        return daysInMonthArray;
    }

    // Retrieve events for a specific date from the eventsList
    public static ArrayList<Event> getEventsForDate(LocalDate date) {
        ArrayList<Event> eventsOnDate = new ArrayList<>();
        for (Event event : eventsList) {
            if (event.getDate().equals(date)) {
                eventsOnDate.add(event); // Add events that match the specified date
            }
        }
        return eventsOnDate;
    }

    // Generate an ArrayList of LocalDates for the days in the week containing the given date
    public static ArrayList<LocalDate> daysInWeekArray(LocalDate date) {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = sundayForDate(date); // Find the Sunday of the week
        LocalDate endDate = current.plusWeeks(1);
        while (current.isBefore(endDate)) {
            days.add(current); // Add each day of the week to the list
            current = current.plusDays(1);
        }
        return days;
    }

    // Find the Sunday of the week containing the given date
    private static LocalDate sundayForDate(LocalDate date) {
        LocalDate oneWeekAgo = date.minusWeeks(1);
        LocalDate current = date;
        while (current.isAfter(oneWeekAgo)) {
            if (current.getDayOfWeek() == DayOfWeek.SUNDAY)
                return current; // Return the first Sunday found in the week
            current = current.minusDays(1);
        }
        return null; // If no Sunday found (this should never happen), null is returned
    }
}


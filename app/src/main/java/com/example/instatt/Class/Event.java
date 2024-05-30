package com.example.instatt.Class;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event {
    // Static list to store all events
    public static ArrayList<Event> eventsList = new ArrayList<>();

    // Method to filter and retrieve events for a specific date
    public static ArrayList<Event> eventsForDate(LocalDate date) {
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : eventsList) {
            // Check if the event's date matches the specified date
            if (event.getDate().equals(date)) {
                events.add(event);
            }
        }
        return events;
    }

    // Event properties
    private String type;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String Room;
    private String Status;
    private String name;

    // Constructor to initialize an Event object
    public Event(String name, String type, LocalDate date, LocalTime startTime, LocalTime endTime, String room, String status) {
        this.type = type;
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        Room = room;
        Status = status;
    }

    // Getter and setter methods for event properties
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}

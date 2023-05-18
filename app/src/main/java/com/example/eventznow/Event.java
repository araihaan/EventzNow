package com.example.eventznow;

public class Event {
    private String eventID;
    private String eventname;
    private String date;
    private String time;
    private String location;
    private String slot;
    private String price;

    public Event() {
        // Default constructor required for Firebase
    }

    public Event(String eventID, String eventname, String date, String time, String location, String slot, String price) {
        this.eventID = eventID;
        this.eventname = eventname;
        this.date = date;
        this.time = time;
        this.location = location;
        this.slot = slot;
        this.price = price;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

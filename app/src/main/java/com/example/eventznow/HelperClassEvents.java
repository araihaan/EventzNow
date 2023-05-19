package com.example.eventznow;

import java.util.List;

public class HelperClassEvents {
    String eventID, eventname, date, time, location, slot, price;
    List<String> joinedUsersList;
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
    public List<String> getJoinedUsersList() {
        return joinedUsersList;
    }

    public void setJoinedUsersList(List<String> joinedUsersList) {
        this.joinedUsersList = joinedUsersList;
    }
    public HelperClassEvents(String eventID, String eventname, String date, String time, String location, String slot, String price, List<String> joinedUsersList) {
        this.eventID = eventID;
        this.eventname = eventname;
        this.date = date;
        this.time = time;
        this.location = location;
        this.slot = slot;
        this.price = price;
        this.joinedUsersList = joinedUsersList;
    }
    public HelperClassEvents() {
    }
}
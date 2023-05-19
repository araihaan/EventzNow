package com.example.eventznow;

import java.util.List;

public class HelperEventOrder {
    String eventID, orderID, payment, amount, totalpay;
    List<String> joinedUsersList;
    public String getEventID() {
        return eventID;
    }
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
    public String getOrderID() {
        return orderID;
    }
    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
    public String getPayment() {
        return payment;
    }
    public void setPayment(String payment) {
        this.payment = payment;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getTotalpay() {
        return totalpay;
    }
    public void setTotalpay(String totalpay) {
        this.totalpay = totalpay;
    }
    public List<String> getJoinedUsersList() {
        return joinedUsersList;
    }

    public void setJoinedUsersList(List<String> joinedUsersList) {
        this.joinedUsersList = joinedUsersList;
    }
    public HelperEventOrder(String eventID, String orderID, String payment, String amount, String totalpay, List<String> joinedUsersList) {
        this.eventID = eventID;
        this.orderID = orderID;
        this.payment = payment;
        this.amount = amount;
        this.totalpay = totalpay;
        this.joinedUsersList = joinedUsersList;
    }
    public HelperEventOrder() {
    }
}
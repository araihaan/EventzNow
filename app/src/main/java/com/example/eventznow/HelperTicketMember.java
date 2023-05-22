    package com.example.eventznow;

    import java.util.List;

    public class HelperTicketMember {
        String eventname, status;
        public String getEventname() {
            return eventname;
        }
        public void setEventname(String eventname) {
            this.eventname = eventname;
        }
        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public HelperTicketMember(String eventID, String orderID) {
            this.eventname = eventname;
            this.status = status;
        }
        public HelperTicketMember() {
        }
    }
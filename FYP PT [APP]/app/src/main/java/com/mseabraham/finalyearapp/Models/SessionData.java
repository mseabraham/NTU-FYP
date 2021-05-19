package com.mseabraham.finalyearapp.Models;

import java.util.List;

public class SessionData {
    public List<Booking> bookings;

    public  SessionData(){}

    public SessionData (List<Booking> bookings){
        this.bookings = bookings;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}

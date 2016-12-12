package com.privilist.model;

import java.util.ArrayList;

/**
 * Created by SonH on 2015-07-02.
 */
public class Booking {
    private long booking_id;
    private String type;
    private String code;
    private String booking_date;
    private String name;
    private boolean expired;
    private Table table;
    private Event event;
    private ArrayList<Details> details;
    private String currency;
    private float total;
    // add for Add Booking result
    private Venue venue;

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public boolean isExpired() {
        return expired;
    }

    public ArrayList<Details> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<Details> details) {
        this.details = details;
    }

    public long getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(long booking_id) {
        this.booking_id = booking_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public static class Table extends com.privilist.model.Table {
        private Venue venue;

        public Venue getVenue() {
            return venue;
        }

        public void setVenue(Venue venue) {
            this.venue = venue;
        }
    }

    public static class Details{
        private int quantity;
        private Drink drink;

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public Drink getDrink() {
            return drink;
        }

        public void setDrink(Drink drink) {
            this.drink = drink;
        }
    }
}

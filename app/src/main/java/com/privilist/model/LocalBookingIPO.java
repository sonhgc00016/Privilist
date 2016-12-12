package com.privilist.model;

import org.joda.time.LocalDate;

import java.util.ArrayList;

/**
 * Created by minhtdh on 6/29/15.
 */
public class LocalBookingIPO {
    public Venue venue;
    public LocalDate date;
    public Table table;
    public long eventID;
    public ArrayList<Drink> drinks;
    public double total;
}

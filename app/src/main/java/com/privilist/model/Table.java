package com.privilist.model;

import java.io.Serializable;

/**
 * Created by minhtdh on 6/17/15.
 */
public class Table implements Serializable {
    public long id;
    public long venue_id;
    public String table_type;
    public int table_count;
    public String table_title;
    public String description;
    public int people;
    public String price;
    public int table_left;
    public boolean is_booked = false;
}

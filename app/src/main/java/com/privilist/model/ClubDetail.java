package com.privilist.model;

import java.util.ArrayList;

/**
 * Created by minhtdh on 6/17/15.
 */
public class ClubDetail {
    public long id;
    public long location_id;
    public String name;
    public String about;
    public String hours_of_operation;
    public String dress_code;
    public String fine_print;
    public int min_people;
    public int max_people;
    public String phone_number;
    public String contact_person_name;
    public String contact_person_email;
    public String contact_person_phone_number;
    public String longtitude;
    public String latitude;
    public String min_price;
    public String type;
    public ArrayList<Table> tables;

    // TODO not sure purpose
//    public ArrayList<String> images;
}

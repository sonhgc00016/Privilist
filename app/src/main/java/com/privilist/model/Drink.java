package com.privilist.model;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by minhtdh on 6/17/15.
 */
public class Drink {
    public long id;
    public long category_id;
    public String name;
    public String price;
    public String description;

    @JsonIgnore
    public int number;
}

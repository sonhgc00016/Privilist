package com.privilist.model;

import com.privilist.define.Constant;

import org.codehaus.jackson.annotate.JsonIgnore;

public class User {

    public long id;
    public String first_name;
    public String last_name;
    public String email;
    public long reputation_points;
    public long reward_points;
    public String birthday;
    public String gender;
    public String phone_number;
    public String referral_code;
    public Rank rank;
    //    public perk
    public long upgrade_points;
    public String full_name;

    @JsonIgnore
    public boolean isMale() {
        return Constant.MALE.equals(gender);
    }

    @JsonIgnore
    public void setGender(boolean isMale) {
        this.gender = isMale ? Constant.MALE : Constant.FEMALE;
    }

    public static class Rank {
        public String name;
        public String icon;
        public String color;
    }

    public Image image;
}

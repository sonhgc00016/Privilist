package com.privilist.model;

import android.content.res.Resources;
import android.graphics.Color;

import com.privilist.R;
import com.privilist.component.EventListAdapter;
import com.privilist.define.Constant;
import com.privilist.util.UserHelper;
import com.privilist.util.Utils;

import java.util.ArrayList;

/**
 * Created by minhtdh on 6/17/15.
 */
public class Venue implements EventListAdapter.EventItem {
    public long id;
    public long location_id;
    public String name;
    public String about;
    public String hours_of_operation;
    public String dress_code;
    public String fine_print;
    public int max_people;
    public String phone_number;
    public String contact_person_name;
    public String contact_person_email;
    public String contact_person_phone_number;
    public String longtitude;
    public String latitude;
    public int min_people;
    public String min_price;
    public String type;
    public Schedule schedule;
//    public deleted_at;
    public boolean open;
    public ArrayList<Image> images;
    //luanlq July,31 2015
    public boolean featured;
    //luanlq July,31 2015 End.
    //luanlq July 10,2015
    public int table_left;
    //luanlq July 10,2015 End.

    @Override
    public CharSequence getMainText(final Resources res) {
        return name;
    }

    @Override
    public CharSequence getSubText(final Resources res) {
        City city = UserHelper.getIns().getSelectedCity();
        String currency = city == null ? String.valueOf(Character.CURRENCY_SYMBOL) : city.currency;
        return res.getString(R.string.venue_subtext, type, min_people, max_people, currency,
                min_price);
    }

    @Override
    public CharSequence getBoxText(final Resources res) {
        //luanlq July 31,2015
        return table_left == 0 ? Constant.NO_TABLE : res.getQuantityString(R.plurals.table_left,
        table_left, table_left);
        //luanlq July 31,2015 End.
    }

    @Override
    public Image getDisplayImage() {
        return Utils.getFirstItem(images);
    }

    //luanlq July 10,2015
    @Override
    public int getSubTextColor(final Resources res) {
        return res.getColor(R.color.color_half_white);
    }
    //luanlq July 10,2015 End.
}

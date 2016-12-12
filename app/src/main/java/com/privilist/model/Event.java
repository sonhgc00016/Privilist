package com.privilist.model;

import android.content.res.Resources;
import android.graphics.Color;

import com.privilist.R;
import com.privilist.component.EventListAdapter;
import com.privilist.define.Constant;
import com.privilist.util.Utils;

import java.util.ArrayList;

/**
 * Created by minhtdh on 6/17/15.
 */
public class Event implements EventListAdapter.EventItem {
    public long id;
    public long venue_id;
    public String title;
    public String description;
    public String start_date;
    public String end_date;
    //luanlq July,22 2015
    public String tag_line;
    //luanlq July,22 2015 End.
    public long location_id;
    public Venue venue;
    public ArrayList<Image> images;
    public int table_left;

    @Override
    public CharSequence getMainText(final Resources res) {
        return title;
    }

    @Override
    public CharSequence getSubText(final Resources res) {
//        return res.getString(R.string.event);
        //luanlq July 10,2015
        return "@"+venue.name.toUpperCase();
        //luanlq July 10,2015 End.
    }

    @Override
    public CharSequence getBoxText(final Resources res) {
//        return table_left == 0 ? Constant.EMPTY : res.getQuantityString(R.plurals.table_left,
//                table_left, table_left);
        //luanlq July 10,2015
//        return "EVENT NIGHT";
        //luanlq July 10,2015 End.

        //luanlq July,22 2015
        return tag_line;
        //luanlq July,22 2015 End.
    }

    @Override
    public Image getDisplayImage() {
        return Utils.getFirstItem(images);
    }

    //luanlq July 10,2015
    @Override
    public int getSubTextColor(final Resources res) {
        return res.getColor(R.color.color_yellow);
    }
    //luanlq July 10,2015 End.
}

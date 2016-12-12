package com.privilist.model;

import android.content.res.Resources;

import com.privilist.component.PointHistoryLastBookedAdapter;

import java.util.ArrayList;

/**
 * Created by SonH on 2015-06-28.
 */
public class PointHistory {
    public Points points;

    public static class Points {
        public long available;
        public long accumulated;
        public long upgrade_points;
    }

    public ArrayList<LastBooked> last_booked;

    public static class LastBooked implements PointHistoryLastBookedAdapter.LastBookedItem {
        public long points;
        public String club;
        public String date;

        @Override
        public CharSequence getPoints(Resources res) {
            return String.valueOf(points);
        }

        @Override
        public CharSequence getClub(Resources res) {
            return club;
        }

        @Override
        public CharSequence getDate(Resources res) {
            return date;
        }
    }
}

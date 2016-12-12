package com.privilist.component;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.privilist.R;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by SonH on 2015-06-30.
 */
public class PointHistoryLastBookedAdapter extends CommonAdapter<PointHistoryLastBookedAdapter.LastBookedItem> {
    public interface LastBookedItem {
        @JsonIgnore
        CharSequence getPoints(Resources res);

        @JsonIgnore
        CharSequence getClub(Resources res);

        @JsonIgnore
        CharSequence getDate(Resources res);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = convertView;
        Holder holder;
        if (ret == null) {
            ret = LayoutInflater.from(parent.getContext()).inflate(R.layout.last_booked_item, parent, false);
            holder = new Holder();
            holder.mTvPoints = (TextView) ret.findViewById(R.id.itemLastBooked_point);
            holder.mTvClub = (TextView) ret.findViewById(R.id.itemLastBooked_club);
            holder.mTvDate = (TextView) ret.findViewById(R.id.itemLastBooked_date);
            ret.setTag(holder);
        } else {
            holder = (Holder) ret.getTag();
        }
        Resources res = parent.getResources();
        LastBookedItem item = (LastBookedItem) getItem(position);
        if (item != null) {
            holder.mTvPoints.setText("+" + item.getPoints(res));
            holder.mTvClub.setText(item.getClub(res));
            if (item.getDate(res) != null) {
                LocalDate localDate = LocalDate.parse(String.valueOf(item.getDate(res)), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
                holder.mTvDate.setText("(" + localDate.toString("MM/dd") + ")");
            }
        }
        return ret;
    }

    static class Holder {
        TextView mTvPoints;
        TextView mTvClub;
        TextView mTvDate;
    }
}

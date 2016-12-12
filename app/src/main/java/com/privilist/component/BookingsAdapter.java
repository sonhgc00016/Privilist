package com.privilist.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.privilist.R;
import com.privilist.define.Constant;
import com.privilist.model.Booking;
import com.privilist.util.GlideHelper;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;

/**
 * Created by SonH on 2015-07-02.
 */
public class BookingsAdapter extends CommonAdapter {

    private ArrayList<Booking> mArrBookings;
    private Context mContext;

    public BookingsAdapter(ArrayList<Booking> mArrBookings, Context mContext) {
        this.mArrBookings = mArrBookings;
        this.mContext = mContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
            holder = new Holder();
            holder.imgBackground = (ImageView) view.findViewById(R.id.itemBooking_imgBackground);
            holder.tvName = (TextView) view.findViewById(R.id.itemBooking_tvName);
            holder.tvTag = (TextView) view.findViewById(R.id.itemBooking_tvTag);
            holder.tvDate = (TextView) view.findViewById(R.id.itemBooking_tvDate);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        if (mArrBookings.get(position) != null) {
            if (mArrBookings.get(position).getEvent() != null) {
                if (mArrBookings.get(position).getExpired()) {
                    holder.tvDate.setTextColor(mContext.getResources().getColor(R.color.color_white));
                    GlideHelper.loadImage(mArrBookings.get(position).getEvent().images.get(0).url + "?expired=1", holder.imgBackground);
                } else {
                    holder.tvDate.setTextColor(mContext.getResources().getColor(R.color.color_orange));
                    GlideHelper.loadImage(mArrBookings.get(position).getEvent().images.get(0).url + "?expired=0", holder.imgBackground);
                }
                holder.tvName.setText(mArrBookings.get(position).getEvent().title);
                holder.tvTag.setVisibility(View.VISIBLE);
                if (mArrBookings.get(position).getTable() != null)
                    holder.tvTag.setText("@" + mArrBookings.get(position).getTable().getVenue().name);
                else
                    holder.tvTag.setText("N/A");
                if (mArrBookings.get(position).getBooking_date() != null) {
                    LocalDate localDate = LocalDate.parse(String.valueOf(mArrBookings.get(position).getBooking_date()), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
                    holder.tvDate.setText(localDate.toString("MMMM dd"));
                }
            } else {
                if (mArrBookings.get(position).getExpired()) {
                    holder.tvDate.setTextColor(mContext.getResources().getColor(R.color.color_white));
                    if (mArrBookings.get(position).getTable() != null)
                        GlideHelper.loadImage(mArrBookings.get(position).getTable()
                                .getVenue().images.get(0).url + "?expired=1", holder.imgBackground);
                    else
                        GlideHelper.loadImage("http://vignette4.wikia.nocookie.net/after-school/images/7/78/400px-Mystic_WHITE.jpg/revision/latest?cb=20131218170815", holder.imgBackground);
                } else {
                    holder.tvDate.setTextColor(mContext.getResources().getColor(R.color.color_orange));
                    if (mArrBookings.get(position).getTable() != null)
                        GlideHelper.loadImage(mArrBookings.get(position).getTable()
                                .getVenue().images.get(0).url + "?expired=0", holder.imgBackground);
                    else
                        GlideHelper.loadImage("http://vignette4.wikia.nocookie.net/after-school/images/7/78/400px-Mystic_WHITE.jpg/revision/latest?cb=20131218170815", holder.imgBackground);
                }
                if (mArrBookings.get(position).getTable() != null)
                    holder.tvName.setText(mArrBookings.get(position).getTable().getVenue().name);
                else
                    holder.tvName.setText("N/A");
                holder.tvTag.setVisibility(View.GONE);
                if (mArrBookings.get(position).getBooking_date() != null) {
                    LocalDate localDate = LocalDate.parse(String.valueOf(mArrBookings.get(position).getBooking_date()), DateTimeFormat.forPattern(Constant.DATE_FORMAT));
                    holder.tvDate.setText(localDate.toString("MMMM dd"));
                }
            }
        }
        return view;
    }

    static class Holder {
        ImageView imgBackground;
        TextView tvName, tvTag, tvDate;
    }
}

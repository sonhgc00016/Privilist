package com.privilist.frag;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.privilist.R;
import com.privilist.frag.base.BaseHeaderFrag;
import com.privilist.model.Day;
import com.privilist.model.Venue;
import com.privilist.util.Utils;

/**
 * Created by SonH on 7/11/15.
 */
public class ClubInfoFrag extends BaseHeaderFrag {

    private Venue mVenue;

    public void setVenue(final Venue pVenue) {
        mVenue = pVenue;
    }
    //luanlq July 17,2015
    private String[] days={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    private LinearLayout hour_oprerationLlt;
    //luanlq July 17,2015 End.

    @Override
    protected CharSequence getTitle() {
        return getString(R.string.club_detail_title);
    }
    private static final String ABOUT ="ABOUT";
    private static final String DRESS_CODE ="DRESS CODE";
    private static final String FINE_PRINT ="FINE PRINT";
    @Override
    protected View createContentView(final LayoutInflater inflater, final ViewGroup container,
                                     final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.club_detail_frag, container, false);
        //luanlq July 17,2015
        hour_oprerationLlt=(LinearLayout)v.findViewById(R.id.hour_oprerationLlt);
        //luanlq July 17,2015 End.
        if (mVenue != null) {
            Utils.setText(v.findViewById(R.id.tv_about), Html.fromHtml(mVenue.about).toString().replace(ABOUT,"").trim());
//            Utils.setText(v.findViewById(R.id.tv_hours), mVenue.hours_of_operation);
            Utils.setText(v.findViewById(R.id.tv_dress_code), Html.fromHtml(mVenue.dress_code.toString().replace(DRESS_CODE,"")));
            Utils.setText(v.findViewById(R.id.tv_fine_content), Html.fromHtml(mVenue.fine_print.toString().replace(FINE_PRINT,"")));
            //luanlq July 17,2015
            showSchedule();
            //luanlq July 17,2015End.
        }
        return v;
    }
    //luanlq July 17,2015
    private void showSchedule(){
        Day day;
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        for(int i=0;i<days.length;i++) {
            View view = inflater.inflate(R.layout.schedule_row, null, false);
            TextView dayTvw = (TextView) view.findViewById(R.id.dayTvw);
            TextView hourTvw = (TextView) view.findViewById(R.id.hourTvw);
            dayTvw.setText(days[i]);
            day = mVenue.schedule.returnDay(i);
            hourTvw.setText(day.open + " ~ " + day.close);
            hour_oprerationLlt.addView(view);
        }
    }
    //luanlq July 17,2015 End.
}

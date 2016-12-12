package com.privilist.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.privilist.R;
import com.privilist.component.BookingsAdapter;
import com.privilist.component.JacksonRequest;
import com.privilist.define.Constant;
import com.privilist.frag.base.BaseHeaderFrag;
import com.privilist.frag.profile.UpComingBooking;
import com.privilist.model.Booking;
import com.privilist.util.ApiHelper;
import com.privilist.util.Log;
import com.privilist.util.UserHelper;
import com.privilist.util.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by SonH on 2015-07-02.
 */
public class BookingsFrag extends BaseHeaderFrag implements AdapterView.OnItemClickListener {

    private WeakReference<ListView> mLvBookings;
    private ArrayList<Booking> mArrBookings;
    private WeakReference<TextView> mTvEmpty;

    public BookingsFrag() {
        // Required empty public constructor
    }

    @Override
    protected CharSequence getTitle() {
        return getString(R.string.bookings);
    }

    @Override
    protected View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView lv = (ListView) view.findViewById(R.id.bookings_lvBookings);
        mLvBookings = new WeakReference<ListView>(lv);
        lv.setOnItemClickListener(this);
        TextView tvEmpty = (TextView) view.findViewById(android.R.id.title);
        mTvEmpty = new WeakReference<TextView>(tvEmpty);
        tvEmpty.setText(getString(R.string.bookings_list_empty_msg));
        doGetBookings();
    }

    private void doGetBookings() {
        String access_token = UserHelper.getIns().getSavedAccessToken(getActivity());
        if (access_token != null) {
            ProgressDialogFrag.ProgressRequestDlg<ArrayList<Booking>> dlg = new
                    ProgressDialogFrag.ProgressRequestDlg<ArrayList<Booking>>();

            Response.Listener<ArrayList<Booking>> listener = new Response.Listener<ArrayList<Booking>>() {
                @Override
                public void onResponse(final ArrayList<Booking> response) {
                    mArrBookings = response;
                    ListView lv = Utils.getVal(mLvBookings);
                    BookingsAdapter adapter = new BookingsAdapter(mArrBookings, getActivity());
                    lv.setAdapter(adapter);
                    adapter.replace(mArrBookings);

                    TextView tv = Utils.getVal(mTvEmpty);
                    if (lv.getAdapter().isEmpty()) {
                        tv.setVisibility(View.VISIBLE);
                        lv.setEmptyView(tv);
                    } else {
                        tv.setVisibility(View.GONE);
                    }

                    adapter.notifyDataSetChanged();
                }
            };

            Response.ErrorListener onErrorLis = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    CharSequence errMsg = error == null ? Constant.EMPTY : error.getLocalizedMessage();
                    if (error != null && error.networkResponse != null && error.networkResponse.statusCode
                            == ApiHelper.SIGNIN_FAILURE_CODE) {
                        errMsg = getString(R.string.signin_error_authen);
                    }
                    errMsg = errMsg == null ? Constant.EMPTY : errMsg;
                    if (Log.isDLoggable(this)) {
                        String err = null;
                        try {
                            err = new String(error.networkResponse.data, "UTF-8");
                        } catch (Exception e) {
                            Log.e(TAG, Log.getStackTraceString(e));
                        }
                        Log.d(TAG, new StringBuilder("onErrorResponse ").append(" err=").append(err).append(
                                this).toString());
                    }
                    showAlert(getString(R.string.getting_data_error, errMsg));
                    commonRequestErr(error);
                }
            };

            JacksonRequest.ArrayRequestWithDlg<Booking>
                    cr = new JacksonRequest.ArrayRequestWithDlg<Booking>(Booking.class,
                    ApiHelper.getIns().getBookingsUrl(access_token), dlg, listener, onErrorLis);
            cr.setShouldCache(true);
            cr.request(getApp().getRequestQueue(), getChildFragmentManager());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Booking booking = mArrBookings.get(position);
        UpComingBooking upComingBooking = new UpComingBooking();
        upComingBooking.setmBooking(booking);
        move(upComingBooking);
    }
}

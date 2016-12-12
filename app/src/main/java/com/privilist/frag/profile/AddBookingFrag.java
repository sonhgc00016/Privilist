package com.privilist.frag.profile;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.privilist.R;
import com.privilist.app.PrivilistApp;
import com.privilist.component.JacksonRequest;
import com.privilist.define.Constant;
import com.privilist.frag.ProgressDialogFrag;
import com.privilist.frag.base.BaseHeaderFrag;
import com.privilist.frag.book.BookSuccessFrag;
import com.privilist.model.Booking;
import com.privilist.util.ApiHelper;
import com.privilist.util.Log;
import com.privilist.util.UserHelper;
import com.privilist.util.Utils;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.lang.ref.WeakReference;

/**
 * Created by SonH on 6/26/15.
 */
public class AddBookingFrag extends BaseHeaderFrag implements View.OnClickListener, Response.ErrorListener {

    private WeakReference<EditText> mEdtBookingCode;

    public AddBookingFrag() {
        // Required empty public constructor
    }

    @Override
    protected CharSequence getTitle() {
        return getString(R.string.add_booking);
    }

    @Override
    protected View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_booking, container, false);
        EditText edt;
        edt = (EditText) view.findViewById(R.id.addBooking_edtBookingCode);
        mEdtBookingCode = new WeakReference<EditText>(edt);
        Utils.setOnclick(view.findViewById(R.id.addBooking_btnSubmit), this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.addBooking_btnSubmit:
                // TODO Submit Add Booking
                EditText edt;
                edt = Utils.getVal(mEdtBookingCode);
                String code = edt.getText().toString().trim();
                if (Utils.notEmpty(code))
                    doAddBooking(code);
                break;
            default:
                break;
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        commonRequestErr(error);
    }

    private void doAddBooking(String code) {
        PrivilistApp app = getApp();
        if (app == null) {
            return;
        }

        String access_token = UserHelper.getIns().getSavedAccessToken(getActivity());
        if (access_token != null) {
            ProgressDialogFrag.ProgressRequestDlg<Booking> dlg = new ProgressDialogFrag
                    .ProgressRequestDlg<Booking>();

            JacksonRequest.RequestWithDlg<Booking> st =
                    new JacksonRequest.RequestWithDlg<Booking>
                            (Booking.class, Request.Method.POST,
                                    ApiHelper.getIns().addBookingUrl(code),
                                    ApiHelper.getIns().buildAddBookingRequest(access_token), dlg,
                                    mListener,
                                    onErrorLis);
            st.setContentType(ApiHelper.CONTENT_TYPE_FORM);
            st.request(app.getRequestQueue(), getChildFragmentManager());
        }
    }

    private Response.Listener<Booking> mListener = new Response.Listener<Booking>() {
        @Override
        public void onResponse(final Booking response) {
            if (response != null) {
                LocalDate localDate = LocalDate.parse(String.valueOf(response.getBooking_date()), DateTimeFormat.forPattern(Constant.DATE_FORMAT));
                BookSuccessFrag frag = BookSuccessFrag.newInstance
                        (localDate.toString(BookSuccessFrag.BOOK_SUCCESS_DATE_FORMAT), response.getVenue().name, response.getCode(),
                                response.getVenue().images.get(0).url);
                move(frag);
            }
        }
    };

    private Response.ErrorListener onErrorLis = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(final VolleyError error) {
            CharSequence errMsg = error == null ? Constant.EMPTY : error.getLocalizedMessage();
            if (error != null && error.networkResponse != null && error.networkResponse.statusCode
                    == ApiHelper.ADD_BOOKING_FAILURE_ALREADY_BOOKED_CODE) {
                errMsg = getString(R.string.add_booking_already_booked);
            } else if (error != null && error.networkResponse != null && error.networkResponse.statusCode
                    == ApiHelper.ADD_BOOKING_FAILURE_NOT_FOUND_CODE) {
                errMsg = getString(R.string.add_booking_code_not_found);
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
            showAlert(getString(R.string.add_booking_error, errMsg));
            if (errMsg == null)
                commonRequestErr(error);
        }
    };
}

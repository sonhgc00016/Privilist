package com.privilist.frag;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.privilist.R;
import com.privilist.app.PrivilistApp;
import com.privilist.component.JacksonRequest;
import com.privilist.define.Constant;
import com.privilist.frag.base.BaseHeaderFrag;
import com.privilist.model.SpecialRequestIPO;
import com.privilist.model.SpecialRequestOPO;
import com.privilist.util.ApiHelper;
import com.privilist.util.Log;
import com.privilist.util.UserHelper;
import com.privilist.util.Utils;

import org.joda.time.LocalDate;

import java.lang.ref.WeakReference;

/**
 * Created by minhtdh on 6/24/15.
 */
public class SpecialRequestFrag extends BaseHeaderFrag
        implements DatePickerDialog.OnDateSetListener, View.OnClickListener, Response.ErrorListener, Response.Listener<SpecialRequestOPO> {

    public LocalDate mDate;
    public long venueID = Constant.UNKNOW_ID;
    public String minPrice;

    private WeakReference<TextView> mTvSpend, mTvGuests, mTvDate, mTvPhoneNumber, mTvOtheRequest;
    @Override
    protected CharSequence getTitle() {
        return getString(R.string.special_request_title);
    }

    @Override
    protected View createContentView(final LayoutInflater inflater, final ViewGroup container,
                                     final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.special_request_frag, container, false);
        TextView tv;
        tv = (TextView) v.findViewById(R.id.edt_money_spend);
        mTvSpend = new WeakReference<TextView>(tv);
        Utils.setText(tv, minPrice);
        mTvGuests = new WeakReference<TextView>((TextView) v.findViewById(R.id.edt_guests));
        tv = (TextView) v.findViewById(R.id.tv_date);
        mTvDate = new WeakReference<TextView>(tv);
//        Utils.setOnclick(tv, this);
        mTvPhoneNumber = new WeakReference<TextView>((TextView) v.findViewById(R.id.edt_phone));
        mTvOtheRequest = new WeakReference<TextView>((TextView) v.findViewById(R.id.edt_other_request));
        Utils.setOnclick(v.findViewById(R.id.btn_special_request), this);
        setDate();
        return v;
    }

    WeakReference<DatePickerDialog> mPickerRef;
    private void showDatePicker() {
        DatePickerDialog dpd = Utils.getVal(mPickerRef);
        if (dpd == null) {
            LocalDate ld = mDate != null ? mDate : LocalDate.now();
            dpd = new DatePickerDialog(getActivity(), this, ld.getYear(), ld.getMonthOfYear(), ld.getDayOfMonth());
            mPickerRef = new WeakReference<DatePickerDialog>(dpd);
        }
        if (!dpd.isShowing()) {
            dpd.show();
        }
    }
    private static final String DATE_DISPLAY_FORMAT = "MMMM dd, yyyy";
    @Override
    public void onDateSet(final DatePicker view, final int year, final int monthOfYear,
                          final int dayOfMonth) {
        mDate = LocalDate.now().withYear(year).withMonthOfYear(monthOfYear).withDayOfMonth
                (dayOfMonth);
        setDate();
    }

    private void setDate() {
        mDate = mDate == null ? LocalDate.now() : mDate;
        TextView tv = Utils.getVal(mTvDate);
        if (tv != null) {
            tv.setText(mDate.toString(DATE_DISPLAY_FORMAT));
        }
    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_date: {
                showDatePicker();
            }
            case R.id.btn_special_request: {
                doRequest();
            }
            break;
            default:
                break;
        }
    }

    public void doRequest() {
        if (venueID == Constant.UNKNOW_ID) {
            return;
        }
        SpecialRequestIPO input = new SpecialRequestIPO();
//        if (!UserHelper.getIns().isLogin()) {
//            showAlert(getString(R.string.login_timeout));
//            return;
//        }
        input.access_token = UserHelper.getIns().getSavedAccessToken(getActivity());
        TextView tv;
        // valid minimum spend

        float minimum_spend = 0;
        try {
            minimum_spend = Float.parseFloat(minPrice);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        final float SPEND_THRESHOLD = minimum_spend;
        tv = Utils.getVal(mTvSpend);
        if (tv != null) {
            try {
                minimum_spend = Float.parseFloat(tv.getText().toString());
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
        if (minimum_spend < SPEND_THRESHOLD) {
            showAlert(getString(R.string.minimum_spend_invalid));
            return;
        }
        input.minimum_spend = minimum_spend;
        // valid date
        if (mDate == null? true : mDate.isBefore(LocalDate.now())) {
            showAlert(getString(R.string.date_invalid));
            return;
        }
        input.request_date = mDate.toString(Constant.DF_DATE_FORMAT);

        tv = Utils.getVal(mTvGuests);
        if (Utils.isEmpty(tv.getText().toString())) {
            showAlert(getString(R.string.number_of_guests_invalid));
            return;
        }
        if (tv != null) {
            try {
                input.guest = Integer.parseInt(tv.getText().toString());
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }

        tv = Utils.getVal(mTvOtheRequest);
        if (Utils.isEmpty(tv.getText().toString())) {
            showAlert(getString(R.string.special_request_invalid));
            return;
        }
        if (tv != null) {
            input.requests = tv.getText().toString();
        }
        tv = Utils.getVal(mTvPhoneNumber);
        if (tv != null) {
            input.request_phone_number = tv.getText().toString();
        }
        PrivilistApp app = getApp();
        if (app == null) {
            return;
        }
        // do request
        ProgressDialogFrag.ProgressRequestDlg<SpecialRequestOPO> dlg = new ProgressDialogFrag
                .ProgressRequestDlg<SpecialRequestOPO>();
        JacksonRequest.RequestWithDlg<SpecialRequestOPO> request = new JacksonRequest
                .RequestWithDlg<SpecialRequestOPO>(SpecialRequestOPO.class, Request.Method.POST,
                ApiHelper.getIns().getSpecialRequestUrl(venueID), ApiHelper.getIns()
                .buildSpecialRequest(input), dlg, this, this);
        request.addSuccessCode(ApiHelper.SPECIAL_REQUEST_SUCCESS_CODE);
        request.setContentType(ApiHelper.CONTENT_TYPE_FORM);
        request.request(app.getRequestQueue(), getChildFragmentManager());
    }

    @Override
    public void onErrorResponse(final VolleyError error) {
        commonRequestErr(error);
    }

    @Override
    public void onResponse(final SpecialRequestOPO response) {
        SpecialRequestResultFrag frag = new SpecialRequestResultFrag();
        move(frag);
    }
}

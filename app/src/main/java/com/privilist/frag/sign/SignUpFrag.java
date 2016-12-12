package com.privilist.frag.sign;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.privilist.R;
import com.privilist.app.PrivilistApp;
import com.privilist.component.JacksonRequest;
import com.privilist.define.Constant;
import com.privilist.frag.ProgressDialogFrag;
import com.privilist.model.RegistrationIPO;
import com.privilist.model.RegistrationOPO;
import com.privilist.util.ApiHelper;
import com.privilist.util.Log;
import com.privilist.util.Utils;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.ref.WeakReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFrag extends InputFrag implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private TextView mTvName, mTvLastName, mTvAge, mTvMail, mTvPass;
    //luanlq July 9,2015
    private TextView showGenderTvw;
    //luanlq July 9,2015 End.
    private Switch mSwitchGender;

    public SignUpFrag() {
    }

    @Override
    protected CharSequence getTitle() {
        return getString(R.string.sign_up);
    }

    @Override
    protected View createContentView(final LayoutInflater inflater, final ViewGroup container,
                                     final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
        Utils.setOnclick(v.findViewById(R.id.btnSignIn), this);
        mTvName = (TextView) v.findViewById(R.id.edt_name);
        mTvLastName = (TextView) v.findViewById(R.id.edt_lastname);
        mTvAge = (TextView) v.findViewById(R.id.edt_age);
        mTvAge.setOnClickListener(this);
        mSwitchGender = (Switch) v.findViewById(R.id.switch_gender);
        mTvMail = (TextView) v.findViewById(R.id.edt_mail);
        mTvPass = (TextView) v.findViewById(R.id.edt_password);
        showGenderTvw=(TextView)v.findViewById(R.id.showGenderTvw);
        showGenderTvw.setOnClickListener(this);
        return v;
    }
    @Override
    public void onClick(final View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnSignIn:
                if (validInput()) {
                    doSignUp();
                }
                break;
            case R.id.edt_age:
                showDatePicker();
                break;
            //luanlq July 9,2015
            case R.id.showGenderTvw:
                mSwitchGender.setVisibility(View.VISIBLE);
                break;
            //luanlq July 9,2015 End.
            default:
                break;
        }
    }

    WeakReference<DatePickerDialog> mPickerRef;
    private void showDatePicker() {
        DatePickerDialog dpd = Utils.getVal(mPickerRef);
        if (dpd == null) {
            LocalDate ld = LocalDate.now();
            dpd = new DatePickerDialog(getActivity(), this, ld.getYear() - Constant.THRESHOLD_AGE, ld.getMonthOfYear
                    (), ld.getDayOfMonth());
            mPickerRef = new WeakReference<DatePickerDialog>(dpd);
        }
        if (!dpd.isShowing()) {
            dpd.show();
        }
    }

    @Override
    public void onDateSet(final DatePicker view, final int year, final int monthOfYear,
                          final int dayOfMonth) {
        if (mTvAge != null) {
            mTvAge.setText(getString(R.string.age_format, year, monthOfYear + 1, dayOfMonth));
        }
    }

    private DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(Constant.DF_DATE_FORMAT);

    private boolean validInput() {
        boolean ret = true;
        CharSequence name = mTvName.getText();
        // TODO valid name
        if (Utils.isEmpty(name)) {
            ret = false;
        }

        name = mTvLastName.getText();
        if (Utils.isEmpty(name)) {
            ret = false;
        }

        if (!ret) {
            showAlert(getString(R.string.name_invalid));
            return ret;
        }
        CharSequence age = mTvAge.getText();
        if (!Utils.isEmpty(age)) {
            try {
                LocalDate ld = dateFormatter.parseLocalDate(age.toString());
                //luanlq July 9,2015
//                ld.toString("");
                //luanlq July 9,2015 End.
                Period period = new Period(ld, LocalDate.now());
                if (period.getYears() < Constant.THRESHOLD_AGE) {
                    ret = false;
                }
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
                ret = false;
            }

        }
        if (!ret) {
            showAlert(getString(R.string.age_invalid));
            return ret;
        }
        // valid mail and pass
        ret = validEmail(mTvMail.getText()) && validPass(mTvPass.getText());
        return ret;
    }

    private void doSignUp() {
        PrivilistApp app = getApp();
        if (app == null) {
            return;
        }

        RegistrationIPO input = new RegistrationIPO();
        input.first_name = mTvName.getText().toString().trim();
        input.last_name = mTvLastName.getText().toString().trim();
        input.birthday = mTvAge.getText().toString().trim();
        input.gender = Utils.getGender(mSwitchGender.isChecked());
        input.email = mTvMail.getText().toString().trim();
        input.password = mTvPass.getText().toString().trim();

        ProgressDialogFrag.ProgressRequestDlg<RegistrationOPO> dlg = new ProgressDialogFrag
                .ProgressRequestDlg<RegistrationOPO>();
        dlg.setMsg(getString(R.string.sign_up));

        JacksonRequest.RequestWithDlg<RegistrationOPO> st =
                new JacksonRequest.RequestWithDlg<RegistrationOPO>
                        (RegistrationOPO.class, Request.Method.POST,
                                ApiHelper.getIns().getSignUpUrl(),
                                ApiHelper.getIns().buildSignUpRequest(input), dlg,
                                mListener,
                                onErrorLis);
        st.setContentType(ApiHelper.CONTENT_TYPE_FORM);
        st.request(app.getRequestQueue(), getChildFragmentManager());
    }

    private Response.Listener<RegistrationOPO> mListener = new Response.Listener<RegistrationOPO>() {
        @Override
        public void onResponse(final RegistrationOPO response) {
            showAlert(getString(R.string.signup_success));
            //luanlq July 24,2015
            SignInFrag sif = new SignInFrag();
            move(sif);
            //luanlq July 24,2015 End.
        }
    };

    private Response.ErrorListener onErrorLis = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(final VolleyError error) {
            CharSequence errMsg = error == null ? Constant.EMPTY : error.getLocalizedMessage();
            if (error != null && error.networkResponse != null && error.networkResponse.statusCode
                    == ApiHelper.SIGNUP_FAILURE_CODE) {
                errMsg = getString(R.string.email_taken);
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
            showAlert(getString(R.string.signUp_error, errMsg));
            if (error instanceof ServerError) {
                showAlert(getString(R.string.default_request_server_err_msg));
            } else if (error instanceof TimeoutError) {
                showAlert(getString(R.string.default_request_timeout_msg));
            } else if (error instanceof NetworkError) {
                showAlert(getString(R.string.default_request_network_err_msg));
            } else {
                showAlert(getString(R.string.default_request_err_msg));
            }
        }
    };

}

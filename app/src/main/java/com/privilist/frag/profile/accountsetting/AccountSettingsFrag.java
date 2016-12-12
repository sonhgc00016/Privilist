package com.privilist.frag.profile.accountsetting;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.GlideCircleTransform;
import com.privilist.R;
import com.privilist.app.PrivilistApp;
import com.privilist.component.JacksonRequest;
import com.privilist.define.Constant;
import com.privilist.frag.ProgressDialogFrag;
import com.privilist.frag.base.BaseHeaderFrag;
import com.privilist.model.UpdateUserIPO;
import com.privilist.model.User;
import com.privilist.util.ApiHelper;
import com.privilist.util.Log;
import com.privilist.util.UserHelper;
import com.privilist.util.Utils;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by SonH on 2015-08-07.
 */
public class AccountSettingsFrag extends BaseHeaderFrag implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private User mUser;

    public void setmUser(User mUser) {
        this.mUser = mUser;
    }

    private WeakReference<ImageView> mImgAvatar;
    private WeakReference<EditText> mEdtFirstName, mEdtLastName, mEdtEmail, mEdtMobile;
    private WeakReference<TextView> mTvDob, mTvGender;
    private WeakReference<Switch> mSwGender;

    public AccountSettingsFrag() {
    }

    @Override
    protected CharSequence getTitle() {
        return getString(R.string.account_settings);
    }

    @Override
    protected View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imv = (ImageView) view.findViewById(R.id.account_settings_imgAvatar);
        mImgAvatar = new WeakReference<ImageView>(imv);

        EditText edt;
        edt = (EditText) view.findViewById(R.id.account_settings_edtFirstName);
        mEdtFirstName = new WeakReference<EditText>(edt);
        edt = (EditText) view.findViewById(R.id.account_settings_edtLastName);
        mEdtLastName = new WeakReference<EditText>(edt);
        edt = (EditText) view.findViewById(R.id.account_settings_edtEmail);
        edt.setKeyListener(null);
        mEdtEmail = new WeakReference<EditText>(edt);
        edt = (EditText) view.findViewById(R.id.account_settings_edtMobile);
        mEdtMobile = new WeakReference<EditText>(edt);

        TextView tv;
        tv = (TextView) view.findViewById(R.id.account_settings_tvDob);
        mTvDob = new WeakReference<TextView>(tv);
        tv = (TextView) view.findViewById(R.id.account_settings_tvGender);
        mTvGender = new WeakReference<TextView>(tv);
        Switch sw = (Switch) view.findViewById(R.id.account_settings_swGender);
        mSwGender = new WeakReference<Switch>(sw);

        if (mUser != null) {
            if (mUser.image != null)
                try {
                    URL url = new URL(mUser.image.url);
                    Uri uri = Uri.parse(url.toURI().toString());
                    Glide.with(getActivity())
                            .load(uri)
                            .transform(new GlideCircleTransform(getActivity()))
                            .into(imv);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            // set border for image
            imv.setBackgroundResource(R.drawable.avatar_border);
            if (mUser.gender != null) {
                sw.setChecked(mUser.gender.equals(Constant.MALE) ? true : false);
            }

            Utils.setText(view.findViewById(R.id.account_settings_edtFirstName), mUser.first_name);
            Utils.setText(view.findViewById(R.id.account_settings_edtLastName), mUser.last_name);
            LocalDate localDate = dateFormatter.parseLocalDate(String.valueOf(mUser.birthday));
            Utils.setText(view.findViewById(R.id.account_settings_tvDob), localDate.toString(Constant.DF_DATE_FORMAT));
            Utils.setText(view.findViewById(R.id.account_settings_edtEmail), mUser.email);
            Utils.setText(view.findViewById(R.id.account_settings_edtMobile), mUser.phone_number);

            Utils.setOnclick(view.findViewById(R.id.account_settings_tvGender), this);
            Utils.setOnclick(view.findViewById(R.id.account_settings_btnChangePassword), this);
            Utils.setOnclick(view.findViewById(R.id.account_settings_btnSave), this);
            Utils.setOnclick(view.findViewById(R.id.account_settings_tvDob), this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.account_settings_btnChangePassword:
                if (!UserHelper.getIns().isLoginFacebok(getActivity())) {
                    ChangePasswordFrag frag = new ChangePasswordFrag();
                    move(frag);
                }
                break;
            case R.id.account_settings_btnSave:
                if (mUser != null && validInput()) {
                    doSave();
                }
                break;
            case R.id.account_settings_tvDob:
                showDatePicker();
                break;
            default:
                break;
        }
    }

    private void doSave() {
        PrivilistApp app = getApp();
        if (app == null) {
            return;
        }

        TextView tv;
        EditText edt;
        Switch sw;

        UpdateUserIPO input = new UpdateUserIPO();
        input.access_token = UserHelper.getIns().getSavedAccessToken(getActivity());
        edt = Utils.getVal(mEdtFirstName);
        input.first_name = edt.getText().toString().trim();
        edt = Utils.getVal(mEdtLastName);
        input.last_name = edt.getText().toString().trim();
        tv = Utils.getVal(mTvDob);
        LocalDate ld = dateFormatter.parseLocalDate(tv.getText().toString());
        input.birthday = ld.toString(Constant.DF_DATE_FORMAT);
        sw = Utils.getVal(mSwGender);
        input.gender = Utils.getGender(sw.isChecked());
        edt = Utils.getVal(mEdtMobile);
        input.phone_number = edt.getText().toString().trim();

        ProgressDialogFrag.ProgressRequestDlg<User> dlg = new ProgressDialogFrag
                .ProgressRequestDlg<User>();

        JacksonRequest.RequestWithDlg<User> st =
                new JacksonRequest.RequestWithDlg<User>
                        (User.class, Request.Method.PUT,
                                ApiHelper.getIns().updateUserUrl(mUser.id),
                                ApiHelper.getIns().buildUpdateUserRequest(input), dlg,
                                mListener,
                                onErrorLis);
        st.setContentType(ApiHelper.CONTENT_TYPE_FORM);
        st.request(app.getRequestQueue(), getChildFragmentManager());
    }

    private Response.Listener<User> mListener = new Response.Listener<User>() {
        @Override
        public void onResponse(final User response) {
            showAlert(getString(R.string.save_changes));
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
            showAlert(getString(R.string.default_request_err_msg, errMsg));
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

    private DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(Constant.DF_DATE_FORMAT);

    private boolean validInput() {
        boolean ret = true;
        EditText edt;
        edt = Utils.getVal(mEdtFirstName);
        if (Utils.isEmpty(edt.getText())) {
            ret = false;
        }

        edt = Utils.getVal(mEdtLastName);
        if (Utils.isEmpty(edt.getText())) {
            ret = false;
        }

        if (!ret) {
            showAlert(getString(R.string.name_invalid));
            return ret;
        }

        TextView tv;
        tv = Utils.getVal(mTvDob);
        if (!Utils.isEmpty(tv.getText())) {
            try {
                LocalDate ld = dateFormatter.parseLocalDate(tv.getText().toString());
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
        return ret;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        TextView tv;
        tv = Utils.getVal(mTvDob);
        tv.setText(getString(R.string.age_format, year, monthOfYear + 1, dayOfMonth));
    }
}

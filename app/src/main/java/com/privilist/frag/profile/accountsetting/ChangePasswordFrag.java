package com.privilist.frag.profile.accountsetting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
import com.privilist.frag.base.BaseHeaderFrag;
import com.privilist.frag.sign.InputFrag;
import com.privilist.model.ChangePasswordIPO;
import com.privilist.model.User;
import com.privilist.util.ApiHelper;
import com.privilist.util.Log;
import com.privilist.util.UserHelper;
import com.privilist.util.Utils;

import java.lang.ref.WeakReference;

/**
 * Created by SonH on 2015-08-13.
 */
public class ChangePasswordFrag extends InputFrag implements View.OnClickListener {

    WeakReference<EditText> mEdtOldPassword, mEdtNewPassword, mEdtRepeatNewPassword;

    public ChangePasswordFrag() {
    }

    @Override
    protected CharSequence getTitle() {
        return getString(R.string.change_password);
    }

    @Override
    protected View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText edt;
        edt = (EditText) view.findViewById(R.id.change_password_edt_old_pass);
        mEdtOldPassword = new WeakReference<EditText>(edt);
        edt = (EditText) view.findViewById(R.id.change_password_edt_new_pass);
        mEdtNewPassword = new WeakReference<EditText>(edt);
        edt = (EditText) view.findViewById(R.id.change_password_edt_repeat_new_pass);
        mEdtRepeatNewPassword = new WeakReference<EditText>(edt);
        Utils.setOnclick(view.findViewById(R.id.change_password_btn_change_pass), this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.change_password_btn_change_pass:
                if (validateData())
                    doChangePass();
                break;
            default:
                break;
        }
    }

    private boolean validateData() {
        EditText edt;
        edt = Utils.getVal(mEdtOldPassword);
        if (Utils.isEmpty(edt.getText())) {
            showAlert(getString(R.string.please_input_old_pass));
            return false;
        }
        edt = Utils.getVal(mEdtNewPassword);
        String newPass = edt.getText().toString();
        if (Utils.isEmpty(newPass)) {
            showAlert(getString(R.string.please_input_new_pass));
            return false;
        }
        edt = Utils.getVal(mEdtRepeatNewPassword);
        String repeatNewPass = edt.getText().toString();
        if (!Utils.compare(newPass, repeatNewPass)) {
            showAlert(getString(R.string.repeat_password_not_match));
            return false;
        }
        if(!validPass(newPass)){
            return false;
        }
        return true;
    }

    private void doChangePass() {
        PrivilistApp app = getApp();
        if (app == null) {
            return;
        }

        String access_token = UserHelper.getIns().getSavedAccessToken(getActivity());
        if (access_token != null) {
            EditText edt;
            ChangePasswordIPO input = new ChangePasswordIPO();

            input.access_token = access_token;
            edt = Utils.getVal(mEdtOldPassword);
            input.old_password = edt.getText().toString().trim();
            edt = Utils.getVal(mEdtNewPassword);
            input.password = edt.getText().toString().trim();

            ProgressDialogFrag.ProgressRequestDlg<User> dlg = new ProgressDialogFrag
                    .ProgressRequestDlg<User>();

            JacksonRequest.RequestWithDlg<User> st =
                    new JacksonRequest.RequestWithDlg<User>
                            (User.class, Request.Method.POST,
                                    ApiHelper.getIns().changePassUrl(),
                                    ApiHelper.getIns().buildChangePassRequest(input), dlg,
                                    mListener,
                                    onErrorLis);
            st.setContentType(ApiHelper.CONTENT_TYPE_FORM);
            st.request(app.getRequestQueue(), getChildFragmentManager());
        }
    }

    private Response.Listener<User> mListener = new Response.Listener<User>() {
        @Override
        public void onResponse(final User response) {
            showAlert(getString(R.string.save_changes));
        }
    };

    private final int CHANGE_PASSWORD_ERROR_CODE = 422;
    private Response.ErrorListener onErrorLis = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(final VolleyError error) {
            CharSequence errMsg = error == null ? Constant.EMPTY : error.getLocalizedMessage();
            if (error != null && error.networkResponse != null && error.networkResponse.statusCode
                    == CHANGE_PASSWORD_ERROR_CODE) {
                errMsg = getString(R.string.old_password_error);
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
            showAlert(errMsg);
            if (Utils.isEmpty(errMsg))
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

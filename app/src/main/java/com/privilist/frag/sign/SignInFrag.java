package com.privilist.frag.sign;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.privilist.frag.home.HomeFrag;
import com.privilist.model.LoginIPO;
import com.privilist.model.LoginOPO;
import com.privilist.util.ApiHelper;
import com.privilist.util.Log;
import com.privilist.util.UserHelper;
import com.privilist.util.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFrag extends InputFrag implements View.OnClickListener, Response.ErrorListener {
    private TextView mTvMail, mTvPass;


    public SignInFrag() {
        // Required empty public constructor
    }

    @Override
    protected CharSequence getTitle() {
        return getString(R.string.sign_in);
    }

    @Override
    protected View createContentView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        Utils.setOnclick(v.findViewById(R.id.btnSignIn), this);
        mTvMail = (TextView) v.findViewById(R.id.edt_mail);
        mTvPass = (TextView) v.findViewById(R.id.edt_password);
        // TODO for test
        mTvMail.setText("suneo@davis.com");
        mTvPass.setText("tvo12345");
        return v;
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.btnSignIn) {
            CharSequence mail = mTvMail.getText();
            CharSequence pass = mTvPass.getText();
            if (validEmail(mail) && validPass(pass)) {
                doSignIn(mail, pass);
            }
        }
    }

    private void doSignIn(CharSequence email, CharSequence pass) {
        PrivilistApp app = getApp();
        if (app == null) {
            return;
        }

        LoginIPO input = new LoginIPO();
        input.username = mTvMail.getText().toString().trim();
        input.password = mTvPass.getText().toString().trim();

        ProgressDialogFrag.ProgressRequestDlg<LoginOPO> dlg = new ProgressDialogFrag
                .ProgressRequestDlg<LoginOPO>();
        dlg.setMsg(getString(R.string.sign_in));

        JacksonRequest.RequestWithDlg<LoginOPO> st =
                new JacksonRequest.RequestWithDlg<LoginOPO>
                        (LoginOPO.class, Request.Method.POST,
                                ApiHelper.getIns().getSigninUrl(),
                                ApiHelper.getIns().buildSigninRequest(input), dlg,
                                mListener,
                                onErrorLis);
        st.setContentType(ApiHelper.CONTENT_TYPE_FORM);
        st.request(app.getRequestQueue(), getChildFragmentManager());
    }


    private Response.Listener<LoginOPO> mListener = new Response.Listener<LoginOPO>() {
        @Override
        public void onResponse(final LoginOPO response) {
            if (getApp() != null) {
                UserHelper.getIns().setLoginInfo(response);
                // SonH June 26, 2015
                // save access_token to saved preference
                UserHelper.getIns().doSaveUserInfo(getActivity(), false);
                // SonH June 26, 2015 End.
                HomeFrag hf = new HomeFrag();
                Option opt = hf.generateDefaultOption();
                opt.finishCurrent = true;
                move(hf, opt);
            }
        }
    };

    private Response.ErrorListener onErrorLis = new Response.ErrorListener() {
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

    @Override
    public void onErrorResponse(VolleyError error) {
        commonRequestErr(error);
    }
}

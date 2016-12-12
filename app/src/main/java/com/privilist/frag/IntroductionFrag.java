package com.privilist.frag;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.privilist.R;
import com.privilist.act.base.BaseAct;
import com.privilist.app.PrivilistApp;
import com.privilist.component.CommonFragPagerAdt;
import com.privilist.component.JacksonRequest;
import com.privilist.define.Constant;
import com.privilist.frag.base.BaseFrag;
import com.privilist.frag.home.HomeFrag;
import com.privilist.frag.sign.SignInFrag;
import com.privilist.frag.sign.SignUpFrag;
import com.privilist.model.LoginIPO;
import com.privilist.model.LoginOPO;
import com.privilist.util.ApiHelper;
import com.privilist.util.Log;
import com.privilist.util.UserHelper;
import com.privilist.util.Utils;
import com.viewpagerindicator.IconPageIndicator;
import com.viewpagerindicator.IconPagerAdapter;

import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 *
 */
public class IntroductionFrag extends BaseFrag implements View.OnClickListener, BaseAct.OnBackPress {


    private IntroductAdapter mAdapter;

    // SonH June 24, 2015
    // work with FB SDK
    private LoginButton mLoginButton;
    private CallbackManager mCallbackManager;
    private String mEmailFB;
    private String mIdFB;
    private String mFirstname;
    private String mLastname;
    private String mAvatarUrl;
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(final LoginResult loginResult) {
            GraphRequest.newMeRequest(
                    loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {
                            if (response.getError() != null) {
                                // handle error
                            } else {
                                mEmailFB = me.optString("email");
                                mIdFB = me.optString("id");
                                mFirstname = me.optString("first_name");
                                mLastname = me.optString("last_name");
                                mAvatarUrl = "https://graph.facebook.com/" + mIdFB + "/picture?type=large";
                                // send email and id to your web server
                                if (mIdFB != null && mEmailFB != null
                                        && mFirstname != null && mLastname != null)
                                    doSignInFB(mIdFB, mEmailFB);
                            }
                        }
                    }).executeAsync();
        }

        @Override
        public void onCancel() {
            UserHelper.getIns().logout(getActivity());
        }

        @Override
        public void onError(FacebookException e) {
            UserHelper.getIns().logout(getActivity());
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof BaseAct) {
            ((BaseAct) getActivity()).registeronBackPress(this);
        }
    }

    @Override
    public void onPause() {
        if (getActivity() instanceof BaseAct) {
            ((BaseAct) getActivity()).unregisteronBackPress(this);
        }
        super.onPause();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initial Facebook SDK
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        // Code to print out the key hash
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(
                    "com.privilist",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void doSignInFB(String idFB, String emailFB) {
        PrivilistApp app = getApp();
        if (app == null) {
            return;
        }

        LoginIPO input = new LoginIPO();
        input.idFB = idFB;
        input.emailFB = emailFB;
        input.hash = getMD5(idFB + "tvoprivilist" + emailFB);
        input.firstname = mFirstname;
        input.lastname = mLastname;

        ProgressDialogFrag.ProgressRequestDlg<LoginOPO> dlg = new ProgressDialogFrag
                .ProgressRequestDlg<LoginOPO>();
        dlg.setMsg(getString(R.string.sign_in));

        JacksonRequest.RequestWithDlg<LoginOPO> st =
                new JacksonRequest.RequestWithDlg<LoginOPO>
                        (LoginOPO.class, Request.Method.POST,
                                ApiHelper.getIns().getSigninFBUrl(),
                                ApiHelper.getIns().buildSigninFBRequest(input), dlg,
                                mListener,
                                onErrorLis);
        st.setContentType(ApiHelper.CONTENT_TYPE_FORM);
        st.request(app.getRequestQueue(), getChildFragmentManager());
    }

    private Response.Listener<LoginOPO> mListener = new Response.Listener<LoginOPO>() {
        @Override
        public void onResponse(final LoginOPO response) {
            if (getApp() != null) {
                response.avatarFBUrl = mAvatarUrl;
                UserHelper.getIns().setLoginInfo(response);
                HomeFrag hf = new HomeFrag();
                // save access_token
                UserHelper.getIns().doSaveUserInfo(getActivity(), true);
                move(hf);
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
            UserHelper.getIns().logout(getActivity());
            showAlert(getString(R.string.signin_error, errMsg));
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

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    // SonH June 24, 2015 End.

    public IntroductionFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_introduction, container, false);
        final ViewPager vp = (ViewPager) v.findViewById(R.id.introduction_pager);
        mAdapter = new IntroductAdapter(getChildFragmentManager());
        vp.setAdapter(mAdapter);
        IconPageIndicator ipi = (IconPageIndicator) v.findViewById(R.id.pager_indicator);
        ipi.setViewPager(vp);
        Utils.setOnclick(v.findViewById(R.id.btnFB), this);
        Utils.setOnclick(v.findViewById(R.id.btnSignIn), this);
        Utils.setOnclick(v.findViewById(R.id.btnSignUp), this);

        // SonH June 24, 2015
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton = (LoginButton) v.findViewById(R.id.login_button);
        mLoginButton.setReadPermissions(Arrays.asList("public_profile, email, user_friends"));
        mLoginButton.setFragment(this);
        mLoginButton.registerCallback(mCallbackManager, mCallback);
        // SonH June 24, 2015 End.

        return v;
    }

    @Override
    public void onClick(final View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.btnFB:
                // TODO login by Facebook
                // SonH June 25, 2015
                mLoginButton.performClick();
                // SonH June 25, 2015 End.
                break;
            case R.id.btnSignIn: {
                SignInFrag sif = new SignInFrag();
                move(sif);
            }
            break;
            case R.id.btnSignUp: {
                SignUpFrag suf = new SignUpFrag();
                move(suf);
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    class PageItem {

    }

    protected class IntroductAdapter extends CommonFragPagerAdt<PageItem> implements IconPagerAdapter {

        {
            // TODO dummy item code
            for (int i = 0; i < 2; i++) {
                mItems.add(new PageItem());
            }
        }

        public IntroductAdapter(final FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(final int position) {
            int dataPos = position;
            PageItem item = mItems.get(dataPos);
            IntroductionPageFrag ret = IntroductionPageFrag.newInstance(dataPos, item);
            return ret;
        }

        @Override
        public int getIconResId(final int i) {
            return R.drawable.introduction_pager;
        }
    }

    public static class IntroductionPageFrag extends BaseFrag {
        public static final int ANIM_TIME = 1500;
        private int mPage = 0;

        public IntroductionPageFrag() {

        }

        public static IntroductionPageFrag newInstance(int page, PageItem item) {
            IntroductionPageFrag ret = new IntroductionPageFrag();
            ret.mPage = page;
            return ret;
        }

        @Nullable
        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
            View v = null;
            switch (mPage) {
                case 0: {
                    v = inflater.inflate(R.layout.introduct_page_1, container, false);
                    View tmp;
                    tmp = v.findViewById(R.id.tv_title);
                    if (tmp != null) {
                        YoYo.with(Techniques.BounceInDown).duration(ANIM_TIME).playOn(tmp);
                    }
                    tmp = v.findViewById(R.id.tv_content);
                    if (tmp != null) {
                        YoYo.with(Techniques.BounceInUp).duration(ANIM_TIME).playOn(tmp);
                    }
                }
                break;
                case 1: {
                    v = inflater.inflate(R.layout.introduct_page_2, container, false);
                }
                break;
                default:
                    v = new View(getActivity());
                    break;
            }

            v.setContentDescription("pager " + mPage);
            if (mPage % 2 == 0) {
                v.setBackgroundResource(R.drawable.background_onboard1);
            } else {
                v.setBackgroundResource(R.drawable.background_onboard2);
            }

            return v;
        }
    }

    // SonH June 24, 2015
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    // SonH June 24, 2015 End.
}

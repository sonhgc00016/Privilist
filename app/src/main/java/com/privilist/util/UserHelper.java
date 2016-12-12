package com.privilist.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginFragment;
import com.facebook.login.LoginManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.privilist.R;
import com.privilist.act.IntroductionAct;
import com.privilist.frag.CityFrag;
import com.privilist.frag.ProgressDialogFrag;
import com.privilist.frag.profile.ProfileFrag;
import com.privilist.model.City;
import com.privilist.model.LoginOPO;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by minhtdh on 6/23/15.
 */
public class UserHelper {
    private static volatile UserHelper ourInstance = new UserHelper();

    public static UserHelper getIns() {
        return ourInstance;
    }

    private UserHelper() {
    }

    private LoginOPO mLoginInfo;
    private long mLoginTime;
    private City mSelectedCity;

    public LoginOPO getLoginInfo() {
        return mLoginInfo;
    }

    public void setLoginInfo(final LoginOPO pLoginInfo) {
        mLoginInfo = pLoginInfo;
        mLoginTime = System.currentTimeMillis();
    }

    public boolean isLogin() {
        return mLoginInfo != null && System.currentTimeMillis() - mLoginTime < mLoginInfo.expires_in;
    }

    public String getLoginToken() {
        return mLoginInfo == null ? null : mLoginInfo.access_token;
    }

    public City getSelectedCity() {
        return mSelectedCity;
    }

    public String getCurrency() {
        return mSelectedCity == null ? String.valueOf(Character.CURRENCY_SYMBOL) : mSelectedCity.currency;
    }

    public void setSelectedCity(final City pSelectedCity) {
        mSelectedCity = pSelectedCity;
    }

    // SonH June 26, 2015
    public static final String MY_PREFS_NAME = "privilist_sharedpreferences";

    /**
     * @param context method save user info
     */
    public void doSaveUserInfo(Context context, boolean isLoginFacebok) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE).edit();
        if (mLoginInfo != null) {
            editor.putString("access_token", mLoginInfo.access_token);
            if (isLoginFacebok)
                editor.putBoolean("isLoginFacebook", isLoginFacebok);
            editor.commit();
        }
    }

    /**
     * @param pContext
     * @return access_token
     */
    public String getSavedAccessToken(Context pContext) {
        SharedPreferences prefs = pContext.getSharedPreferences(UserHelper.MY_PREFS_NAME, pContext.MODE_PRIVATE);
        String access_token = prefs.getString("access_token", null);
        if (access_token != null) {
            return access_token;
        }
        return access_token;
    }

    /**
     * @param pContext
     * @return isLoginFacebook
     */
    public boolean isLoginFacebok(Context pContext) {
        SharedPreferences prefs = pContext.getSharedPreferences(UserHelper.MY_PREFS_NAME, pContext.MODE_PRIVATE);
        return prefs.getBoolean("isLoginFacebook", false);
    }

    /**
     * @param pContext clear access_token
     */
    public void clearSharedPreferences(Context pContext) {
        SharedPreferences.Editor editor = pContext.getSharedPreferences(MY_PREFS_NAME, pContext.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

    /**
     * @param pContext
     */
    public void logout(Context pContext) {
        if (Utils.isOnline(pContext)) {
            // initial Facebook SDK fix bug null when try logout Facebook
            FacebookSdk.sdkInitialize(pContext.getApplicationContext());

            // logout Facebook
            LoginManager.getInstance().logOut();

            // clear user info access_token
            UserHelper.getIns().clearSharedPreferences(pContext);

            CityFrag.isSelectedCity = false;

            mLoginTime = 0;
            mLoginInfo = null;
            mSelectedCity = null;
            if (pContext instanceof IntroductionAct) {
                IntroductionAct introductionAct = (IntroductionAct) pContext;

                // start new Introduction activity
                Intent startI = new Intent(introductionAct, IntroductionAct.class);
//            introductionAct.finish();
                introductionAct.startActivity(startI);
            }
        } else {
            IntroductionAct introductionAct = (IntroductionAct) pContext;
            ProfileFrag profileFrag = (ProfileFrag) introductionAct.getSupportFragmentManager().findFragmentById(R.id.content_container);
            profileFrag.showAlert(introductionAct.getString(R.string.default_request_network_err_msg));
        }
    }
    // SonH June 26, 2015 End.
}

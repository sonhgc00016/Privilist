package com.privilist.frag.base;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.privilist.R;
import com.privilist.app.PrivilistApp;
import com.privilist.frag.AlertFrag;
import com.privilist.frag.home.HomeFrag;
import com.privilist.util.Log;
import com.privilist.util.Utils;

/**
 * Created by minhtdh on 6/10/15.
 */
public class BaseFrag extends Fragment {
    protected final String TAG = getClass().getSimpleName();
    public void finish() {
        if (isAdded() && getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    public void showAlert(CharSequence msg) {
        Snackbar sb = Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG);
        sb.setText(msg);
        sb.show();
    }

    protected void alert(CharSequence msg) {
        AlertFrag frag = new AlertFrag();
        frag.msg = msg;
        frag.show(getChildFragmentManager(), AlertFrag.class.getSimpleName());
    }

    protected void commonRequestErr(VolleyError error) {
        if (Log.isDLoggable(this)) {
            String err = null;
            try {
                err = new String(error.networkResponse.data, Request.DEFAULT_PARAMS_ENCODING);
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
            Log.d(TAG, new StringBuilder("onErrorResponse ").append(" err=").append(err).append(
                    this).toString());
        }
        if (isAdded()) {
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
    }

    protected void backToHome(FragmentManager fm) {
        fm.popBackStack(HomeFrag.class.getName(), 0);
    }

    @Override
    public void onPause() {
        Utils.hideSoftKeyboard(getActivity());
        super.onPause();
    }

    public PrivilistApp getApp() {
        return getActivity() == null ? null : (PrivilistApp) getActivity().getApplication();
    }

    public static class Option {
        public boolean finishCurrent = false;
        public boolean useAnimation = true;
        public boolean isReplace = true;
        public boolean isAddBackStack = true;
        public String backStackName;
        public boolean useSenderPlaceHolder = true;
        int placeHolderId;
        public String tag;
        
        public void setPlaceHolder(int holderId) {
            useSenderPlaceHolder = false;
            placeHolderId = holderId;
        }
    }

    private Option mOption;

    public Option getOption() {
        return mOption;
    }

    public Option generateDefaultOption() {
        Option opt = new Option();
        opt.tag = getClass().getSimpleName();
        opt.backStackName = getClass().getName();
        return opt;
    }

    public void move(BaseFrag newFrag) {
        if (newFrag != null) {
            Option opt = newFrag.generateDefaultOption();
            move(newFrag, opt);
        }
    }

    public void move(BaseFrag newFrag, Option option) {
        move(getFragmentManager(), newFrag, option);
    }

    public void move(FragmentManager fm, BaseFrag newFrag, Option option) {
        if (isAdded() && newFrag != null) {
            if (option != null && option.useSenderPlaceHolder
                    && mOption != null) {
                option.placeHolderId = mOption.placeHolderId;
            }
            if (option != null && option.finishCurrent) {
                finish();
            }
            newFrag.selfMove(fm, option);
        }
    }
    
    public void selfMove(FragmentManager fm, Option option) {
        if (option != null) {
            mOption = option;
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out,
                    R.anim.slide_left_in, R.anim.slide_right_out);
            if (option.isReplace && option.placeHolderId != 0) {
                ft.replace(option.placeHolderId, this, option.tag);

            } else {
                if (option.placeHolderId == 0) {
                    ft.add(this, option.tag);
                } else {
                    ft.add(option.placeHolderId, this, option.tag);
                }
            }
            if (option.isAddBackStack) {
                ft.addToBackStack(option.backStackName);
            }
            ft.commit();
        }
    }
}

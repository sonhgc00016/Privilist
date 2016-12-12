package com.privilist.act.base;

import android.support.v4.app.FragmentActivity;

import com.privilist.R;
import com.privilist.util.Utils;

import java.lang.ref.WeakReference;

/**
 * Created by minhtdh on 6/10/15.
 */
public class BaseAct extends FragmentActivity {

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        enterAnim = R.anim.slide_right_in;
        exitAnim = R.anim.slide_left_out;
        super.overridePendingTransition(enterAnim, exitAnim);
    }

    public interface OnBackPress {
        void onBackPressed();
    }

    private WeakReference<OnBackPress> mOnBackPressRef;
    public void registeronBackPress(OnBackPress listener) {
        mOnBackPressRef = new WeakReference<OnBackPress>(listener);
    }
    public void unregisteronBackPress(OnBackPress listener) {
        OnBackPress val = Utils.getVal(mOnBackPressRef);
        if (val == listener) {
            mOnBackPressRef = null;
        }
    }

    @Override
    public void onBackPressed() {
        OnBackPress val = Utils.getVal(mOnBackPressRef);
        if (val != null) {
            val.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}

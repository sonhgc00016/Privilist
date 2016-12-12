package com.privilist.component.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;

/**
 * Created by minhtdh on 6/24/15.
 */
public class TabIndicator extends TabPageIndicator {
    public TabIndicator(final Context context) {
        super(context);
    }

    public TabIndicator(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public interface ConfigureText {
        void configureText(TextView tv);
    }

    private ConfigureText mConfigure;

    public ConfigureText getTextConfigure() {
        return mConfigure;
    }

    public void setTextConfigure(final ConfigureText pConfigure) {
        mConfigure = pConfigure;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (mConfigure != null && getChildCount() > 0 && getChildAt(0) instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) getChildAt(0);
            for (int i = 0; i < vg.getChildCount(); i++) {
                if (vg.getChildAt(i) instanceof TextView) {
                    mConfigure.configureText((TextView) vg.getChildAt(i));
                }
            }
        }
    }
}

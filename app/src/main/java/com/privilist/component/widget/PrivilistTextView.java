package com.privilist.component.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.privilist.R;
import com.privilist.component.FontManager;

/**
 * Created by minhtdh on 6/30/15.
 */
public class PrivilistTextView extends TextView {

    public PrivilistTextView(final Context context) {
        super(context);
        init(null, 0, 0);
    }

    public PrivilistTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public PrivilistTextView(final Context context, final AttributeSet attrs,
                             final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PrivilistTextView(final Context context, final AttributeSet attrs,
                             final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(final AttributeSet attrs,
                      final int defStyleAttr, final int defStyleRes) {
        if (attrs != null) {
            TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable
                    .PrivilistTextView, defStyleAttr, defStyleRes);
            if (ta != null) {
                String font = null;
                int style = FontManager.TextStyle.Normal.ordinal();
                if (ta.hasValue(R.styleable.PrivilistTextView_font)) {
                    font = ta.getString(R.styleable.PrivilistTextView_font);
                }
                if (ta.hasValue(R.styleable.PrivilistTextView_fontStyle)) {
                    style = ta.getInt(R.styleable.PrivilistTextView_fontStyle,
                            FontManager.TextStyle.Normal.ordinal());
                }
                if (font != null) {
                    setFont(font, style);
                }
                ta.recycle();
            }
        }
    }

    public void setFont(String assetFontPath) {
        setFont(assetFontPath, 0);
    }

    public void setFont(String assetFontPath, int style) {
        Typeface font = FontManager.getIns().getFont(getResources(), assetFontPath);
        setTypeface(font, style);
    }

}

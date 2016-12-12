package com.privilist.frag.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.privilist.R;

/**
 * Created by minhtdh on 6/11/15.
 */
public class BaseHeaderFrag extends BaseFrag {

    @Nullable
    @Override
    public final View onCreateView(final LayoutInflater inflater, final ViewGroup container, final
    Bundle savedInstanceState) {
        Context c = inflater.getContext();
        LinearLayout v = new LinearLayout(c);
        v.setOrientation(LinearLayout.VERTICAL);
        v.setBackgroundColor(getResources().getColor(R.color.color_black));
//        v.setBackgroundResource(R.drawable.overlay);
        View header = createdHeaderView(inflater, v, savedInstanceState);
        if (header != null) {
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            v.addView(header, lParams);
            View divider = new View(c);
            divider.setBackgroundColor(getResources().getColor(R.color.divider_start_color));
            LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue
                    .COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
            v.addView(divider, dividerParams);
        }
        View content = createContentView(inflater, v, savedInstanceState);
        if (content != null) {
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            v.addView(content, lParams);

        }
        return v;
    }

    /**
     * default create overlaybg support toolbar view <br/>
     * return null if don't need header
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected View createdHeaderView(final LayoutInflater inflater, final ViewGroup container,
                                     final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_header, container, false);
        RelativeLayout rltBack = (RelativeLayout) v.findViewById(R.id.rltBack);
        rltBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavigatorClick(v);
            }
        });
        TextView tvTitle = (TextView) v.findViewById(R.id.toolbar_title);
        tvTitle.setText(getTitle().toString().toUpperCase());
        return v;
    }

    protected void onNavigatorClick(View v) {
        finish();
    }

    protected CharSequence getTitle() {
        return null;
    }

    protected View createContentView(final LayoutInflater inflater, final ViewGroup container,
                                     final Bundle savedInstanceState) {
        return null;
    }
}

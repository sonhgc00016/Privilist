package com.privilist.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.privilist.R;
import com.privilist.frag.base.BackToHomeFrag;

/**
 * Created by minhtdh on 6/25/15.
 */
public class SpecialRequestResultFrag extends BackToHomeFrag {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final
    Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.special_request_result, container, false);
        return  v;
    }
}

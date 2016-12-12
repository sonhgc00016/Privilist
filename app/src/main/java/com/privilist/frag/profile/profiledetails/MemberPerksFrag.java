package com.privilist.frag.profile.profiledetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.privilist.R;
import com.privilist.frag.base.BaseFrag;

/**
 * Created by SonH on 2015-06-30.
 */
public class MemberPerksFrag extends BaseFrag implements Response.ErrorListener{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_member_perks, container, false);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        commonRequestErr(error);
    }
}

package com.privilist.frag.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.privilist.R;
import com.privilist.frag.base.BaseHeaderFrag;
import com.privilist.util.Utils;

/**
 * Created by SonH on 2015-07-13.
 */
public class ReferAFriendFrag extends BaseHeaderFrag implements View.OnClickListener {

    private String mReferralCode;

    public ReferAFriendFrag() {
    }

    public static ReferAFriendFrag newInstance(String pReferralCode) {
        ReferAFriendFrag frag = new ReferAFriendFrag();
        frag.mReferralCode = pReferralCode;
        return frag;
    }

    @Override
    protected CharSequence getTitle() {
        return getString(R.string.refer_a_friend);
    }

    @Override
    protected View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refer_a_friend, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mReferralCode != null) {
            Utils.setText(view.findViewById(R.id.refer_a_friend_tv_referral_code), mReferralCode);
            Utils.setOnclick(view.findViewById(R.id.refer_a_friend_btn_refer), this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.refer_a_friend_btn_refer:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mReferralCode);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share via"));
                break;
            default:
                break;
        }
    }
}

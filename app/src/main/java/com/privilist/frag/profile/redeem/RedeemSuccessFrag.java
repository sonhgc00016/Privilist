package com.privilist.frag.profile.redeem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.privilist.R;
import com.privilist.frag.base.BackToHomeFrag;
import com.privilist.util.GlideHelper;
import com.privilist.util.Utils;

/**
 * Created by SonH on 2015-07-08.
 */
public class RedeemSuccessFrag extends BackToHomeFrag{

    private String mTitle, mCode, mImagePath;

    public static RedeemSuccessFrag newInstance(String title, String code, String imagePath) {
        RedeemSuccessFrag frag = new RedeemSuccessFrag();
        frag.mTitle = title;
        frag.mCode = code;
        frag.mImagePath = imagePath;
        return frag;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final
    Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_redeem_success, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.setText(view.findViewById(R.id.redeemSuccess_tvTitle), mTitle);
        Utils.setText(view.findViewById(R.id.redeemSuccess_tvCode), mCode);
        GlideHelper.loadImage(mImagePath, (ImageView) view.findViewById(R.id.redeemSuccess_imgReward));
    }
}

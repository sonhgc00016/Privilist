package com.privilist.frag.profile.redeem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.privilist.R;
import com.privilist.component.JacksonRequest;
import com.privilist.define.Constant;
import com.privilist.frag.ProgressDialogFrag;
import com.privilist.frag.base.BaseHeaderFrag;
import com.privilist.model.RedeemRewardOPO;
import com.privilist.model.Reward;
import com.privilist.util.ApiHelper;
import com.privilist.util.GlideHelper;
import com.privilist.util.Log;
import com.privilist.util.UserHelper;
import com.privilist.util.Utils;

/**
 * Created by SonH on 2015-07-08.
 */
public class RedeemRewardFrag extends BaseHeaderFrag{
    private Reward mReward;
    public static final int REDEEM_ERROR_CODE = 422;

    public RedeemRewardFrag() {
    }

    public void setmReward(Reward mReward) {
        this.mReward = mReward;
    }

    @Override
    protected CharSequence getTitle() {
        return getString(R.string.redeem_reward);
    }

    @Override
    protected View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_redeem_reward, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mReward != null) {
            GlideHelper.loadImage(mReward.getImage(), (ImageView) view.findViewById(R.id.itemReward_imgBackground));
            Utils.setText(view.findViewById(R.id.redeem_reward_tvDescription), mReward.getDescription());
            Button btnRedeem = (Button) view.findViewById(R.id.redeem_reward_btnRedeem);
            btnRedeem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String access_token = UserHelper.getIns().getSavedAccessToken(getActivity());
                    if (access_token != null) {
                        ProgressDialogFrag.ProgressRequestDlg<RedeemRewardOPO> dlg = new ProgressDialogFrag
                                .ProgressRequestDlg<RedeemRewardOPO>();

                        Response.Listener<RedeemRewardOPO> listener = new Response.Listener<RedeemRewardOPO>() {
                            @Override
                            public void onResponse(final RedeemRewardOPO response) {
                                RedeemSuccessFrag redeemSuccessFrag
                                        = RedeemSuccessFrag.newInstance(mReward.getTitle(), response.getCode(), mReward.getImage().url);
                                move(redeemSuccessFrag);
                            }
                        };

                        Response.ErrorListener onErrorLis = new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(final VolleyError error) {
                                CharSequence errMsg = error == null ? Constant.EMPTY : error.getLocalizedMessage();
                                if (error != null && error.networkResponse != null && error.networkResponse.statusCode
                                        == REDEEM_ERROR_CODE) {
                                    errMsg = getString(R.string.redeem_error_not_enough_points);
                                }
                                errMsg = errMsg == null ? Constant.EMPTY : errMsg;
                                if (Log.isDLoggable(this)) {
                                    String err = null;
                                    try {
                                        err = new String(error.networkResponse.data, "UTF-8");
                                    } catch (Exception e) {
                                        Log.e(TAG, Log.getStackTraceString(e));
                                    }
                                    Log.d(TAG, new StringBuilder("onErrorResponse ").append(" err=").append(err).append(
                                            this).toString());
                                }
                                showAlert(getString(R.string.redeem_error, errMsg));
                                commonRequestErr(error);
                            }
                        };

                        JacksonRequest.RequestWithDlg<RedeemRewardOPO> st =
                                new JacksonRequest.RequestWithDlg<RedeemRewardOPO>
                                        (RedeemRewardOPO.class, Request.Method.POST,
                                                ApiHelper.getIns().getRedeemRewardUrl(mReward.getId()),
                                                ApiHelper.getIns().buildRedeemRewardRequest(access_token), dlg,
                                                listener,
                                                onErrorLis);
                        st.setContentType(ApiHelper.CONTENT_TYPE_FORM);
                        st.addSuccessCode(ApiHelper.REDEEM_REWARD_SUCCESS_CODE);
                        st.request(getApp().getRequestQueue(), getChildFragmentManager());
                    }
                }
            });
        }
    }
}

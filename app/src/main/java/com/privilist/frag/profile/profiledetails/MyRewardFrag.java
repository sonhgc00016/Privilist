package com.privilist.frag.profile.profiledetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.privilist.R;
import com.privilist.component.JacksonRequest;
import com.privilist.component.MyRewardAdapter;
import com.privilist.frag.ProgressDialogFrag;
import com.privilist.frag.base.BaseFrag;
import com.privilist.model.Reward;
import com.privilist.util.ApiHelper;
import com.privilist.util.UserHelper;
import com.privilist.util.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by SonH on 2015-06-30.
 */
public class MyRewardFrag extends BaseFrag implements Response.ErrorListener{

    private WeakReference<ListView> mLvRewards;
    private ArrayList<Reward> mArrRewards;

    public MyRewardFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_reward, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView lv;
        lv = (ListView) view.findViewById(R.id.myRewards_lvRewards);
        mLvRewards = new WeakReference<ListView>(lv);
        doGetMyRewwards();
    }

    private void doGetMyRewwards() {
        String access_token = UserHelper.getIns().getSavedAccessToken(getActivity());
        if (access_token != null) {
            ProgressDialogFrag.ProgressRequestDlg<ArrayList<Reward>> dlg = new
                    ProgressDialogFrag.ProgressRequestDlg<ArrayList<Reward>>();

            Response.Listener<ArrayList<Reward>> listener = new Response.Listener<ArrayList<Reward>>() {
                @Override
                public void onResponse(final ArrayList<Reward> response) {
                    mArrRewards = response;
                    MyRewardAdapter adapter = new MyRewardAdapter(mArrRewards, getActivity());
                    ListView lv;
                    lv = Utils.getVal(mLvRewards);
                    lv.setAdapter(adapter);
                    adapter.replace(mArrRewards);
                    adapter.notifyDataSetChanged();
                }
            };

            JacksonRequest.ArrayRequestWithDlg<Reward>
                    cr = new JacksonRequest.ArrayRequestWithDlg<Reward>(Reward.class,
                    ApiHelper.getIns().getMyRewardsUrl(access_token), dlg, listener, this);
            cr.setShouldCache(true);
            cr.request(getApp().getRequestQueue(), getChildFragmentManager());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        commonRequestErr(error);
    }
}

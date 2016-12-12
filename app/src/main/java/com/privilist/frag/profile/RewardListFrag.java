package com.privilist.frag.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.privilist.R;
import com.privilist.component.RewardAdapter;
import com.privilist.frag.base.BaseHeaderFrag;
import com.privilist.frag.profile.redeem.RedeemRewardFrag;
import com.privilist.model.Reward;
import com.privilist.model.User;
import com.privilist.util.Utils;

import java.util.ArrayList;

/**
 * Created by SonH on 2015-07-08.
 */
public class RewardListFrag extends BaseHeaderFrag implements AdapterView.OnItemClickListener, Response.ErrorListener {

    public RewardListFrag() {
    }

    private ListView mLvRewards;
    private User mUser;
    private ArrayList<Reward> mArrRewards;

    public void setmUser(User mUser) {
        this.mUser = mUser;
    }

    public void setmArrRewards(ArrayList<Reward> mArrRewards) {
        this.mArrRewards = mArrRewards;
    }

    @Override
    protected CharSequence getTitle() {
        return getString(R.string.rewards);
    }

    @Override
    protected View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reward_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mUser != null) {
            Utils.setText(view.findViewById(R.id.rewards_list_tvAvailable), "Available: " + String.valueOf(mUser.reward_points) + " Points");
        }
        if (mArrRewards != null) {
            mLvRewards = (ListView) view.findViewById(R.id.rewards_list_lvRewards);
            mLvRewards.setOnItemClickListener(this);

            RewardAdapter adapter = new RewardAdapter(mArrRewards, getActivity());
            mLvRewards.setAdapter(adapter);
            adapter.replace(mArrRewards);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Reward reward = mArrRewards.get(position);
        RedeemRewardFrag redeemRewardFrag = new RedeemRewardFrag();
        redeemRewardFrag.setmReward(reward);
        move(redeemRewardFrag);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        commonRequestErr(error);
    }
}

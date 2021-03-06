package com.privilist.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.privilist.R;
import com.privilist.model.Reward;
import com.privilist.util.GlideHelper;

import java.util.ArrayList;

/**
 * Created by SonH on 2015-07-10.
 */
public class MyRewardAdapter extends CommonAdapter {
    private ArrayList<Reward> mArrRewards;
    private Context mContext;

    public MyRewardAdapter(ArrayList<Reward> pArrRewars, Context pContext) {
        this.mArrRewards = pArrRewars;
        this.mContext = pContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_reward_item, parent, false);
            holder = new Holder();
            holder.imgBackground = (ImageView) view.findViewById(R.id.itemMyReward_imgReward);
            holder.tvTitle = (TextView) view.findViewById(R.id.itemMyReward_tvTitle);
            holder.tvCode = (TextView) view.findViewById(R.id.itemMyReward_tvCode);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        if (mArrRewards.get(position) != null) {
            if (mArrRewards.get(position).getImage() != null) {
                GlideHelper.loadImage(mArrRewards.get(position).getImage(), holder.imgBackground);
            }
            holder.tvTitle.setText(mArrRewards.get(position).getTitle());
            holder.tvCode.setText(mArrRewards.get(position).getPivot().getCode());
        }
        return view;
    }

    static class Holder {
        ImageView imgBackground;
        TextView tvTitle, tvCode;
    }
}

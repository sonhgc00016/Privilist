package com.privilist.component;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.GlideCircleTransform;
import com.privilist.R;
import com.privilist.model.RankDetails;
import com.privilist.util.GlideHelper;
import com.privilist.util.Utils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by SonH on 2015-06-30.
 */
public class RankingAdapter extends CommonAdapter {
    private ArrayList<RankDetails> mArrRanks;
    private Context mContext;

    public RankingAdapter(ArrayList<RankDetails> pArrRanks, Context pContext) {
        this.mArrRanks = pArrRanks;
        this.mContext = pContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_item, parent, false);
            holder = new Holder();
            holder.imgAvatar = (ImageView) view.findViewById(R.id.itemRanking_imgAvatar);
            holder.imgCard = (ImageView) view.findViewById(R.id.itemRanking_imgCard);
            holder.tvFullName = (TextView) view.findViewById(R.id.itemRanking_tvFullName);
            holder.tvPointType = (TextView) view.findViewById(R.id.itemRanking_tvPointType);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        if (mArrRanks.get(position) != null) {
            if (mArrRanks.get(position).getImage() != null && !mArrRanks.get(position).getImage().url.isEmpty() && !mArrRanks.get(position).getImage().url.equals(""))
                try {
                    URL url = new URL(mArrRanks.get(position).getImage().url);
                    Uri uri = Uri.parse(url.toURI().toString());
                    Glide.with(mContext)
                            .load(uri)
                            .transform(new GlideCircleTransform(mContext))
                            .into(holder.imgAvatar);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            if (!mArrRanks.get(position).getRank().getIcon().isEmpty() && !mArrRanks.get(position).getRank().getIcon().equals("")) {
                GlideHelper.loadImage(mArrRanks.get(position).getRank().getIcon(), holder.imgCard);
                holder.imgCard.setVisibility(View.VISIBLE);
            } else {
                holder.imgCard.setVisibility(View.GONE);
            }
            holder.tvFullName.setText((position + 1) + ". " + mArrRanks.get(position).getFull_name());
            holder.tvPointType.setText(mArrRanks.get(position).getRank().getName() +
                    "(" + mArrRanks.get(position).getReputation_points() + " POINTS)");
            holder.tvPointType.setTextColor(Color.parseColor(mArrRanks.get(position).getRank().getColor()));
        }
        return view;
    }

    static class Holder {
        ImageView imgAvatar, imgCard;
        TextView tvFullName, tvPointType;
    }
}

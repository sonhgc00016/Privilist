package com.privilist.frag.profile.profiledetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.privilist.R;
import com.privilist.component.JacksonRequest;
import com.privilist.component.RankingAdapter;
import com.privilist.frag.ProgressDialogFrag;
import com.privilist.frag.base.BaseFrag;
import com.privilist.model.RankDetails;
import com.privilist.util.ApiHelper;
import com.privilist.util.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * Created by SonH on 2015-06-30.
 */
public class RankingListFrag extends BaseFrag implements Response.ErrorListener{
    private ArrayList<RankDetails> mArrRankDetails;
    private WeakReference<ListView> mLvRanking;

    public RankingListFrag() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ranking, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView lv = (ListView) view.findViewById(R.id.ranking_lvRanking);
        mLvRanking = new WeakReference<ListView>(lv);
        doGetRank();
    }

    private void doGetRank() {
        ProgressDialogFrag.ProgressRequestDlg<ArrayList<RankDetails>> dlg = new
                ProgressDialogFrag.ProgressRequestDlg<ArrayList<RankDetails>>();

        Response.Listener<ArrayList<RankDetails>> listener = new Response.Listener<ArrayList<RankDetails>>() {
            @Override
            public void onResponse(final ArrayList<RankDetails> response) {
                mArrRankDetails = response;
                RankingAdapter adapter = new RankingAdapter(mArrRankDetails, getActivity());
                ListView lv = Utils.getVal(mLvRanking);
                lv.setAdapter(adapter);
                adapter.replace(mArrRankDetails);
                adapter.notifyDataSetChanged();
            }
        };
        JacksonRequest.ArrayRequestWithDlg<RankDetails>
                cr = new JacksonRequest.ArrayRequestWithDlg<RankDetails>(RankDetails.class,
                ApiHelper.getIns().getRankUrl(), dlg, listener, this);
        cr.setShouldCache(true);
        cr.request(getApp().getRequestQueue(), getChildFragmentManager());
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        commonRequestErr(error);
    }
}

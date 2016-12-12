package com.privilist.frag.profile.profiledetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.privilist.R;
import com.privilist.component.JacksonRequest;
import com.privilist.component.PointHistoryLastBookedAdapter;
import com.privilist.frag.ProgressDialogFrag;
import com.privilist.frag.base.BaseFrag;
import com.privilist.model.PointHistory;
import com.privilist.util.ApiHelper;
import com.privilist.util.UserHelper;
import com.privilist.util.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by SonH on 2015-06-29.
 */
public class PointHistoryFrag extends BaseFrag implements Response.ErrorListener {

    private PointHistory mPointHistory;
    private PointHistoryLastBookedAdapter mLastBookedAdapter = new PointHistoryLastBookedAdapter();
    private WeakReference<ListView> mLvLastBooked;
    private WeakReference<TextView> mTvPointAvailable, mTvPointAccumulated, mTvPointUtilUpgrade;

    public PointHistoryFrag() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_point_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doGetPointHistory();
        TextView tv;
        tv = (TextView) view.findViewById(R.id.pointHistory_tvPointAvailable);
        mTvPointAvailable = new WeakReference<TextView>(tv);
        tv = (TextView) view.findViewById(R.id.pointHistory_tvPointAccumulated);
        mTvPointAccumulated = new WeakReference<TextView>(tv);
        tv = (TextView) view.findViewById(R.id.pointHistory_tvPointUtilUpgrade);
        mTvPointUtilUpgrade = new WeakReference<TextView>(tv);

        ListView lv;
        lv = (ListView) view.findViewById(R.id.pointHistory_lvLastBooked);
        mLvLastBooked = new WeakReference<ListView>(lv);
        lv.setAdapter(mLastBookedAdapter);
        lv.setVisibility(View.GONE);
    }

    private void doGetPointHistory() {
        String access_token = UserHelper.getIns().getSavedAccessToken(getActivity());
        if (access_token != null) {
            ProgressDialogFrag.ProgressRequestDlg<PointHistory> dlg = new ProgressDialogFrag
                    .ProgressRequestDlg<PointHistory>();

            Response.Listener<PointHistory> mListener = new Response.Listener<PointHistory>() {
                @Override
                public void onResponse(final PointHistory response) {
                    if (getApp() != null) {
                        // TODO with response
                        mPointHistory = response;
                        TextView tv;
                        tv = Utils.getVal(mTvPointAvailable);
                        tv.setText(Html.fromHtml(getString(R.string.point_available, String.valueOf(mPointHistory.points.available))));

                        tv = Utils.getVal(mTvPointAccumulated);
                        tv.setText(Html.fromHtml(getString(R.string.point_accumulated, String.valueOf(mPointHistory.points.accumulated))));

                        tv = Utils.getVal(mTvPointUtilUpgrade);
                        tv.setText(Html.fromHtml(getString(R.string.point_util_upgrade, String.valueOf(mPointHistory.points.upgrade_points))));

                        if (mPointHistory.last_booked != null && mPointHistory.last_booked.size() > 0) {
                            ListView lv = Utils.getVal(mLvLastBooked);
                            lv.setVisibility(View.VISIBLE);
                            ArrayList<PointHistoryLastBookedAdapter.LastBookedItem> items = new ArrayList<PointHistoryLastBookedAdapter.LastBookedItem>();
                            items.addAll(mPointHistory.last_booked);
                            mLastBookedAdapter.replace(items);
                            mLastBookedAdapter.notifyDataSetChanged();
                        }
                    }
                }
            };

            JacksonRequest.RequestWithDlg<PointHistory> st =
                    new JacksonRequest.RequestWithDlg<PointHistory>
                            (PointHistory.class, ApiHelper.getIns().getPointHistoryUrl(access_token), dlg,
                                    mListener,
                                    this);
            st.setContentType(ApiHelper.CONTENT_TYPE_FORM);
            st.setShouldCache(true);
            st.request(getApp().getRequestQueue(), getChildFragmentManager());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        commonRequestErr(error);
    }
}
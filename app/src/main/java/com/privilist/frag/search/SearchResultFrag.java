package com.privilist.frag.search;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.privilist.app.PrivilistApp;
import com.privilist.component.EventListAdapter;
import com.privilist.component.JacksonRequest;
import com.privilist.frag.ProgressDialogFrag;
import com.privilist.frag.home.EventListFrag;
import com.privilist.model.EventsOPO;
import com.privilist.model.VenuesOPO;
import com.privilist.util.ApiHelper;

import java.util.ArrayList;

/**
 * Created by minhtdh on 6/23/15.
 */
public class SearchResultFrag extends EventListFrag {

    @Override
    public void fetchData() {
        PrivilistApp app = getApp();
        if (app == null) {
            return;
        }
        ProgressDialogFrag.ProgressRequestDlg<EventsOPO> dlg = new ProgressDialogFrag
                .ProgressRequestDlg<EventsOPO>();
        JacksonRequest.RequestWithDlg<EventsOPO> request = new JacksonRequest
                .RequestWithDlg<EventsOPO>(EventsOPO.class, ApiHelper.getIns
                ().getSearchData(location_id, SearchFrag.searchKey), dlg, new Response.Listener<EventsOPO>() {
            @Override
            public void onResponse(final EventsOPO response) {
                ArrayList<EventListAdapter.EventItem> items = null;
                if (response != null) {
                    items = new ArrayList<EventListAdapter.EventItem>();
                    addEvents(response.events, items,0);
                    addVenues(response.venues, items);
                }
                mAdapter.replace(items);
                mAdapter.notifyDataSetChanged();

            }
        }, this);
        request.request(app.getRequestQueue(), getChildFragmentManager());
        mRequest = request;
    }
}

package com.privilist.frag.home;

import com.android.volley.Response;
import com.privilist.app.PrivilistApp;
import com.privilist.component.EventListAdapter;
import com.privilist.component.JacksonRequest;
import com.privilist.frag.ProgressDialogFrag;
import com.privilist.model.VenuesOPO;
import com.privilist.util.ApiHelper;

import java.util.ArrayList;

/**
 * Created by minhtdh on 6/23/15.
 */
public class VenuesListFrag extends EventListFrag {
    @Override
    public void fetchData() {
        //luanlq July,31 2015
        EventListFrag.kindFrag=1;
        //luanlq July,31 2015 End.
        PrivilistApp app = getApp();
        if (app == null) {
            return;
        }
        ProgressDialogFrag.ProgressRequestDlg<VenuesOPO> dlg = new ProgressDialogFrag
                .ProgressRequestDlg<VenuesOPO>();
        JacksonRequest.RequestWithDlg<VenuesOPO> request = new JacksonRequest
                .RequestWithDlg<VenuesOPO>(VenuesOPO.class, ApiHelper.getIns
                ().getVenues(location_id), dlg, new Response.Listener<VenuesOPO>() {
            @Override
            public void onResponse(final VenuesOPO response) {
                ArrayList<EventListAdapter.EventItem> items = null;
                if (response != null) {
                    items = new ArrayList<EventListAdapter.EventItem>();
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

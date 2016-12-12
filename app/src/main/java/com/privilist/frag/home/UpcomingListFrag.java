package com.privilist.frag.home;

import com.android.volley.Response;
import com.privilist.app.PrivilistApp;
import com.privilist.component.EventListAdapter;
import com.privilist.component.JacksonRequest;
import com.privilist.frag.ProgressDialogFrag;
import com.privilist.model.EventsOPO;
import com.privilist.util.ApiHelper;

import java.util.ArrayList;

/**
 * Created by minhtdh on 6/23/15.
 */
public class UpcomingListFrag extends EventListFrag {
    @Override
    public void fetchData() {
        //luanlq July,31 2015
        EventListFrag.kindFrag=1;
        //luanlq July,31 2015 End.
        PrivilistApp app = getApp();
        if (app == null) {
            return;
        }
        ProgressDialogFrag.ProgressRequestDlg<EventsOPO> dlg = new ProgressDialogFrag
                .ProgressRequestDlg<EventsOPO>();
        JacksonRequest.RequestWithDlg<EventsOPO> request = new JacksonRequest
                .RequestWithDlg<EventsOPO>(EventsOPO.class, ApiHelper.getIns
                ().getUpcomingEvents(location_id), dlg, new Response.Listener<EventsOPO>() {
            @Override
            public void onResponse(final EventsOPO response) {
                ArrayList<EventListAdapter.EventItem> items = null;
                if (response != null) {
                    items = new ArrayList<EventListAdapter.EventItem>();
//                    addVenues(response.ads, items);
//                    addEvents(response.events, items);
                    //luanlq July 10,2015
                    addEvents(response.events, items,1);
                    //luanlq July 10,2015 End.
//                    addVenues(response.venues, items);
                }
                mAdapter.replace(items);
                mAdapter.notifyDataSetChanged();
            }
        }, this);
        request.request(app.getRequestQueue(), getChildFragmentManager());
        mRequest = request;
    }
}

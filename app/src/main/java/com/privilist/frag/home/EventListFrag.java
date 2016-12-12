package com.privilist.frag.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.privilist.R;
import com.privilist.component.EventListAdapter;
import com.privilist.component.JacksonRequest;
import com.privilist.define.Constant;
import com.privilist.frag.base.BaseFrag;
import com.privilist.model.City;
import com.privilist.model.Event;
import com.privilist.model.Venue;
import com.privilist.util.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by minhtdh on 6/17/15.
 */
public abstract class EventListFrag extends BaseFrag implements SwipeRefreshLayout
        .OnRefreshListener, Response.ErrorListener {
    protected EventListAdapter mAdapter = new EventListAdapter();
    protected long location_id = Constant.UNKNOW_ID;
    //luanlq July,31 2015
    public static int kindFrag=0;
    //TonightListFrag: kindFrag=0
    //VenuesListFrag: kindFrag=1
    //UpcomingListFrag: kindFrag=1
    //luanlq July,31 2015 End.
    private ListView lv;
    private boolean enable = false;

    public void setLocation(final City pLocation) {
        location_id = pLocation == null ? Constant.UNKNOW_ID : pLocation.id;
    }

    private AdapterView.OnItemClickListener mItemClickListener;

    public void setItemClickListener(
            final AdapterView.OnItemClickListener pItemClickListener) {
        mItemClickListener = pItemClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        if (savedInstanceState != null && location_id == Constant.UNKNOW_ID) {
            location_id = savedInstanceState.getLong(KEY_LOCATION, Constant.UNKNOW_ID);
        }
        Context c = inflater.getContext();
        FrameLayout parent = new FrameLayout(c);
        View empty = inflater.inflate(R.layout.big_text, parent, false);
        Utils.setText(empty.findViewById(android.R.id.title),
                c.getString(R.string.default_empty_msg));
        parent.addView(empty);

        lv = new ListView(c);
        lv.setOnItemClickListener(mItemClickListener);
        lv.setEmptyView(empty);
        lv.setAdapter(mAdapter);

        final SwipeRefreshLayout srl = new SwipeRefreshLayout(c);
        srl.setOnRefreshListener(this);
        srl.setRefreshing(false);
        srl.addView(lv);
        mSwipeRef = new WeakReference<SwipeRefreshLayout>(srl);
        parent.addView(srl);

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (lv != null && lv.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = lv.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = lv.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                srl.setEnabled(enable);
            }
        });
        return parent;
    }
    private static final String KEY_LOCATION = "key_location";
    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_LOCATION, location_id);
    }

    private WeakReference<SwipeRefreshLayout> mSwipeRef;
    @Override
    public void onRefresh() {
        SwipeRefreshLayout srl = Utils.getVal(mSwipeRef);
        if (srl.isRefreshing())
            srl.setRefreshing(false);
        fetchData();
    }

    public abstract void fetchData();

    protected JacksonRequest.RequestWithDlg mRequest;
    public void cancelCurrent() {
        if (mRequest != null) {
            mRequest.cancel();
        }
    }

    @Override
    public void onDestroyView() {
        cancelCurrent();
        super.onDestroyView();
    }

    @Override
    public void onErrorResponse(final VolleyError error) {
        if (isAdded()) {
            mAdapter.replace(null);
            mAdapter.notifyDataSetChanged();
        }
        commonRequestErr(error);
    }

    protected void addVenues(ArrayList<Venue> venues, ArrayList<EventListAdapter.EventItem> items) {
        if (venues != null) {
            for (Venue venue : venues) {
                //luanlq July,31 2015
                if(kindFrag == 0) {
                    if(venue.open) {
                        items.add(venue);
                    }
                }else if(kindFrag == 1){
                    items.add(venue);
                }
                //luanlq July,31 2015 End.
            }
        }
    }

//    protected void addEvents(ArrayList<Event> events, ArrayList<EventListAdapter.EventItem> items) {
//        if (events != null) {
//            for (Event event : events) {
//                items.add(event);
//            }
//        }
//    }

    //luanlq July 10,2015
    protected void addEvents(ArrayList<Event> events, ArrayList<EventListAdapter.EventItem> items,int kindEvent) {
        if (events != null) {
            for (Event event : events) {
                //luanlq July,31 2015
                if(kindFrag == 0) {
                    if (event.venue.open) {
                        items.add(event);
                    }
                }else if(kindFrag == 1){
                    items.add(event);
                }
                //luanlq July,31 2015 End.
            }
        }
        EventListAdapter.kindEvent=kindEvent;
    }
    //luanlq July 10,2015 End.
}

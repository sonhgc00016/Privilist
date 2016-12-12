package com.privilist.frag;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.privilist.R;
import com.privilist.app.PrivilistApp;
import com.privilist.component.JacksonRequest;
import com.privilist.component.TableAdapter;
import com.privilist.define.Constant;
import com.privilist.frag.base.BaseFrag;
import com.privilist.model.Event;
import com.privilist.model.EventForDateIPO;
import com.privilist.model.Table;
import com.privilist.model.TableListIPO;
import com.privilist.model.TableListOPO;
import com.privilist.util.ApiHelper;
import com.privilist.util.GlideHelper;
import com.privilist.util.Log;
import com.privilist.util.UserHelper;
import com.privilist.util.Utils;

import java.util.ArrayList;

/**
 * Created by minhtdh on 6/24/15.
 */
public class TableListFrag extends BaseFrag implements View.OnClickListener, AdapterView.OnItemClickListener, Response.ErrorListener, Response.Listener<TableListOPO> {

    private TableListIPO mInput;
    private TableAdapter mAdapter = new TableAdapter();
    //luanlq July,22 2015
    private View header = null;
    private EventForDateIPO eventInput;
    private LinearLayout event_descriptionLlt;
    //luanlq July,22 2015 End.
    //luanlq August 03,2015
    private View savedHeader = null;
    //luanlq August 03,2015End.
    //    public static TableListFrag newInstance(TableListIPO input, RequestHandler requesthandler) {
//        TableListFrag ret = new TableListFrag();
//        ret.mInput = input;
//        ret.mRequestHandler = requesthandler;
//        return ret;
//    }
    //luanlq July,22 2015
    public static TableListFrag newInstance(TableListIPO input, EventForDateIPO eventInput, RequestHandler requesthandler) {
        TableListFrag ret = new TableListFrag();
        ret.mInput = input;
        ret.eventInput = eventInput;
        ret.mRequestHandler = requesthandler;
        return ret;
    }

    private void showEventDescription(Event event) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.event_description, null, false);
        ImageView event_bgrIvw = (ImageView) v.findViewById(R.id.event_bgrIvw);
        TextView event_tagTvw = (TextView) v.findViewById(R.id.event_tagTvw);
        TextView event_titleTvw = (TextView) v.findViewById(R.id.event_titleTvw);
        TextView event_descTvw = (TextView) v.findViewById(R.id.event_descTvw);
        GlideHelper.loadImage(event.getDisplayImage(), event_bgrIvw);
        event_tagTvw.setText(event.tag_line);
        event_tagTvw.setBackgroundResource(R.drawable.ads_box_border);
        event_titleTvw.setText(event.title);
        event_descTvw.setText(Html.fromHtml(event.description));
        event_descriptionLlt.removeAllViews();
        event_descriptionLlt.addView(v);
    }

    //luanlq July,22 2015 End.
    public static class RequestInfo {
        public static final RequestInfo SPECIAL_REQUEST = new RequestInfo();
        public Table table;
    }

    public interface RequestHandler {
        void onRequest(RequestInfo pResquestInfo, TableListIPO input);
    }

    private RequestHandler mRequestHandler;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final
    Bundle savedInstanceState) {
        Context c = inflater.getContext();
        ListView lv = new ListView(c);
        //luanlq July,16 2015
        //to use listview inside scrollview
//        lv.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN:
//                        v.getParent().requestDisallowInterceptTouchEvent(true);
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//                        v.getParent().requestDisallowInterceptTouchEvent(false);
//                        break;
//                }
//                v.onTouchEvent(event);
//                return true;
//            }
//        });
        //luanlq July,16 2015 End.
        lv.setOnItemClickListener(this);
        mAdapter.currency = UserHelper.getIns().getCurrency();
        View footer = inflater.inflate(R.layout.btn_special_request, lv, false);
        //luanlq July,22 2015
        if(savedHeader == null) {
            header = inflater.inflate(R.layout.club_header, lv, false);
        }else{
            header=savedHeader;
        }
        event_descriptionLlt = (LinearLayout) header.findViewById(R.id.event_descriptionLlt);
        //luanlq July,22 2015 End.
        Utils.setOnclick(footer.findViewById(R.id.btn_special_request), this);
        lv.addFooterView(footer, null, false);
        lv.setDivider(getResources().getDrawable(R.drawable.table_item_divider_trans));
        lv.setDividerHeight(Math.round(TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));
        //luanlq July,22 2015
        lv.addHeaderView(header, null, false);
        //luanlq July,22 2015 End.
        lv.setAdapter(mAdapter);
        return lv;
    }

    public void fetchData() {
        //luanlq July 8,2015
        //remove check login
        if (mInput != null && mInput.venueID != Constant.UNKNOW_ID && mInput.date != null) {
            //luanlq July 8,2015 End.
            PrivilistApp app = getApp();
            if (app == null) {
                return;
            }
            mInput.access_token = UserHelper.getIns().getSavedAccessToken(getActivity());
            ProgressDialogFrag.ProgressRequestDlg<TableListOPO> dlg = new ProgressDialogFrag
                    .ProgressRequestDlg<TableListOPO>();
            JacksonRequest.RequestWithDlg<TableListOPO> request = new JacksonRequest
                    .RequestWithDlg<TableListOPO>(TableListOPO.class,
                    ApiHelper.getIns().getTableListUrl(mInput), dlg, this, this);
            request.request(app.getRequestQueue(), getChildFragmentManager());

            //luanlq July,22 2015
            ProgressDialogFrag.ProgressRequestDlg<ArrayList<Event>> eventDlg = new ProgressDialogFrag
                    .ProgressRequestDlg<ArrayList<Event>>();
            JacksonRequest.ArrayRequestWithDlg<Event>
                    eventRequest = new JacksonRequest.ArrayRequestWithDlg<Event>(Event.class,
                    ApiHelper.getIns().getEventForTableListUrl(eventInput), eventDlg,
                    new Response.Listener<ArrayList<Event>>() {
                        @Override
                        public void onResponse(ArrayList<Event> response) {
                            if (response != null && response.size() > 0) {
                                Event event = response.get(0);
                                showEventDescription(event);
                            } else {
                                event_descriptionLlt.removeAllViews();
                            }
                        }
                    }, this);
            eventRequest.request(app.getRequestQueue(), getChildFragmentManager());
            //luanlq July,22 2015 End.

            if (Log.isDLoggable(this)) {
                Log.d(TAG, new StringBuilder("fetchData ")
                        .append(ApiHelper.getIns().getTableListUrl(mInput)).append(this)
                        .toString());
            }
            mRequest = request;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //luanlq August 03,2015
        savedHeader=header;
        //luanlq August 03,2015 End.
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //luanlq August 03,2015
        savedHeader=null;
        //luanlq August 03,2015 End.
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onResponse(final TableListOPO response) {
        if (response != null) {
            mAdapter.replace(response.tables);
        } else {
            mAdapter.replace(null);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorResponse(final VolleyError error) {
        commonRequestErr(error);
    }

    protected JacksonRequest.RequestWithDlg mRequest;

    public void cancelCurrent() {
        if (mRequest != null) {
            mRequest.cancel();
        }
    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_special_request: {
                if (mRequestHandler != null) {
                    mRequestHandler.onRequest(RequestInfo.SPECIAL_REQUEST, mInput);
                }
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position,
                            final long id) {
        // SonH July 27, 2015
        // fix bug wrong position when click item list view
        // Listview have header and header is item position 0
//        Table item = mAdapter.getItem(position);
        Table item = mAdapter.getItem(position - 1);
        // SonH July 27, 2015 End.
        if (item != null) {
            if (item.table_left == 0) {
                alert(getString(R.string.table_sold_out));
                return;
            } else if (mRequestHandler != null) {
                RequestInfo request = new RequestInfo();
                request.table = item;
                mRequestHandler.onRequest(request, mInput);
            }
        }
    }

}

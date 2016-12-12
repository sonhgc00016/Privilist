package com.privilist.frag.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.privilist.R;
import com.privilist.component.EventListAdapter;
import com.privilist.component.JacksonRequest;
import com.privilist.component.widget.PrivilistEditText;
import com.privilist.frag.ClubFrag;
import com.privilist.frag.ProgressDialogFrag;
import com.privilist.frag.home.EventListFrag;
import com.privilist.model.Event;
import com.privilist.model.TableListIPO;
import com.privilist.model.TableListOPO;
import com.privilist.model.Venue;
import com.privilist.util.ApiHelper;
import com.privilist.util.UserHelper;
import com.privilist.util.Utils;

import java.lang.ref.WeakReference;

/**
 * Created by Le Quoc Luan on 7/1/2015.
 */
public class SearchFrag extends EventListFrag implements View.OnClickListener,AdapterView.OnItemClickListener {
    public static String searchKey;
    private PrivilistEditText searchEtt;
    private ViewPager searchVpr;
    private SearchAdapter searchAdapter;
    private WeakReference<ViewPager> mVP;
    private EventListFrag elf = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        searchEtt=(PrivilistEditText)v.findViewById(R.id.searchEtt);
        searchEtt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    doSearch();
                    return true;
                }
                return false;
            }
        });
        searchVpr=(ViewPager)v.findViewById(R.id.searchVpr);
        searchAdapter=new SearchAdapter(getChildFragmentManager());
        searchVpr.setAdapter(searchAdapter);
        searchVpr.setCurrentItem(0);
        mVP = new WeakReference<ViewPager>(searchVpr);
        return v;
    }
    private void getSearchKey(){
        searchKey=searchEtt.getText().toString().trim();
        if(!searchKey.equals(""))
            searchKey=searchKey.replace(" ","+");
    }
    private void doSearch(){
        hideKeyboard();
        getSearchKey();
        if(elf != null){
            elf.setLocation(UserHelper.getIns().getSelectedCity());
            elf.fetchData();
        }
    }
    private void hideKeyboard(){
        View view=getActivity().getCurrentFocus();
        if(view != null){
            InputMethodManager inputMethodManager=(InputMethodManager)getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE
            );
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.setOnclick(view.findViewById(R.id.search_backIvw), this);
        Utils.setOnclick(view.findViewById(R.id.searchIvw), this);
    }

    @Override
    public void fetchData() {

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.search_backIvw:
                finish();
                break;
            case R.id.searchIvw:
                doSearch();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(final AdapterView<?> parent,final View view, final int position, final long id) {
        final EventListAdapter.EventItem item =
                (EventListAdapter.EventItem) parent.getItemAtPosition(position);
        long venueID = 0;
        if (item instanceof Venue) {
            venueID = ((Venue) item).id;
        } else if (item instanceof Event) {
            venueID = ((Event) item).venue_id;
        }
        if (getApp() == null) {
            return;
        }
        TableListIPO tlo = new TableListIPO();
        tlo.venueID = venueID;
        tlo.access_token =UserHelper.getIns().getSavedAccessToken(getActivity());
                ProgressDialogFrag.ProgressRequestDlg<TableListOPO> dlg = new ProgressDialogFrag
                .ProgressRequestDlg<TableListOPO>();
        JacksonRequest.RequestWithDlg<TableListOPO> request = new JacksonRequest
                .RequestWithDlg<TableListOPO>(TableListOPO.class, ApiHelper.getIns()
                .getNoDateTableListUrl(tlo), dlg, new Response.Listener<TableListOPO>() {
            @Override
            public void onResponse(final TableListOPO response) {

                Venue venue = response;
                if (venue != null) {
                    ClubFrag cf = new ClubFrag();
                    if (item instanceof Event) {
                        cf.setEvent((Event) item);
                    }
                    cf.setVenue(venue);
                    move(cf);
                }
            }
        }, this);
        request.request(getApp().getRequestQueue(), getChildFragmentManager());
    }
    private class SearchAdapter extends FragmentPagerAdapter{

        public SearchAdapter(final FragmentManager fm) {
            super(fm);
        }
        SparseArray<WeakReference<EventListFrag>> mFrags=new SparseArray<WeakReference<EventListFrag>>();
        WeakReference<EventListFrag> mCurrentFrag;
        @Override
        public Fragment getItem(final int position) {
            switch (position) {
                case 0:
                    elf=new SearchResultFrag();
                    break;
            }
            elf.setItemClickListener(SearchFrag.this);
            return elf;
        }
        @Override
        public int getCount() {
            return 1;
        }
        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            EventListFrag elf = (EventListFrag) super.instantiateItem(container, position);
            mFrags.put(position, new WeakReference<EventListFrag>(elf));
            return elf;
        }

        @Override
        public void destroyItem(final ViewGroup container, final int position,
                                final Object object) {
            mFrags.delete(position);
            super.destroyItem(container, position, object);
        }
    }
}

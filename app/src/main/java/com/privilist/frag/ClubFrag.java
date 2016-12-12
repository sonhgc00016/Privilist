package com.privilist.frag;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.privilist.R;
import com.privilist.component.CommonFragPagerAdt;
import com.privilist.component.JacksonRequest;
import com.privilist.component.RecyclerViewDateAdapter;
import com.privilist.component.widget.MyLinearLayoutManager;
import com.privilist.component.widget.TabIndicator;
import com.privilist.define.Constant;
import com.privilist.frag.base.BaseHeaderFrag;
import com.privilist.frag.book.SelectDrinkFrag;
import com.privilist.model.DrinkCategory;
import com.privilist.model.Event;
import com.privilist.model.EventForDateIPO;
import com.privilist.model.Image;
import com.privilist.model.TableListIPO;
import com.privilist.model.Venue;
import com.privilist.util.ApiHelper;
import com.privilist.util.GlideHelper;
import com.privilist.util.Utils;
import com.viewpagerindicator.IconPageIndicator;
import com.viewpagerindicator.IconPagerAdapter;

import org.joda.time.LocalDate;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by minhtdh on 6/23/15.
 */
public class ClubFrag extends BaseHeaderFrag
        implements View.OnClickListener, TableListFrag.RequestHandler, ViewPager.OnPageChangeListener, Response.ErrorListener, RecyclerViewDateAdapter.IMyViewHolderClicks {
    private Venue mVenue;
    private Event mEvent;
    private TableAdapter mTableAdapter;
//    private TabIndicator tpi;
    //luanlq August 03,2015
    private boolean isFirsLoad=false;
    //luanlq August 03,2015 End.

    private ViewPager mVpTable;
    private RecyclerViewDateAdapter mRecyclerViewDateAdapter;
    private LinearLayoutManager mLayoutManager;
    private int mCurrentFirstVisibleRecyclerViewPosition = 0;
    private int mCurrentViewPagerPosition = 0;

    public void setVenue(final Venue pVenue) {
        mVenue = pVenue;
    }

    public void setEvent(final Event pEvent) {
        mEvent = pEvent;
    }

    @Override
    protected CharSequence getTitle() {
        return mVenue == null ? null : mVenue.name;
    }

    @Override
    protected View createContentView(final LayoutInflater inflater, final ViewGroup container,
                                     final Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.club_frag, container, false);
        mVpTable = (ViewPager) ret.findViewById(R.id.pager_images);
        ImagePageAdapter imgAdapter = new ImagePageAdapter(getChildFragmentManager());
        imgAdapter.replace(mVenue == null ? null : mVenue.images);
        mVpTable.setAdapter(imgAdapter);
        IconPageIndicator ipi = (IconPageIndicator) ret.findViewById(R.id.pager_images_indicator);
        ipi.setViewPager(mVpTable);
        mVpTable = (ViewPager) ret.findViewById(R.id.pager_days);
        mTableAdapter = new TableAdapter(getChildFragmentManager());
        LocalDate ld;
        int numOfDays = 14;
        /*if (mEvent != null) {
            LocalDateTime startTime = LocalDateTime.parse(mEvent.start_date, DateTimeFormat
                    .forPattern(Constant.DATE_FORMAT));
            LocalDateTime endTime = LocalDateTime.parse(mEvent.end_date, DateTimeFormat
                    .forPattern(Constant.DATE_FORMAT));
            LocalDateTime ldt = LocalDateTime.now();
            int dif = Days.daysBetween(ldt, endTime).getDays();
            dif = dif < 0 ? 0 : dif;
            if (Log.isDLoggable(this)) {
                Log.d(TAG, new StringBuilder("createContentView ").append(" dif=").append(dif).append(
                        this).toString());
            }
            numOfDays = numOfDays > dif ? dif : numOfDays;
        }*/
        ArrayList<LocalDate> mDates = new ArrayList<LocalDate>(14);
        for (int i = 0; i<numOfDays; i++) {
            ld = LocalDate.now();
            mDates.add(ld.plusDays(i));
        }

        // initial recyclerview
        final RecyclerView rvDate = (RecyclerView) ret.findViewById(R.id.rvDate);
        // Create adapter passing in the sample user data
        ArrayList<String> arrayStringDate = new ArrayList<String>();
        for (LocalDate localDate : mDates) {
            arrayStringDate.add(localDate.toString("MM/dd'\n'EEE"));
        }
        mRecyclerViewDateAdapter = new RecyclerViewDateAdapter(arrayStringDate, getActivity(), this);
        // Attach the adapter to the recyclerview to populate items
        rvDate.setAdapter(mRecyclerViewDateAdapter);
        // Setup layout manager for items
        mLayoutManager = new MyLinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        // Control orientation of the items
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        // Attach layout manager to the RecyclerView
        rvDate.setLayoutManager(mLayoutManager);
        rvDate.setHasFixedSize(true);
        rvDate.setItemAnimator(new DefaultItemAnimator());

        rvDate.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int firstVisiblePosition = mLayoutManager.findFirstVisibleItemPosition();
                    if (firstVisiblePosition < mCurrentFirstVisibleRecyclerViewPosition && !(firstVisiblePosition >= 6)) {
                        // scroll to left
                        mLayoutManager.scrollToPositionWithOffset(0, 0);
                        mVpTable.setCurrentItem(0, false);
                        mRecyclerViewDateAdapter.setSelectedPosition(0);
                    } else if (firstVisiblePosition > mCurrentFirstVisibleRecyclerViewPosition) {
                        //scroll to right
                        mLayoutManager.scrollToPositionWithOffset(7, 0);
                        mVpTable.setCurrentItem(7, false);
                        mRecyclerViewDateAdapter.setSelectedPosition(7);
                    }
                    mRecyclerViewDateAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        // set selected date first time load
        mRecyclerViewDateAdapter.setSelectedPosition(mCurrentFirstVisibleRecyclerViewPosition);

//        tpi = (TabIndicator) ret.findViewById(R.id.pager_days_indicator);
//        tpi.setTextConfigure(this);
//        tpi.setOnPageChangeListener(this);

        mTableAdapter.replace(mDates);
        mTableAdapter.venueId = mVenue == null ? Constant.UNKNOW_ID : mVenue.id;
        mVpTable.setAdapter(mTableAdapter);
        mVpTable.addOnPageChangeListener(this);

//        tpi.setViewPager(mVpTable);
        Utils.setText(ret.findViewById(android.R.id.title), mVenue == null ? null : mVenue.name.toUpperCase());
        Utils.setOnclick(ret.findViewById(R.id.img_compass), this);
        Utils.setOnclick(ret.findViewById(R.id.img_info), this);
        //luanlq July 08,2015
        if(!isFirsLoad) {
            loadFirst();
            isFirsLoad=true;
        }
        //luanlq July 08,2015 End.
        return ret;
    }
    //luanlq July 08,2015
    private void loadFirst(){
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                reloadPage(0);
            }
        },350);
    }
    //luanlq July 08,2015 End.

//    //luanlq July 16,2015
//    private void showEventDescription(){
//        if(mEvent != null) {
//            LayoutInflater inflater = LayoutInflater.from(getActivity());
//            View v = inflater.inflate(R.layout.event_description, null, false);
//            ImageView event_bgrIvw = (ImageView) v.findViewById(R.id.event_bgrIvw);
//            TextView event_tagTvw = (TextView) v.findViewById(R.id.event_tagTvw);
//            TextView event_titleTvw = (TextView) v.findViewById(R.id.event_titleTvw);
//            TextView event_descTvw = (TextView) v.findViewById(R.id.event_descTvw);
//            GlideHelper.loadImage(mEvent.getDisplayImage(),event_bgrIvw);
//            event_tagTvw.setBackgroundResource(R.drawable.ads_box_border);
//            event_tagTvw.setText("EVENT NIGHT");
//            event_titleTvw.setText(mEvent.title);
//            event_descTvw.setText(mEvent.description);
//            event_descLlt.addView(v);
//        }
//    }
//    //luanlq July 16,2015 End.
    @Override
    public void onClick(final View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_compass: {
                openMap();
            }
            break;
            case R.id.img_info: {
                ClubInfoFrag cif = new ClubInfoFrag();
                cif.setVenue(mVenue);
                move(cif);
            }
            break;
            default:
                break;
        }
    }

    private void openMap() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (status == ConnectionResult.SUCCESS) {
            MapFrag frag = new MapFrag();
            frag.venue = mVenue;
            move(frag);
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 10).show();
        }
    }

    private static final String DATE_DISPLAY_FORMAT = "MM/dd'\n'EEE";
    private String[] dayOfWeeks = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    private static final String DATE_COMPARE_FORMAT = "EEE";
    private static final String DATE_ALERT_FORMAT = "EEEEEEEEE";

    @Override
    public void onRequest(final TableListFrag.RequestInfo pResquestInfo, final TableListIPO input) {
        if (pResquestInfo == TableListFrag.RequestInfo.SPECIAL_REQUEST) {
            SpecialRequestFrag srf = new SpecialRequestFrag();
            srf.mDate = input == null ? null : input.date;
            srf.venueID = mVenue == null ? Constant.UNKNOW_ID : mVenue.id;
            srf.minPrice = mVenue == null ? null : mVenue.min_price;
            move(srf);
        } else if (mVenue != null && getApp() != null) {
            // SonH August 03, 2015
            // Add check close date of Club, if closed click on table do nothing
            if(mVenue.schedule != null){
                ArrayList<String> arrClosedDates = new ArrayList<String>();
                for (int i = 0; i < dayOfWeeks.length; i++) {
                    if (Utils.isEmpty(mVenue.schedule.returnDay(i).open) || Utils.isEmpty(mVenue.schedule.returnDay(i).close)) {
                        arrClosedDates.add(dayOfWeeks[i]);
                    }
                }
                if (input != null && input.date != null) {
                    String date = input.date.toString(DATE_COMPARE_FORMAT);
                    for (String dateClosed : arrClosedDates) {
                        if (Utils.compare(date, dateClosed)) {
                            alert(getString(R.string.club_closed, input.date.toString(DATE_ALERT_FORMAT)));
                            return;
                        }
                    }
                }
            }
            // SonH August 03, 2015 End.
            ProgressDialogFrag.ProgressRequestDlg<ArrayList<DrinkCategory>> dlg = new
                    ProgressDialogFrag.ProgressRequestDlg<ArrayList<DrinkCategory>>();
            JacksonRequest.ArrayRequestWithDlg<DrinkCategory> request = new JacksonRequest
                    .ArrayRequestWithDlg<DrinkCategory>(DrinkCategory.class, ApiHelper.getIns()
                    .getDrinkUrl(mVenue.id), dlg,
                    new Response.Listener<ArrayList<DrinkCategory>>() {
                        @Override
                        public void onResponse(final ArrayList<DrinkCategory> response) {
                            if (response != null && mVenue != null && input != null) {
                                int eventId = 0;
                                if (mEvent != null) {
                                    eventId = (int) mEvent.id;
                                }
                                SelectDrinkFrag frag = SelectDrinkFrag.newInstance(pResquestInfo
                                        .table, mVenue, input.date, eventId);
                                frag.setItems(response);
                                move(frag);
                            }
                        }
                    }, this);
            request.request(getApp().getRequestQueue(), getChildFragmentManager());
        }
    }

   /* @Override
    public void configureText(final TextView tv) {
        tv.setTextColor(getResources().getColorStateList(R.color.pager_title_text));
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(getResources().getDimensionPixelSize(R.dimen.common_space) / 2, 0,
                getResources().getDimensionPixelSize(R.dimen.common_space) / 2, 0);
        Typeface font = Typeface.createFromAsset(getResources().getAssets(),
                getResources().getString(R.string.font_gotham_book));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        tv.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.0f,  getResources().getDisplayMetrics()), 1.0f);
        tv.setAllCaps(true);
        tv.setTypeface(font);
    }*/

    @Override
    public void onPageScrolled(final int position, final float positionOffset,
                               final int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(final int position) {
        reloadPage(position);
        if (position == 0 || position == 7)
            mCurrentFirstVisibleRecyclerViewPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(final int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            int position = mVpTable.getCurrentItem();
            if (position == 7 && mCurrentViewPagerPosition < position) {
                // scroll one week to right
                mLayoutManager.scrollToPositionWithOffset(7, 0);
                mRecyclerViewDateAdapter.setSelectedPosition(7);
            } else if (position == 6 && mCurrentViewPagerPosition > position) {
                // scroll one week to left
                mLayoutManager.scrollToPositionWithOffset(0, 0);
                mRecyclerViewDateAdapter.setSelectedPosition(6);
            }
            mRecyclerViewDateAdapter.setSelectedPosition(position);
            mRecyclerViewDateAdapter.notifyDataSetChanged();
            mCurrentViewPagerPosition = position;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void reloadPage(int position) {
        TableListFrag frag = Utils.getVal(mTableAdapter.mCurrentFrag);
        if (frag != null) {
            frag.cancelCurrent();
        }
        frag = Utils.getVal(mTableAdapter.mFrags.get(position));
        if (frag != null) {
            frag.fetchData();
        }
        mTableAdapter.mCurrentFrag = new WeakReference<TableListFrag>(frag);
    }

    @Override
    public void onErrorResponse(final VolleyError error) {
        commonRequestErr(error);
    }

    @Override
    public void onDateClick(View view, int position) {
        mVpTable.setCurrentItem(position, false);
        mRecyclerViewDateAdapter.setSelectedPosition(position);
        mRecyclerViewDateAdapter.notifyDataSetChanged();
    }

    private class TableAdapter extends CommonFragPagerAdt<LocalDate> {

        SparseArray<WeakReference<TableListFrag>> mFrags = new
                SparseArray<WeakReference<TableListFrag>>();
        WeakReference<TableListFrag> mCurrentFrag;

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            TableListFrag frag = (TableListFrag) super.instantiateItem(container, position);
            mFrags.put(position, new WeakReference<TableListFrag>(frag));
            return frag;
        }

        @Override
        public void destroyItem(final ViewGroup container, final int position,
                                final Object object) {
            mFrags.delete(position);
            super.destroyItem(container, position, object);
        }

        public long venueId = Constant.UNKNOW_ID;
        public TableAdapter(final FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
//            TableListIPO tlo = new TableListIPO();
//            tlo.venueID = venueId;
//            tlo.date = mItems.get(position);
//            TableListFrag tlf = TableListFrag.newInstance(tlo, ClubFrag.this);
            //luanlq July,22 2015
            TableListIPO tlo = new TableListIPO();
            EventForDateIPO epo=new EventForDateIPO();
            tlo.venueID = venueId;
            tlo.date = mItems.get(position);
            epo.venueID=venueId;
            epo.date=mItems.get(position);
            TableListFrag tlf = TableListFrag.newInstance(tlo,epo,ClubFrag.this);
            //luanlq July,22 2015 End.
            return tlf;
        }

//        @Override
//        public CharSequence getPageTitle(final int position) {
//            LocalDate date = mItems.get(position);
//            return date == null ? null : date.toString(DATE_DISPLAY_FORMAT);
//        }
    }

    public static class ImagePageAdapter extends CommonFragPagerAdt<Image> implements
            IconPagerAdapter {

        public ImagePageAdapter(final FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(final int position) {
            ImageFrag ret = new ImageFrag();
            ret.mImage = mItems.get(position);
            return ret;
        }

        @Override
        public int getIconResId(final int i) {
            return R.drawable.introduction_pager;
        }
    }

    public static class ImageFrag extends Fragment {
        Image mImage;
        @Nullable
        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final
        Bundle savedInstanceState) {
            ImageView iv = new ImageView(inflater.getContext());
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            GlideHelper.loadImage(mImage, iv);
            return iv;
        }
    }
}

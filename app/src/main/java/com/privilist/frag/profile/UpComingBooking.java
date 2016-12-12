package com.privilist.frag.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.FacebookSdk;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.privilist.R;
import com.privilist.component.CommonFragPagerAdt;
import com.privilist.component.JacksonRequest;
import com.privilist.define.Constant;
import com.privilist.frag.ClubInfoFrag;
import com.privilist.frag.MapFrag;
import com.privilist.frag.ProgressDialogFrag;
import com.privilist.frag.base.BaseHeaderFrag;
import com.privilist.model.Booking;
import com.privilist.model.Image;
import com.privilist.model.Venue;
import com.privilist.util.ApiHelper;
import com.privilist.util.GlideHelper;
import com.privilist.util.Log;
import com.privilist.util.Utils;
import com.viewpagerindicator.IconPageIndicator;
import com.viewpagerindicator.IconPagerAdapter;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by SonH on 2015-07-03.
 */
public class UpComingBooking extends BaseHeaderFrag implements View.OnClickListener {
    private Booking mBooking;
    private WeakReference<TextView> mTvName, mTvDate, mTvCode, mTvTableType, mTvGuestLimit, mTvTotal;
    private WeakReference<ViewGroup> mLnlBottleService;
    private Venue mVenue;
    private ShareDialog shareDialog;

    public void setmBooking(Booking mBooking) {
        this.mBooking = mBooking;
    }

    public UpComingBooking() {
    }

    @Override
    protected CharSequence getTitle() {
        String title = null;
        if (mBooking != null) {
            if (mBooking.getExpired())
                title = getString(R.string.expired_booking);
            else
                title = getString(R.string.upcoming_booking);
        }
        return title;
    }

    @Override
    protected View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upcomming_booking, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Facebook SDK
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        shareDialog = new ShareDialog(this);

        ViewPager vpImage = (ViewPager) view.findViewById(R.id.upcomming_vpImage);
        ImagePageAdapter imgAdapter = new ImagePageAdapter(getChildFragmentManager());
        if (mBooking != null) {
            doGetBookingInfo();
            if (mBooking.getTable() != null) {
                mVenue = mBooking.getTable().getVenue();
                imgAdapter.replace(mBooking.getTable().getVenue().images == null ? null : mBooking.getTable().getVenue().images);
            } else {
                Image image = new Image();
                image.url = "http://vignette4.wikia.nocookie.net/after-school/images/7/78/400px-Mystic_WHITE.jpg/revision/latest?cb=20131218170815";
                ArrayList<Image> images = new ArrayList<Image>();
                images.add(image);
                imgAdapter.replace(images);
            }
            vpImage.setAdapter(imgAdapter);
            IconPageIndicator ipi = (IconPageIndicator) view.findViewById(R.id.upcomming_indicator);
            ipi.setViewPager(vpImage);

            TextView tv;
            tv = (TextView) view.findViewById(R.id.upcomming_tvName);
            mTvName = new WeakReference<TextView>(tv);

            tv = (TextView) view.findViewById(R.id.upcomming_tvDate);
            mTvDate = new WeakReference<TextView>(tv);

            tv = (TextView) view.findViewById(R.id.upcomming_tvCode);
            mTvCode = new WeakReference<TextView>(tv);

            tv = (TextView) view.findViewById(R.id.upcomming_tvTableType);
            mTvTableType = new WeakReference<TextView>(tv);

            tv = (TextView) view.findViewById(R.id.upcomming_tvGuestLimit);
            mTvGuestLimit = new WeakReference<TextView>(tv);

            ViewGroup lnlBottleService = (ViewGroup) view.findViewById(R.id.upcomming_lnlBottleService);
            mLnlBottleService = new WeakReference<ViewGroup>(lnlBottleService);

            tv = (TextView) view.findViewById(R.id.upcomming_tvTotal);
            mTvTotal = new WeakReference<TextView>(tv);

            Utils.setOnclick(view.findViewById(R.id.upcomming_imgInfo), this);
            Utils.setOnclick(view.findViewById(R.id.upcomming_imgLocation), this);
            Utils.setOnclick(view.findViewById(R.id.btnShare), this);
        }
    }

    private void doGetBookingInfo() {
        ProgressDialogFrag.ProgressRequestDlg<Booking> dlg = new ProgressDialogFrag
                .ProgressRequestDlg<Booking>();

        Response.Listener<Booking> mListener = new Response.Listener<Booking>() {
            @Override
            public void onResponse(final Booking response) {
                LocalDate localDate = LocalDate.parse((response.getBooking_date()),
                        DateTimeFormat.forPattern(Constant.DATE_FORMAT));
                TextView tv;

                tv = Utils.getVal(mTvDate);
                tv.setText(localDate.toString("MMMM dd, yyyy"));

                tv = Utils.getVal(mTvCode);
                tv.setText(response.getCode());

                if (response.getTable() != null) {
                    tv = Utils.getVal(mTvName);
                    tv.setText(response.getTable().getVenue().name);

                    tv = Utils.getVal(mTvTableType);
                    tv.setText(response.getTable().table_type);

                    tv = Utils.getVal(mTvGuestLimit);
                    tv.setText("Up to " + response.getTable().people + " Guests");
                } else {
                    tv = Utils.getVal(mTvName);
                    tv.setText("N/A");

                    tv = Utils.getVal(mTvTableType);
                    tv.setText("N/A");

                    tv = Utils.getVal(mTvGuestLimit);
                    tv.setText("N/A");
                }
                if (response.getDetails() != null && response.getDetails().size() > 0) {
                    LayoutInflater inflater = (LayoutInflater) getActivity()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view;
                    ViewGroup vg;
                    vg = Utils.getVal(mLnlBottleService);
                    for (Booking.Details details : response.getDetails()) {
                        if (details.getDrink() != null) {
                            view = inflater.inflate(R.layout.bottle_service_item, vg, false);
                            TextView tvName = (TextView) view.findViewById(R.id.itemBottleService_tvName);
                            tvName.setText(details.getDrink().name);
                            TextView tvQuantityCost = (TextView) view.findViewById(R.id.itemBottleService_tvQuanityCost);
                            tvQuantityCost.setText(response.getCurrency() +" "+ String.format("%.2f", Float.parseFloat(details.getDrink().price)) + " x " + details.getQuantity());
                            vg.addView(view);
                        }
                    }
                }
                tv = Utils.getVal(mTvTotal);
                tv.setText(response.getCurrency() + " " + String.format("%.2f", response.getTotal()));
            }
        };

        Response.ErrorListener onErrorLis = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                CharSequence errMsg = error == null ? Constant.EMPTY : error.getLocalizedMessage();
                errMsg = errMsg == null ? Constant.EMPTY : errMsg;
                if (Log.isDLoggable(this)) {
                    String err = null;
                    try {
                        err = new String(error.networkResponse.data, "UTF-8");
                    } catch (Exception e) {
                        Log.e(TAG, Log.getStackTraceString(e));
                    }
                    Log.d(TAG, new StringBuilder("onErrorResponse ").append(" err=").append(err).append(
                            this).toString());
                }
                showAlert(getString(R.string.getting_data_error, errMsg));
                commonRequestErr(error);
            }
        };

        JacksonRequest.RequestWithDlg<Booking> st =
                new JacksonRequest.RequestWithDlg<Booking>
                        (Booking.class, ApiHelper.getIns().getBookingInfo(mBooking.getBooking_id()), dlg,
                                mListener,
                                onErrorLis);
        st.setContentType(ApiHelper.CONTENT_TYPE_FORM);
        st.request(getApp().getRequestQueue(), getChildFragmentManager());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.upcomming_imgLocation:
                openMap();
                break;
            case R.id.upcomming_imgInfo:
                ClubInfoFrag cif = new ClubInfoFrag();
                cif.setVenue(mVenue);
                move(cif);
                break;
            case R.id.btnShare:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mBooking.getCode());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share via"));
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

package com.privilist.frag.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.privilist.R;
import com.privilist.act.base.BaseAct;
import com.privilist.component.EventListAdapter;
import com.privilist.component.JacksonRequest;
import com.privilist.frag.CityFrag;
import com.privilist.frag.ClubFrag;
import com.privilist.frag.profile.ProfileFrag;
import com.privilist.frag.ProgressDialogFrag;
import com.privilist.frag.base.BaseFrag;
import com.privilist.frag.search.SearchFrag;
import com.privilist.model.City;
import com.privilist.model.Event;
import com.privilist.model.TableListIPO;
import com.privilist.model.TableListOPO;
import com.privilist.model.Venue;
import com.privilist.util.ApiHelper;
import com.privilist.util.UserHelper;
import com.privilist.util.Utils;
import com.viewpagerindicator.TitlePageIndicator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * Created by minhtdh on 6/17/15.
 */
public class HomeFrag extends BaseFrag implements View.OnClickListener, AdapterView.OnItemClickListener, Response.ErrorListener, BaseAct.OnBackPress {

    private HomeAdapter mAdapter;
    private WeakReference<TextView> mTvLocation;
    private City mCurrentLocation;
    private WeakReference<ViewPager> mVP;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        mCurrentLocation = UserHelper.getIns().getSelectedCity();
        View v = inflater.inflate(R.layout.home_frag, container, false);

        final ViewPager vp = (ViewPager) v.findViewById(R.id.pager_tab);
        mAdapter = new HomeAdapter(getChildFragmentManager());
        vp.setAdapter(mAdapter);
        TitlePageIndicator tpi = (TitlePageIndicator) v.findViewById(R.id.pager_tab_indicator);
        tpi.setViewPager(vp);

        vp.setCurrentItem(DEFAULT_PAGE);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                reloadPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // SonH July 10, 2015
                // add for endless viewpager
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int curr = vp.getCurrentItem();
                    int lastReal = vp.getAdapter().getCount() - 2;
                    if (curr == 0) {
                        vp.setCurrentItem(lastReal, false);
                    } else if (curr > lastReal) {
                        vp.setCurrentItem(1, false);
                    }
                }
                // SonH July 10, 2015 End.
            }
        });
        mVP = new WeakReference<ViewPager>(vp);

        TextView tv = (TextView) v.findViewById(R.id.tv_city);
        if (mCurrentLocation != null) {
            tv.setText(mCurrentLocation.name);
        }
        mTvLocation = new WeakReference<TextView>(tv);
        Utils.setOnclick(v.findViewById(R.id.location_container), this);
        Utils.setOnclick(v.findViewById(R.id.rltProfile), this);
        Utils.setOnclick(v.findViewById(R.id.rltSearch), this);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        // SonH July 27, 2015
        // register back press
        if (getActivity() instanceof BaseAct) {
            ((BaseAct) getActivity()).registeronBackPress(this);
        }
        // SonH July 27, 2015 End.

        if (mCurrentLocation == null) {
            City city = UserHelper.getIns().getSelectedCity();
            if (city == null) {
                View v = getView() == null ? null : getView().findViewById(R.id.location_container);
                if (v != null) {
                    v.performClick();
                }
            } else {
                setLocation(city);
            }
        }
        //luanlq July,13 2015
        else {
            if (Utils.isBookSuccess) {
                ViewPager vp = Utils.getVal(mVP);
                if (vp != null) {
                    reloadPageForBookSuccess(vp.getCurrentItem());
                }
                Utils.isBookSuccess = false;
            }
        }
        //luanlq July,13 2015 End.
    }

    // SonH July 27, 2015
    // unregister back press
    @Override
    public void onPause() {
        if (getActivity() instanceof BaseAct) {
            ((BaseAct) getActivity()).unregisteronBackPress(this);
        }
        super.onPause();
    }
    // SonH July 27, 2015 End.

    private void reloadPage(int position) {
        // SonH July 10, 2015
        // Edit for endless viewpager
        int dataPos = position - 1;
        if (position == 0) {
            dataPos = NUM_OF_TAB - 1;
        } else if (position > NUM_OF_TAB) {
            dataPos = 0;
        }
        EventListFrag elf = Utils.getVal(mAdapter.mCurrentFrag);
        if (elf != null) {
            elf.cancelCurrent();
        }
        elf = Utils.getVal(mAdapter.mFrags.get(dataPos));
        // SonH July 10, 2015 End.
        if (elf != null && mCurrentLocation != null) {
            elf.setLocation(mCurrentLocation);
            elf.fetchData();
        }
        mAdapter.mCurrentFrag = new WeakReference<EventListFrag>(elf);
    }

    //luanlq July,13 2015
    private void reloadPageForBookSuccess(final int position) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                reloadPage(position);
            }
        }, 100);
    }

    //luanlq July,13 2015 End.
    public void setLocation(City newCity) {
        TextView tv = Utils.getVal(mTvLocation);
        if (mCurrentLocation != newCity) {
            mCurrentLocation = newCity;
            UserHelper.getIns().setSelectedCity(mCurrentLocation);
            if (tv != null) {
                tv.setText(mCurrentLocation == null ? null : mCurrentLocation.name);
            }
            ViewPager vp = Utils.getVal(mVP);
            if (vp != null) {
                reloadPage(vp.getCurrentItem());
            }
        }
    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();
        switch (id) {
            case R.id.location_container: {
                ProgressDialogFrag.ProgressRequestDlg<ArrayList<City>> dlg = new
                        ProgressDialogFrag.ProgressRequestDlg<ArrayList<City>>();
//                dlg.setMsg(getString(R.string.loading_cities));
                Response.Listener<ArrayList<City>> listener = new Response.Listener<ArrayList<City>>() {
                    @Override
                    public void onResponse(final ArrayList<City> response) {
                        CityFrag cf = CityFrag.newInstance(HomeFrag.this, response);
                        cf.show(getChildFragmentManager(), cf.getClass().getSimpleName());
                    }
                };
                JacksonRequest.ArrayRequestWithDlg<City>
                        cr = new JacksonRequest.ArrayRequestWithDlg<City>(City.class,
                        ApiHelper.getIns().getLocationsUrl(), dlg, listener, this);
                cr.setShouldCache(true);
                cr.request(getApp().getRequestQueue(), getChildFragmentManager());
            }
            break;
            case R.id.rltProfile: {
                // SonH July 09, 2015
                ProfileFrag profileFrag = new ProfileFrag();
                profileFrag.setmCurrentLocation(mCurrentLocation);
                move(profileFrag);
                // SonH July 09, 2015 End.
            }
            break;
            case R.id.rltSearch: {
                // luanlq July 01, 2015
                SearchFrag searchFrag = new SearchFrag();
                move(searchFrag);
                // luanlq July 01, 2015 End.
            }
            break;
            default:
                break;
        }

    }


    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position,
                            final long id) {
        final EventListAdapter.EventItem item =
                (EventListAdapter.EventItem) parent.getItemAtPosition(position);
        long venueID = 0;
        if (item instanceof Venue) {
            venueID = ((Venue) item).id;
        } else if (item instanceof Event) {
            venueID = ((Event) item).venue_id;
        }

//        if (!UserHelper.getIns().isLogin()) {
//            showAlert(getString(R.string.login_timeout));
//            return;
//        }
        if (getApp() == null) {
            return;
        }
        TableListIPO tlo = new TableListIPO();
        tlo.venueID = venueID;
        tlo.access_token = UserHelper.getIns().getSavedAccessToken(getActivity());

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

    private static final int DEFAULT_PAGE = 2;
    private static final int NUM_OF_TAB = 3;
    public static final int TAB_VENUS = 0;
    public static final int TAB_TONIGHT = 1;
    public static final int TAB_UPCOMING = 2;
    private static final int[] PAGE_TITLES = new int[]{R.string.venues, R.string.tonight, R
            .string.home_event};

    @Override
    public void onErrorResponse(final VolleyError error) {
        commonRequestErr(error);
    }

    @Override
    public void onBackPressed() {
//        System.exit(1);
        ProgressDialogFrag.ProgressRequestDlg<ArrayList<City>> dlg = new
                ProgressDialogFrag.ProgressRequestDlg<ArrayList<City>>();
//                dlg.setMsg(getString(R.string.loading_cities));
        Response.Listener<ArrayList<City>> listener = new Response.Listener<ArrayList<City>>() {
            @Override
            public void onResponse(final ArrayList<City> response) {
                CityFrag cf = CityFrag.newInstance(HomeFrag.this, response);
                cf.show(getChildFragmentManager(), cf.getClass().getSimpleName());
            }
        };
        JacksonRequest.ArrayRequestWithDlg<City>
                cr = new JacksonRequest.ArrayRequestWithDlg<City>(City.class,
                ApiHelper.getIns().getLocationsUrl(), dlg, listener, this);
        cr.setShouldCache(true);
        cr.request(getApp().getRequestQueue(), getChildFragmentManager());
    }

    private class HomeAdapter extends FragmentPagerAdapter {

        public HomeAdapter(final FragmentManager fm) {
            super(fm);
        }

        SparseArray<WeakReference<EventListFrag>> mFrags = new
                SparseArray<WeakReference<EventListFrag>>();
        WeakReference<EventListFrag> mCurrentFrag;

        // SonH July 10, 2015
        // Edit for endless viewpager
        @Override
        public Fragment getItem(final int position) {
            int dataPos = position - 1;
            if (position == 0) {
                dataPos = NUM_OF_TAB - 1;
            } else if (position > NUM_OF_TAB) {
                dataPos = 0;
            }
            EventListFrag elf;
            switch (dataPos) {
                case TAB_VENUS: // venues
                    elf = new VenuesListFrag();
                    break;
                default:
                case TAB_TONIGHT: // tonight
                    elf = new TonightListFrag();
                    break;
                case TAB_UPCOMING: // upcoming
                    elf = new UpcomingListFrag();
                    break;
            }
            elf.setItemClickListener(HomeFrag.this);
            return elf;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            int dataPos = position - 1;
            if (position == 0) {
                dataPos = NUM_OF_TAB - 1;
            } else if (position > NUM_OF_TAB) {
                dataPos = 0;
            }
            EventListFrag elf = (EventListFrag) super.instantiateItem(container, position);
            mFrags.put(dataPos, new WeakReference<EventListFrag>(elf));
            return elf;
        }

        /*@Override
        public void destroyItem(final ViewGroup container, final int position,
                                final Object object) {
            int dataPos = position - 1;
            if (position == 0) {
                dataPos = NUM_OF_TAB - 1;
            } else if (position > NUM_OF_TAB) {
                dataPos = 0;
            }
            mFrags.delete(dataPos);
            super.destroyItem(container, dataPos, object);
        }*/

        @Override
        public int getCount() {
            return NUM_OF_TAB + 2;
        }

        @Override
        public CharSequence getPageTitle(final int position) {
            int dataPos = position - 1;
            if (position == 0) {
                dataPos = NUM_OF_TAB - 1;
            } else if (position > NUM_OF_TAB) {
                dataPos = 0;
            }
            return getString(PAGE_TITLES[dataPos]);
        }
        // SonH July 10, 2015 End.
    }
}

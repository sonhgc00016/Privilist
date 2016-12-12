package com.privilist.frag.profile.profiledetails;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.GlideCircleTransform;
import com.privilist.R;
import com.privilist.component.CommonFragPagerAdt;
import com.privilist.frag.base.BaseFrag;
import com.privilist.frag.base.BaseHeaderFrag;
import com.privilist.model.User;
import com.privilist.util.GlideHelper;
import com.privilist.util.Utils;
import com.viewpagerindicator.TitlePageIndicator;
import java.net.URL;
/**
 * Created by SonH on 6/28/15.
 */
public class ProfileDetailsFrag extends BaseHeaderFrag implements ErrorListener {

    private User mUser;

    public void setmUser(User mUser) {
        this.mUser = mUser;
    }

    private ProfileAdapter mProfileAdapter;

    public ProfileDetailsFrag() {
        // Required empty public constructor
    }

    @Override
    protected CharSequence getTitle() {
        return getString(R.string.profile);
    }

    @Override
    protected View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.setVisibility(view.findViewById(R.id.profile_imgArrow), View.GONE);
        final ViewPager vpProfile = (ViewPager) view.findViewById(R.id.profileDetails_pager_tab);
        mProfileAdapter = new ProfileAdapter(getChildFragmentManager());
        vpProfile.setAdapter(mProfileAdapter);
        TitlePageIndicator tpi = (TitlePageIndicator) view.findViewById(R.id.profileDetails_pager_tab_indicator);
        tpi.setViewPager(vpProfile);
        vpProfile.setCurrentItem(1);
        vpProfile.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int curr = vpProfile.getCurrentItem();
                    int lastReal = vpProfile.getAdapter().getCount() - 2;
                    if (curr == 0) {
                        vpProfile.setCurrentItem(lastReal, false);
                    } else if (curr > lastReal) {
                        vpProfile.setCurrentItem(1, false);
                    }
                }
            }
        });
        tpi.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                if (state == ViewPager.SCROLL_STATE_IDLE) {
//                    int curr = vpProfile.getCurrentItem();
//                    int lastReal = vpProfile.getAdapter().getCount() - 2;
//                    if (curr == 0) {
//                        vpProfile.setCurrentItem(lastReal, false);
//                    } else if (curr > lastReal) {
//                        vpProfile.setCurrentItem(1, false);
//                    }
//                }
            }
        });

        // get user info
        if (mUser != null) {
            ImageView imgAvatar = (ImageView) view.findViewById(R.id.profile_imgAvatar);
            if (mUser.image != null)
                try {
                    URL url = new URL(mUser.image.url);
                    Uri uri = Uri.parse(url.toURI().toString());
                    Glide.with(getActivity())
                            .load(uri)
                            .transform(new GlideCircleTransform(getActivity()))
                            .into(imgAvatar);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            // set border for image
            imgAvatar.setBackgroundResource(R.drawable.avatar_border);

            Utils.setText(view.findViewById(R.id.profile_tvName), mUser.full_name);
            Utils.setText(view.findViewById(R.id.profile_tvRank), mUser.rank.name + " member");
            Utils.setTextViewColor(view.findViewById(R.id.profile_tvRank), Color.parseColor(mUser.rank.color));
            ImageView imgRank = (ImageView) view.findViewById(R.id.profile_imgRank);
            if (!mUser.rank.icon.isEmpty() && !mUser.rank.icon.equals("")) {
                GlideHelper.loadImage(mUser.rank.icon, imgRank);
                imgRank.setVisibility(View.VISIBLE);
            } else {
                imgRank.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        commonRequestErr(error);
    }

    class PageItem {
    }

    public static final int TAB_POINT_HISTORY = 0;
    public static final int TAB_MEMBER_PERKS = 1;
    public static final int TAB_RANKING = 2;
    public static final int TAB_REWARD = 3;
    private static final int[] PAGE_TITLES = new int[]{R.string.point_history, R.string.member_perks,
            R.string.ranking, R.string.reward};

    private class ProfileAdapter extends CommonFragPagerAdt<PageItem> {

        {
            // TODO dummy item code
            for (int i = 0; i < 4; i++) {
                mItems.add(new PageItem());
            }
        }

        public ProfileAdapter(final FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(final int position) {
            int dataPos = position - 1;
            if (position == 0) {
                dataPos = mItems.size() - 1;
            } else if (position == mItems.size() + 1) {
                dataPos = 0;
            }
            BaseFrag frag;
            switch (dataPos) {
                default:
                case TAB_POINT_HISTORY: // point history
                    frag = new PointHistoryFrag();
                    break;
                case TAB_MEMBER_PERKS: // member perks
                    frag = new MemberPerksFrag();
                    break;
                case TAB_RANKING: // ranking
                    frag = new RankingListFrag();
                    break;
                case TAB_REWARD: // reward
                    frag = new MyRewardFrag();
                    break;
            }
            return frag;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int dataPos = position - 1;
            if (position == 0) {
                dataPos = mItems.size() - 1;
            } else if (position == mItems.size() + 1) {
                dataPos = 0;
            }
            return getString(PAGE_TITLES[dataPos]);
        }

        @Override
        public int getCount() {
            // Add 2 for endless viewpager
            return Utils.getSize(mItems) + 2;
        }
    }
}
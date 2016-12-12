package com.privilist.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.privilist.R;
import com.privilist.component.widget.DrinkChildView;
import com.privilist.model.Drink;
import com.privilist.model.DrinkCategory;
import com.privilist.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by minhtdh on 6/26/15.
 */
public class DrinkAdapter extends BaseExpandableListAdapter {

    protected final ArrayList<DrinkCategory> mItems = new ArrayList<DrinkCategory>();

    public void replace(List<? extends DrinkCategory> newItems) {
        mItems.clear();
        if (newItems != null) {
            mItems.addAll(newItems);
        }
    }

    @Override
    public int getGroupCount() {
        return mItems.size();
    }

    @Override
    public int getChildrenCount(final int groupPosition) {
        DrinkCategory cat = getGroup(groupPosition);
        return cat == null ? 0 : Utils.getSize(cat.drinks);
    }

    @Override
    public DrinkCategory getGroup(final int groupPosition) {
        return mItems.get(groupPosition);
    }

    @Override
    public Drink getChild(final int groupPosition, final int childPosition) {
        DrinkCategory cat = getGroup(groupPosition);
        return cat == null ? null : Utils.getItem(cat.drinks, childPosition);
    }

    @Override
    public long getGroupId(final int groupPosition) {
        DrinkCategory cat = getGroup(groupPosition);
        return cat == null ? 0 : cat.id;
    }

    @Override
    public long getChildId(final int groupPosition, final int childPosition) {
        Drink d = getChild(groupPosition, childPosition);
        return d == null ? 0 : d.id;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded,
                             final View convertView,
                             final ViewGroup parent) {
        CategoryGroup ret = (CategoryGroup) convertView;
        if (ret == null) {
            ret = new CategoryGroup(parent.getContext());
        }
        DrinkCategory item = getGroup(groupPosition);
        if (item != null) {
            ret.setExpand(isExpanded);
            ret.mTvName.setText(item.name);
        }
        return ret;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             final boolean isLastChild,
                             final View convertView, final ViewGroup parent) {
        DrinkChildView ret = (DrinkChildView) convertView;
        if (ret == null) {
            ret = new DrinkChildView(parent.getContext());
        }
        ret.setOnNumberChangedLister(mOnNumberChangedListener);
        ret.setDrink(getChild(groupPosition, childPosition));
        return ret;
    }

    @Override
    public boolean isChildSelectable(final int groupPosition, final int childPosition) {
        return false;
    }

    private DrinkChildView.OnNumberChanged mOnNumberChangedListener;

    public void setOnNumberChangedListener(
            final DrinkChildView.OnNumberChanged pOnNumberChangedListener) {
        mOnNumberChangedListener = pOnNumberChangedListener;
    }

    public static class CategoryGroup extends LinearLayout {
        public CategoryGroup(final Context context) {
            super(context);
            init();
        }

        public CategoryGroup(final Context context, final AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public CategoryGroup(final Context context, final AttributeSet attrs,
                             final int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public CategoryGroup(final Context context, final AttributeSet attrs,
                             final int defStyleAttr, final int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            init();
        }
        private TextView mTvName;
        private ImageView mImgExpand;
        private void init() {
            setOrientation(HORIZONTAL);
            setPadding(0, getResources().getDimensionPixelSize(R.dimen.common_space), 0, 0);
            LayoutInflater.from(getContext()).inflate(R.layout.drink_category_group, this, true);
            mTvName = (TextView) findViewById(R.id.tv_drink_category_name);
            mImgExpand = (ImageView) findViewById(R.id.img_expand);
        }

        public void setExpand(boolean expanded) {
            mImgExpand.setSelected(expanded);
        }
    }
}

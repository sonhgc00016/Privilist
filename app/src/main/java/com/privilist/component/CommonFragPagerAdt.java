package com.privilist.component;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.privilist.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by minhtdh on 6/24/15.
 */
public abstract class CommonFragPagerAdt<T> extends FragmentPagerAdapter {
    protected final ArrayList<T> mItems = new ArrayList<T>();
    public CommonFragPagerAdt(final FragmentManager fm) {
        super(fm);
    }

    public void replace(List<? extends T> newItems) {
        mItems.clear();
        if (newItems != null) {
            mItems.addAll(newItems);
        }
    }

    public T getObjectAtPosition(int pos) {
        return mItems.get(pos);
    }

    @Override
    public int getCount() {
        return Utils.getSize(mItems);
    }
}

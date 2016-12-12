package com.privilist.component;

import android.widget.BaseAdapter;

import com.privilist.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by minhtdh on 6/25/15.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected final ArrayList<T> mItems = new ArrayList<T>();

    public void replace(List<? extends T> newItems) {
        mItems.clear();
        if (newItems != null) {
            mItems.addAll(newItems);
        }
    }

    @Override
    public int getCount() {
        return Utils.getSize(mItems);
    }

    @Override
    public T getItem(final int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }
}

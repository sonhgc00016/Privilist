package com.privilist.frag.book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.privilist.R;
import com.privilist.component.DrinkAdapter;
import com.privilist.component.widget.DrinkChildView;
import com.privilist.define.Constant;
import com.privilist.frag.base.BaseHeaderFrag;
import com.privilist.model.Drink;
import com.privilist.model.DrinkCategory;
import com.privilist.model.LocalBookingIPO;
import com.privilist.model.Table;
import com.privilist.model.Venue;
import com.privilist.util.Log;
import com.privilist.util.UserHelper;
import com.privilist.util.Utils;

import org.joda.time.LocalDate;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by minhtdh on 6/26/15.
 */
public class SelectDrinkFrag extends BaseHeaderFrag
        implements View.OnClickListener, DrinkChildView.OnNumberChanged, ExpandableListView.OnGroupClickListener {

    private LocalBookingIPO mBookingInput;

    public static SelectDrinkFrag newInstance(Table table, Venue pVenue, LocalDate date, int
            eventID) {
        SelectDrinkFrag frag = new SelectDrinkFrag();
        LocalBookingIPO input = new LocalBookingIPO();
        Bundle args = new Bundle();
        args.putInt("event_id", eventID);
        args.putSerializable("table", table);
        args.putSerializable("date", date);

        input.eventID = eventID;
        input.date = date;
        input.table = table;
        input.venue = pVenue;
        frag.mBookingInput = input;
        frag.setArguments(args);
        return frag;
    }

    private DrinkAdapter mAdapter = new DrinkAdapter();

    public void setItems(ArrayList<DrinkCategory> cats) {
        mAdapter.replace(cats);
    }

    @Override
    protected CharSequence getTitle() {
        return getString(R.string.select_drink_title);
    }

    private WeakReference<TextView> mtvTotal;

    @Override
    protected View createContentView(final LayoutInflater inflater, final ViewGroup container,
                                     final Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.select_drink, container, false);
        ExpandableListView elv = (ExpandableListView) ret.findViewById(R.id.lv_drinks);
        mAdapter.setOnNumberChangedListener(this);
        elv.setAdapter(mAdapter);
        elv.setOnGroupClickListener(this);
        /*for (int i = 0; i< mAdapter.getGroupCount(); i++) {
            elv.expandGroup(i);
        }*/
        mtvTotal = new WeakReference<TextView>((TextView) ret.findViewById(R.id.tv_total));
        Utils.setOnclick(ret.findViewById(R.id.btnSelect), this);
        recalTotal();
        return ret;
    }
    private double mTotal = 0;
    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.btnSelect) {
            if (mBookingInput == null || mBookingInput.date == null || mBookingInput.table == null) {
                return;
            }
            // validate total
            recalTotal();
            double min = 0;
            try {
                min = Double.parseDouble(mBookingInput.venue.min_price);
            } catch (Exception e) {
                Log.d(TAG, Log.getStackTraceString(e));
            }
            if (mTotal < min) {
                showAlert(getString(R.string.select_drink_lower_minimum_warning));
                return;
            }
            mBookingInput.drinks = getBookedDrinkList();
            mBookingInput.total = mTotal;
            CheckOutFrag frag = CheckOutFrag.newInstance(mBookingInput);
            move(frag);
        }
    }
    private ArrayList<Drink> getBookedDrinkList() {
        ArrayList<Drink> ret = new ArrayList<Drink>();
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            int childCount = mAdapter.getChildrenCount(i);
            for (int j = 0; j < childCount; j++) {
                Drink d = mAdapter.getChild(i, j);
                if (d != null && d.number > 0) {
                    ret.add(d);
                }
            }
        }
        return ret;
    }

    private void recalTotal() {
        double total = 0;
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            int childCount = mAdapter.getChildrenCount(i);
            for (int j = 0; j < childCount; j++) {
                Drink d = mAdapter.getChild(i, j);
                if (d != null && d.number > 0) {
                    try {
                        total += d.number * Double.parseDouble(d.price);
                    } catch (Exception e) {
                        Log.d(TAG, Log.getStackTraceString(e));
                    }
                }
            }
        }
        mTotal = total;
        updateTotalText();
    }

    private void updateTotalText() {
        TextView tv = Utils.getVal(mtvTotal);
        if (tv != null) {
            tv.setText(getString(R.string.select_drink_total_format, UserHelper.getIns()
                    .getCurrency(), mTotal, mBookingInput == null || mBookingInput.venue == null ?
                    Constant.EMPTY : mBookingInput.venue.min_price));
        }
    }

    @Override
    public void onNumberChanged(final Drink drink, final int oldNumber, final int newNumber) {
        //
        double price = 0;
        try {
            // TODO note the locate
            price = Double.parseDouble(drink.price);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        mTotal += (newNumber - oldNumber) * price;
        updateTotalText();
    }

    @Override
    public boolean onGroupClick(final ExpandableListView parent, final View v,
                                final int groupPosition, final long id) {
        if (v instanceof DrinkAdapter.CategoryGroup) {
            ((DrinkAdapter.CategoryGroup) v).setExpand(parent.isGroupExpanded(groupPosition));
        }
        return false;
    }
}

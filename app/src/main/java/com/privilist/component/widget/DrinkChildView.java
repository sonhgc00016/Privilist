package com.privilist.component.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.privilist.R;
import com.privilist.define.Constant;
import com.privilist.model.Drink;
import com.privilist.util.UserHelper;

/**
 * Created by minhtdh on 6/26/15.
 */
public class DrinkChildView extends LinearLayout implements View.OnClickListener {

    public interface OnNumberChanged {
        void onNumberChanged(Drink drink, int oldNumber, int newNumber);
    }

    public DrinkChildView(final Context context) {
        super(context);
        init();
    }

    public DrinkChildView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrinkChildView(final Context context, final AttributeSet attrs,
                          final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DrinkChildView(final Context context, final AttributeSet attrs,
                          final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    protected Drink mDrink;
    private OnNumberChanged mOnNumberChangedLister;

    protected TextView mTvName, mTvPrice, mTvNumber;
    protected View mBtnLeft, mBtnRight;

    private void init() {
        setOrientation(HORIZONTAL);
        LayoutInflater.from(getContext()).inflate(R.layout.drink_child_item, this, true);
        mTvName = (TextView) findViewById(R.id.tv_drink_name);
        mTvPrice = (TextView) findViewById(R.id.tv_price);
        mTvNumber = (TextView) findViewById(R.id.tv_number);
        mBtnLeft = findViewById(R.id.img_decrease);
        mBtnLeft.setOnClickListener(this);
        mBtnRight = findViewById(R.id.img_increase);
        mBtnRight.setOnClickListener(this);
    }

    public void setOnNumberChangedLister(
            final OnNumberChanged pOnNumberChangedLister) {
        mOnNumberChangedLister = pOnNumberChangedLister;
    }

    public void setDrink(Drink drink) {
        mDrink = drink;
        mTvName.setText(mDrink == null ? null : mDrink.name);
        setPrice(drink);
        setNumText(mDrink == null ? 0 : mDrink.number);
    }

    protected void setPrice(Drink d) {
        mTvPrice.setText(getResources().getString(R.string.table_price_format, UserHelper.getIns
                ().getCurrency(), mDrink == null ? Constant.EMPTY : mDrink.price));
    }

    protected void setNumText(int number) {
        mTvNumber.setText(String.valueOf(number));
        if (number == 0) {
            mTvNumber.setSelected(false);
            mBtnLeft.setVisibility(View.INVISIBLE);
        } else {
            mTvNumber.setSelected(true);
            mBtnLeft.setVisibility(View.VISIBLE);
        }
    }

    private void changeNumber(boolean increased) {
        if (mDrink != null) {
            final int oldNum = mDrink.number;
            final int newNum = oldNum + (increased ? 1 : -1);
            if (newNum >= 0) {
                mDrink.number = newNum;
                setNumText(newNum);
                if (mOnNumberChangedLister != null) {
                    mOnNumberChangedLister.onNumberChanged(mDrink, oldNum, newNum);
                }
            }
        }
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.img_increase) {
            changeNumber(true);
        } else if (v.getId() == R.id.img_decrease) {
            changeNumber(false);
        }
    }

    public static class CheckOutDrinkView extends DrinkChildView {
        public CheckOutDrinkView(final Context context) {
            super(context);
            mBtnRight.setVisibility(View.GONE);
            mBtnLeft.setVisibility(View.GONE);
            mTvNumber.setVisibility(View.GONE);
        }

        @Override
        protected void setPrice(final Drink d) {
            mTvPrice.setText(getResources().getString(R.string.checkout_drink_format, UserHelper.getIns
                    ().getCurrency(), mDrink == null ? Constant.EMPTY : mDrink.price, mDrink ==
                    null ? 0 : mDrink.number));
        }

        @Override
        protected void setNumText(final int number) {
            // do nothing
        }
    }
}

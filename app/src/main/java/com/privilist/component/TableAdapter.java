package com.privilist.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.privilist.R;
import com.privilist.model.Table;

/**
 * Created by minhtdh on 6/25/15.
 */
public class TableAdapter extends CommonAdapter<Table> {
    public String currency;
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View ret = convertView;
        Holder holder;
        if (ret == null) {
            ret = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_item, parent,
                    false);
            holder = new Holder();
            holder.mTvTitle = (TextView) ret.findViewById(R.id.tv_table_title);
            holder.mTvSoldOut = (TextView) ret.findViewById(R.id.tv_sold_out);
            holder.mTvSubText = (TextView) ret.findViewById(R.id.tv_guests_num);
            holder.mTvPrice = (TextView) ret.findViewById(R.id.tv_price);
            ret.setTag(holder);
        } else {
            holder = (Holder) ret.getTag();
        }
        Table item = getItem(position);
        boolean soldout = false;
        if (item != null) {
            holder.mTvTitle.setText(item.table_title);
            soldout = item.table_left == 0;
            if (currency != null && item.price != null) {
                holder.mTvPrice.setText(ret.getResources().getString(R.string.table_price_format,
                        currency, item.price));
            }
            holder.mTvSubText.setText(ret.getResources()
                    .getString(R.string.guests_limit_format, String.valueOf(item.people)));
        }
        holder.mTvSoldOut.setVisibility(soldout ? View.VISIBLE : View.INVISIBLE);
        return ret;
    }

    static class Holder {
        TextView mTvTitle, mTvSoldOut, mTvSubText, mTvPrice;
    }
}

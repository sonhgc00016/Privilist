package com.privilist.component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.privilist.R;

import java.util.ArrayList;

/**
 * Created by SonH on 2015-08-05.
 */
public class RecyclerViewDateAdapter extends RecyclerView.Adapter<RecyclerViewDateAdapter.ViewHolder> {
    private ArrayList<String> mArrDates;
    private Context mContext;
    private int selectedPosition = -1;
    public IMyViewHolderClicks mListener;

    public RecyclerViewDateAdapter(ArrayList<String> mArrDates, Context mContext, IMyViewHolderClicks pListener) {
        this.mArrDates = mArrDates;
        this.mContext = mContext;
        this.mListener = pListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(mContext).
                inflate(R.layout.item_date, parent, false);
        // Return a new holder instance
        return new RecyclerViewDateAdapter.ViewHolder(itemView, mContext);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvName.setText(mArrDates.get(position));
        if (selectedPosition != -1 && selectedPosition == position) {
            holder.tvName.setTextColor(mContext.getResources().getColor(R.color.color_orange));
        } else {
            holder.tvName.setTextColor(Color.WHITE);
        }
    }

    public void setSelectedPosition(int _selectedPosition) {
        this.selectedPosition = _selectedPosition;
    }

    @Override
    public int getItemCount() {
        return mArrDates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvName;
        Context mContext;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            mContext = context;
            this.tvName = (TextView) itemView.findViewById(R.id.tvDate);
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            ViewGroup.LayoutParams params = this.tvName.getLayoutParams();
            params.width = width / 7;
            this.tvName.setLayoutParams(params);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onDateClick(v, getAdapterPosition());
        }
    }

    public interface IMyViewHolderClicks {
        void onDateClick(View view, int position);
    }
}

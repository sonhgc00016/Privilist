package com.privilist.component;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.privilist.R;
import com.privilist.frag.home.EventListFrag;
import com.privilist.model.Event;
import com.privilist.model.Image;
import com.privilist.model.Venue;
import com.privilist.util.GlideHelper;
import com.privilist.util.Utils;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by minhtdh on 6/17/15.
 */
public class EventListAdapter extends CommonAdapter<EventListAdapter.EventItem> {
    public interface EventItem {
        @JsonIgnore
        CharSequence getMainText(Resources res);
        @JsonIgnore
        CharSequence getSubText(Resources res);
        @JsonIgnore
        CharSequence getBoxText(Resources res);
        @JsonIgnore
        Image getDisplayImage();
        //luanlq July 10,2015
        int getSubTextColor(Resources res);
        //luanlq July 10,2015 End.
    }
    //luanlq July 10,2015
    public static int kindEvent;
    //luanlq July 10,2015 End.
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        // TODO implement UI for each event
        View ret = convertView;
        Holder holder;
        if (ret == null) {
            ret = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
            holder = new Holder();
            holder.mTvMain = (TextView) ret.findViewById(R.id.tv_main);
            holder.mTvSub = (TextView) ret.findViewById(R.id.tv_sub);
            holder.mTvBox = (TextView) ret.findViewById(R.id.tv_box);
            holder.mImg = (ImageView) ret.findViewById(R.id.img_background);
            ret.setTag(holder);
        } else {
            holder = (Holder) ret.getTag();
        }
        Resources res = parent.getResources();
        EventItem item = (EventItem) getItem(position);
        if (item != null) {
            holder.mTvMain.setText(item.getMainText(res));
            holder.mTvSub.setText(item.getSubText(res));
            //luanlq July 10,2015
            holder.mTvSub.setTextColor(item.getSubTextColor(res));
            //luanlq July 10,2015 End.
            CharSequence boxText = item.getBoxText(res);
            holder.mTvBox.setText(boxText);
            holder.mTvBox.setVisibility(Utils.notEmpty(boxText) ? View.VISIBLE : View.INVISIBLE);
            //luanlq July,31 2015
            if(kindEvent == 1 || EventListFrag.kindFrag == 1){
                holder.mTvBox.setVisibility(View.INVISIBLE);
            }
            //luanlq July,31 2015 End.
            if (item instanceof Event) {
                holder.mTvBox.setBackgroundResource(R.drawable.ads_box_border);
            } else if (item instanceof Venue) {
                //luanlq July,31 2015
                if(((Venue) item).featured) {
                    holder.mTvBox.setBackgroundResource(R.drawable.event_box_border);
                }else{
                    holder.mTvBox.setVisibility(View.INVISIBLE);
                }
                //luanlq July,31 2015 End.
            }
            // SonH July 31, 2015
            // Fix bug display Event image when image = null.
            if(item.getDisplayImage() != null) {
                GlideHelper.loadImage(item.getDisplayImage(), holder.mImg);
            }else {
                Image img = new Image();
                img.url = "https://upload.wikimedia.org/wikipedia/commons/6/68/Solid_black.png";
                GlideHelper.loadImage(img, holder.mImg);
            }
            // SonH July 31, 2015 End.
        }
        return ret;
    }

    static class Holder {
        TextView mTvMain;
        TextView mTvSub;
        TextView mTvBox;
        ImageView mImg;
    }
}

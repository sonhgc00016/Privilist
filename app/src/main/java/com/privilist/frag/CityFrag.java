package com.privilist.frag;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.privilist.R;
import com.privilist.component.CommonAdapter;
import com.privilist.frag.base.BaseDialog;
import com.privilist.frag.home.HomeFrag;
import com.privilist.model.City;
import com.privilist.util.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by minhtdh on 6/17/15.
 */
public class CityFrag extends BaseDialog implements AdapterView.OnItemClickListener {

    public static CityFrag newInstance(HomeFrag parent, ArrayList<City> cities) {
        CityFrag ret = new CityFrag();
        ret.setParent(parent);
        if (cities != null) {
            ret.mLocations.addAll(cities);
        }
        return ret;
    }

    private final ArrayList<City> mLocations = new ArrayList<City>();
    private WeakReference<HomeFrag> mParent;

    public void setParent(final HomeFrag pParent) {
        mParent = new WeakReference<HomeFrag>(pParent);
    }
    //luanlq July,27 2015
    private ListView city_resultListView;
    private ImageView select_city_backImageView;
    //End.
    //luanlq August 06,2015
    public static boolean isSelectedCity=false;
    //luanlq August 06,2015 End.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyCustomDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_select_city,container,false);
        city_resultListView=(ListView)v.findViewById(R.id.city_resultListView);
        select_city_backImageView=(ImageView)v.findViewById(R.id.select_city_backImageView);
        CityAdapter adapter = new CityAdapter();
        adapter.replace(mLocations);
        city_resultListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        city_resultListView.setDividerHeight(getResources().getDimensionPixelSize(R.dimen.location_item_space));
        city_resultListView.setOnItemClickListener(this);
        city_resultListView.setAdapter(adapter);
        select_city_backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //luanlq August 06,2015
        if(!isSelectedCity){
            disableSelectCityBackImageView();
        }
        //luanlq August 06,2015 End.

        getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.overlay));
        return v;
    }
    //luanlq July,27 2015 End.

    //    @NonNull
//    @Override
//    public Dialog onCreateDialog(final Bundle savedInstanceState) {
//        AlertDialog.Builder bld = new AlertDialog.Builder(getActivity(), STYLE_NO_TITLE);
//        CityAdapter adapter = new CityAdapter();
//        adapter.replace(mLocations);
//
//        ListView lv = new ListView(getActivity());
//        lv.setDivider(new ColorDrawable(Color.TRANSPARENT));
//        lv.setDividerHeight(getResources().getDimensionPixelSize(R.dimen.location_item_space));
//        lv.setOnItemClickListener(this);
//        lv.setAdapter(adapter);
//        bld.setView(lv);
////        bld.setAdapter(adapter, this);
//        bld.setCancelable(true);
//        AlertDialog ad = bld.create();
//        customizeDlg(ad);
//        return ad;
//    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position,
                            final long id) {
        dismiss();
        HomeFrag parentFrag = Utils.getVal(mParent);
        City city = mLocations.get(position);
        if (parentFrag != null) {
            parentFrag.setLocation(city);
            //luanlq August 06,2015
            isSelectedCity=true;
            //luanlq August 06,2015 End.
        }

    }
    //luanlq August 06,2015
    private void disableSelectCityBackImageView(){
        select_city_backImageView.setVisibility(View.INVISIBLE);
        select_city_backImageView.setEnabled(false);
        select_city_backImageView.setClickable(false);
    }
    //luanlq August 06,2015 End.

    @Override
    public void onResume() {
        super.onResume();
        //luanlq August 06,2015
        //custom backpress to quit app when no selected city
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    if(!isSelectedCity){
                        System.exit(1);
                        return true;
                    }else{
                        return false;
                    }
                }else
                    return false;
            }
        });
        //luanlq August 06,2015 End.
    }

    public static class CityAdapter extends CommonAdapter<City> {

        @Override
        public long getItemId(final int position) {
            return 0;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View ret = convertView;
            Holder holder;
            if (ret == null) {
                ret = LayoutInflater.from(parent.getContext()).inflate(R.layout.big_text, parent, false);
                holder = new Holder();
                holder.mTv = (TextView) ret.findViewById(android.R.id.title);
                ret.setTag(holder);
            } else {
                holder = (Holder) ret.getTag();
            }
            City item = getItem(position);
            if (item != null) {
                holder.mTv.setText(item.name);
            }
            return ret;
        }

        static class Holder {
            TextView mTv;
        }
    }
}

package com.privilist.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.paypal.android.sdk.p;
import com.privilist.R;
import com.privilist.frag.base.BaseHeaderFrag;
import com.privilist.model.Venue;
import com.privilist.util.Log;

/**
 * Created by minhtdh on 6/30/15.
 */
public class MapFrag extends BaseHeaderFrag implements OnMapReadyCallback {
    public Venue venue;

    @Override
    protected CharSequence getTitle() {
        return getString(R.string.map_title);
    }

    @Override
    protected View createContentView(final LayoutInflater inflater, final ViewGroup container,
                                     final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_frag, container, false);
        SupportMapFragment smf =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frag_map);
        smf.getMapAsync(this);
        return v;
    }


    @Override
    public void onMapReady(final GoogleMap pGoogleMap) {
        if (venue != null) {
            double lat = 0, lng = 0;
            try {
                lat = Double.parseDouble(venue.latitude);
                lng = Double.parseDouble(venue.longtitude);
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
            pGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), /*zoom level*/ 15.0f));
            pGoogleMap.addMarker(new MarkerOptions()
                    .title(venue.name)
                    .position(new LatLng(lat, lng)))
                    .showInfoWindow();
        }
    }
}

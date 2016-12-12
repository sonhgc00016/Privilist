package com.privilist.act;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.view.WindowManager;

import com.privilist.R;
import com.privilist.act.base.BaseAct;
import com.privilist.frag.IntroductionFrag;
import com.privilist.frag.base.BaseFrag;
import com.privilist.frag.home.HomeFrag;
import com.privilist.util.UserHelper;

import java.util.Locale;

public class IntroductionAct extends BaseAct {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        // SonH July 14, 2015
        // StrictMode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // SonH July 14, 2015 end.

        // SonH July 30, 2015
        // Set default language for activity
        Locale.setDefault(Locale.US);
        Configuration config = getResources().getConfiguration();
        config.locale = Locale.US;
        getApplicationContext().getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        // SonH July 30, 2015 End.

        // SonH June 26, 2015
        // check access_token existing
        String access_token = UserHelper.getIns().getSavedAccessToken(getApplicationContext());
        if (access_token != null) {// if access_token exist
            // go to Home
            HomeFrag iFrag = new HomeFrag();
            BaseFrag.Option opt = new BaseFrag.Option();
            opt.tag = iFrag.getClass().getSimpleName();
            opt.backStackName = iFrag.getClass().getName();
            opt.isAddBackStack = true;
            opt.setPlaceHolder(R.id.content_container);
            iFrag.selfMove(getSupportFragmentManager(), opt);
        } else {// access_token not exist
            // go to Introduction
            IntroductionFrag iFrag = new IntroductionFrag();
            BaseFrag.Option opt = new BaseFrag.Option();
            opt.isReplace = false;
            opt.isAddBackStack = false;
            opt.useAnimation = false;
            opt.setPlaceHolder(R.id.content_container);
            opt.tag = "Introduction";
            iFrag.selfMove(getSupportFragmentManager(), opt);
        }
        // SonH June 26, 2015 End.

    }

    // SonH June 26, 2015
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }
    // SonH June 26, 2015 End.
}

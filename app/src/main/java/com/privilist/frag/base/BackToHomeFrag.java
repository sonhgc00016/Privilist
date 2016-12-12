package com.privilist.frag.base;

import android.os.Bundle;
import android.view.View;

import com.privilist.R;
import com.privilist.act.base.BaseAct;
import com.privilist.util.Utils;

/**
 * Created by minhtdh on 6/26/15.
 */
public class BackToHomeFrag extends BaseFrag implements BaseAct.OnBackPress, View.OnClickListener {

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.setOnclick(view.findViewById(R.id.btnReturnToHome), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof BaseAct) {
            ((BaseAct) getActivity()).registeronBackPress(this);
        }
    }

    @Override
    public void onPause() {
        if (getActivity() instanceof BaseAct) {
            ((BaseAct) getActivity()).unregisteronBackPress(this);
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        backToHome(getFragmentManager());
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.btnReturnToHome) {
            //luanlq July,13 2015
            Utils.isBookSuccess=true;
            //luanlq July,13 2015 End.
            backToHome(getFragmentManager());
        }
    }
}

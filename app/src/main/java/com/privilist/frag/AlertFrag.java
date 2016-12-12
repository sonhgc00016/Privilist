package com.privilist.frag;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.privilist.frag.base.BaseDialog;

/**
 * Created by minhtdh on 6/29/15.
 */
public class AlertFrag extends BaseDialog {
    public CharSequence msg;
    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder bld = new AlertDialog.Builder(getActivity(), STYLE_NO_TITLE);
        bld.setMessage(msg);
        Dialog d = bld.create();
        d.setCanceledOnTouchOutside(true);
        customizeDlg(d);
        return d;
    }
}

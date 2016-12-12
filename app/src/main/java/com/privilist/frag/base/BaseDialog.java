package com.privilist.frag.base;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by minhtdh on 6/17/15.
 */
public class BaseDialog extends DialogFragment {


    protected void customizeDlg(Dialog dlg) {
        Window wd = dlg.getWindow();
        // request overlaybg window without the title
        wd.requestFeature(Window.FEATURE_NO_TITLE);
        // make dialog itself transparent
        wd.setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams params = wd.getAttributes();
        if (params != null) {
            params.gravity = Gravity.CENTER;
            wd.setAttributes(params);
        }
        wd.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}

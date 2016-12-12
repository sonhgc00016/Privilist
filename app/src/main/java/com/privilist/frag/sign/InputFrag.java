package com.privilist.frag.sign;


import android.util.Patterns;

import com.privilist.R;
import com.privilist.frag.base.BaseHeaderFrag;
import com.privilist.util.Utils;

import java.util.regex.Pattern;

/**
 * Created by minhtdh on 6/15/15.
 */
public class InputFrag extends BaseHeaderFrag {

    protected boolean validEmail(CharSequence email) {
        boolean ret = !Utils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (!ret) {
            showAlert(getString(R.string.mail_invalid));
        }
        return  ret;
    }
    static Pattern p = Pattern.compile("(?=.*[0-9])(?=.*[a-zA-Z])\\S{8,}");
    protected boolean validPass(CharSequence pass) {
        boolean ret = true;
        if (!p.matcher(pass).matches()) {
            ret = false;
        }
        if (!ret) {
            alert(getString(R.string.pass_invalid));
        }
        return  ret;
    }
}

/**
 *
 */
package com.privilist.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Util;
import com.privilist.R;
import com.privilist.define.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;

/**
 * @author minhtdh
 */
public class Utils {
    //luanlq July,13 2015
    public static boolean isBookSuccess=false;
    //luanlq July,13 2015 End.
    public static void hideSoftKeyboard(Activity activity) {
        if (activity == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if (imm != null && v != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static void setText(View tv, CharSequence cs) {
        if (tv instanceof TextView) {
            ((TextView) tv).setText(cs);
        }
    }

    public static void setTextSize(View tv, float size) {
        if (tv instanceof TextView) {
            ((TextView) tv).setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        }
    }

    public static void setTextViewColor(View tv, int color) {
        if (tv instanceof TextView) {
            ((TextView) tv).setTextColor(color);
        }
    }

    public static void setTextColor(View tv, int colorId) {
        if (tv instanceof TextView) {
            ((TextView) tv).setTextColor(tv.getResources().getColorStateList(colorId));
        }
    }

    public static void setVisibility(View v, int visiblility) {
        if (v != null) {
            v.setVisibility(visiblility);
        }
    }

    public static void setBackGround(View v, int backGroundId) {
        if (v != null) {
            v.setBackgroundResource(backGroundId);
        }
    }

    public static void setOnclick(View v, OnClickListener listener) {
        if (v != null) {
            v.setOnClickListener(listener);
        }
    }

    public static void setEnable(View v, boolean enabled) {
        if (v != null) {
            v.setEnabled(enabled);
        }
    }

    public static boolean compare(String s1, String s2) {
        return s1 != null && s1.equals(s2);
    }

    public static int getSize(Collection list) {
        return list != null ? list.size() : 0;
    }

    public static <E> E getItem(List<E> list, int pos) {
        return list == null ? null : list.get(pos);
    }

    public static <T> T getFirstItem(List<T> list) {
        return list == null || list.size() == 0 ? null : list.get(0);
    }

    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    public static boolean notEmpty(CharSequence s) {
        return s != null && s.length() > 0;
    }

    public static <T> T getVal(WeakReference<T> ref) {
        return ref == null ? null : ref.get();
    }

    public static void scale(View v, float scale) {
        if (v != null) {
            if (v instanceof TextView) {
                ((TextView) v).setTextScaleX(scale);
            } else {
                v.setScaleX(scale);
                v.setScaleY(scale);
            }
        }
    }

    public static void scale(View v, float scale, int gravity) {
        if (v != null) {
            int pX, pY;
            switch (gravity) {
                default:
                case Gravity.CENTER:
                    pX = (v.getMeasuredWidth()) / 2;
                    pY = (v.getMeasuredHeight()) / 2;
                    break;
                case Gravity.CENTER | Gravity.BOTTOM:
                    pX = (v.getMeasuredWidth()) / 2;
                    pY = v.getMeasuredHeight();
                    break;
                case Gravity.CENTER | Gravity.TOP:
                    pX = (v.getMeasuredWidth()) / 2;
                    pY = 0;
                    break;
            }
            v.setPivotX(pX);
            v.setPivotY(pY);
            v.setScaleX(scale);
            v.setScaleY(scale);
        }
    }

    /**
     * copy the input stream into overlaybg file, close input stream when done
     *
     * @param is
     * @param file
     * @return
     */
    public static boolean writeToFile(InputStream is, File file) {
        OutputStream out = null;
        boolean ret = false;
        if (is == null) {
            return ret;
        }
        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            ret = true;
        } catch (IOException e) {
            Log.e(Utils.class.getSimpleName(), Log.getStackTraceString(e));
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e(Utils.class.getSimpleName(), Log.getStackTraceString(e));
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Log.e(Utils.class.getSimpleName(), Log.getStackTraceString(e));
                }
            }
        }
        return ret;
    }

    public static boolean writeToFile(String data, File file) {
        OutputStreamWriter outputStreamWriter = null;
        boolean ret = false;
        try {
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
            outputStreamWriter.write(data);
            outputStreamWriter.flush();
            ret = true;
        } catch (IOException e) {
            Log.e(Utils.class.getSimpleName(), Log.getStackTraceString(e));
        } finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    Log.e(Utils.class.getSimpleName(), Log.getStackTraceString(e));
                }
            }
        }
        return ret;
    }


    public static final String SYSTEM_NEWLINE = System.getProperty("line.separator");

    public static StringBuilder readFromFile(File file) {
        StringBuilder ret = null;
        try {
            InputStream inputStream = new FileInputStream(file);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                ret = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    ret.append(receiveString);
                    ret.append(SYSTEM_NEWLINE);
                }
                if (ret.length() > 0) {
                    ret.delete(ret.length() - SYSTEM_NEWLINE.length(), ret.length());
                }
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return ret;
    }

    public static int getRelativeTopWithRoot(View v) {
        int ret = 0;
        if (v != null) {

            while (v.getParent() instanceof View && v.getParent() != v.getRootView()) {
                ret += v.getTop();
                v = (View) v.getParent();
            }
        }
        return ret;
    }

    public static String genGlideEmptySafeKey(String url) {
        String ret = null;
        if (!isEmpty(url)) {
            MessageDigest messageDigest = null;
            try {
                messageDigest = MessageDigest.getInstance("SHA-256");
                messageDigest.update(url.getBytes(Key.STRING_CHARSET_NAME));
            } catch (NoSuchAlgorithmException e) {
                Log.e("genGlideEmptyKey", Log.getStackTraceString(e));
            } catch (UnsupportedEncodingException e) {
                Log.e("genGlideEmptyKey", Log.getStackTraceString(e));
            }
            if (messageDigest != null) {
                ret = Util.sha256BytesToHex(messageDigest.digest());
            }
        }
        return ret;
    }

    public static boolean isMale(String gender) {
        return Constant.MALE.equals(gender);
    }

    public static String getGender(boolean isMale) {
        return isMale ? Constant.MALE : Constant.FEMALE;
    }

    public static boolean isOnline(Context pContext) {
        ConnectivityManager cm =
                (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

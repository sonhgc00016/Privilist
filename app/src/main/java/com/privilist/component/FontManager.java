package com.privilist.component;

import android.content.res.Resources;
import android.graphics.Typeface;

import com.privilist.util.Log;
import com.privilist.util.Utils;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by minhtdh on 6/30/15.
 */
public class FontManager {

    public static enum TextStyle {
        Normal,
        Bold,
        Italic;
    }

    static class Holder {
        private static volatile FontManager mInstance = new FontManager();
    }

    public static FontManager getIns() {
        return Holder.mInstance;
    }

    private FontManager() {

    }

    private final WeakHashMap<Typeface, String> mFontsMap = new WeakHashMap<Typeface, String>();

    public Typeface getFont(Resources res, String assetFontPath) {
        Typeface ret = null;
        for (Map.Entry<Typeface, String> entry : mFontsMap.entrySet()) {
            Typeface font = entry.getKey();
            if (font != null && Utils.compare(entry.getValue(), assetFontPath)) {
                ret = font;
                return ret;
            }
        }
        if (Utils.notEmpty(assetFontPath) && res != null) {
            try {
                ret = Typeface.createFromAsset(res.getAssets(), assetFontPath);
                mFontsMap.put(ret, assetFontPath);
            } catch (Exception e) {
                Log.e(FontManager.class.getSimpleName(), Log.getStackTraceString(e));
            }
        }
        return ret;
    }
}

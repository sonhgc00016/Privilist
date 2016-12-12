package com.privilist.util;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.Target;
import com.privilist.model.Image;

/**
 * Created by minhtdh on 6/23/15.
 */
public class GlideHelper {


    public static Target<GlideDrawable> loadImage(Image img, ImageView iv) {
        Target<GlideDrawable> ret = null;
        if (img != null && Utils.notEmpty(img.url) && iv != null) {
            ret = Glide.with(iv.getContext()).load(Uri.parse(img.url))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .into(iv);
        }
        return ret;
    }

    public static Target<GlideDrawable> loadImage(String img, ImageView iv) {
        Target<GlideDrawable> ret = null;
        if (Utils.notEmpty(img) && iv != null) {
            ret = Glide.with(iv.getContext()).load(Uri.parse(img))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .into(iv);
        }
        return ret;
    }
}

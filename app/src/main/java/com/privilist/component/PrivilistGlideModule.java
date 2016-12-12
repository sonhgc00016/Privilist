package com.privilist.component;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.volley.VolleyGlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.privilist.define.Constant;

import java.util.concurrent.Executors;

/**
 * Created by minhtdh on 6/10/15.
 */
public class PrivilistGlideModule extends VolleyGlideModule {
    public static final int NUM_OF_THREAD = 10;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        builder
                .setDecodeFormat(DecodeFormat.PREFER_ARGB_8888)
                .setMemoryCache(
                        new LruResourceCache(Constant.MEM_CACHE_SIZE))
                .setBitmapPool(new LruBitmapPool(Constant.POOL_CACHE_SIZE))
                .setDiskCacheService(Executors.newFixedThreadPool(NUM_OF_THREAD))
                .setResizeService(Executors.newFixedThreadPool(NUM_OF_THREAD))
                .setDiskCache(
                        new InternalCacheDiskCacheFactory(context, Constant.DISK_CACHE_SIZE));
        setDEFAULT_TIME_OUT(Constant.REQUEST_TIME_OUT);
    }
}

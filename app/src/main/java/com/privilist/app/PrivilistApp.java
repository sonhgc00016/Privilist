package com.privilist.app;

import android.support.multidex.MultiDexApplication;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.privilist.R;
import com.privilist.define.Constant;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by minhtdh on 6/10/15.
 */
@ReportsCrashes(
        formKey = "", // This is required for backward compatibility but not used
        mailTo = "sonhgc00016@gmail.com",//
        mode = ReportingInteractionMode.DIALOG,
        resDialogText = R.string.dialog_error_report_text
)
public class PrivilistApp extends MultiDexApplication {

    private RequestQueue mRequestQueue;

    public RequestQueue getRequestQueue() {
        if (mRequestQueue != null){
            mRequestQueue.getCache().clear();
        }
        return mRequestQueue;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
        // setting time out for Volley library
        Request.setDefaultTimeout(Constant.REQUEST_TIME_OUT, Constant.REQUEST_TIME_OUT);
//        mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue = Volley.newRequestQueue(this, new HurlStack() {
            @Override
            protected HttpURLConnection createConnection(URL url) throws IOException {
                HttpURLConnection pHttpURLConnection = super.createConnection(url);
//                pHttpURLConnection.setUseCaches(false);
                System.setProperty("http.keepAlive", "false");
                pHttpURLConnection.setChunkedStreamingMode(0);
                return pHttpURLConnection;
            }

        });
    }
}

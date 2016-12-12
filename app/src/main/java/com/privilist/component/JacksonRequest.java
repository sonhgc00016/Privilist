package com.privilist.component;

import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.privilist.define.Constant;
import com.privilist.frag.ProgressDialogFrag;
import com.privilist.util.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by minhtdh on 6/15/15.
 */
public abstract class JacksonRequest<T> extends JsonRequest<T> {

    private Class<T> mResponseType;
    /**
     * list of success code
     */
    private final HashSet<Integer> mSuccesCodes = new HashSet<Integer>();
    {
        mSuccesCodes.add(200);
        mSuccesCodes.add(201);
    }

    /**
     * set list of success code
     * @param list
     */
    public void setSuccessCode(final List<Integer> list) {
        mSuccesCodes.clear();
        if (list != null) {
            mSuccesCodes.addAll(list);
        }
    }

    /**
     * @param code success status code.(code 200, 201 was include by default)
     */
    public void addSuccessCode(final int code) {
        mSuccesCodes.add(code);
    }

    public JacksonRequest(final Class<T> pResponseType, final int method, final String url, final String requestBody, final Response.Listener<T> listener, final Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
//        this.setRetryPolicy(new DefaultRetryPolicy(Constant.REQUEST_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES
//                , DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mResponseType = pResponseType;
    }

    public JacksonRequest(final Class<T> pResponseType, final String url, final Response.Listener<T> listener, final Response.ErrorListener errorListener) {
        this(pResponseType, Method.GET, url, null, listener, errorListener);
    }

    private String mContentType;

    /**
     * pass @null if for use default application/json
     * @param pContentType content type parameter
     */
    public void setContentType(final String pContentType) {
        mContentType = pContentType;
    }

    @Override
    public String getBodyContentType() {
        return mContentType != null ? mContentType : super.getBodyContentType();
    }

    @Override
    public void cancel() {
        super.cancel();
        Log.d("JacksonRequest", "cancel");
    }

    @Override
    protected void deliverResponse(T response) {
        super.deliverResponse(response);
        Log.d("JacksonRequest", "deliverResponse");
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
        Log.d("JacksonRequest", "deliverError");
    }

    @Override
    protected Response<T> parseNetworkResponse(final NetworkResponse response) {
        boolean isSuccess = false;
        for (Integer code : mSuccesCodes) {
            if (code != null && code == response.statusCode) {
                isSuccess = true;
                break;
            }
        }
        if (isSuccess) {
            try {
                return Response.success(readResponse(new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers))),HttpHeaderParser
                        .parseCacheHeaders
                                (response));
            } catch (UnsupportedEncodingException e) {
                Response.error(new VolleyError(e));
            }
        }
        return Response.error(new VolleyError(response));
    }

    protected T readResponse(String res) {
        return JsonUtil.fromString(res, mResponseType);
    }

    /**
     * request with a waiting progress dialog
     * @param <K> type of return class
     */
    public static class RequestWithDlg<K> extends JacksonRequest<K> {
        public RequestWithDlg(final Class<K> pResponseType, final int method, final String url,
                              final String requestBody,
                              final ProgressDialogFrag.ProgressRequestDlg<K> dlg,
                              final Response.Listener<K> listener,
                              final Response.ErrorListener errorListener) {
            super(pResponseType, method, url, requestBody, dlg, dlg);
            mDialog = dlg;
            mDialog.setRequest(this);
            mDialog.setResListener(listener);
            mDialog.setResErrListenner(errorListener);
        }

        public RequestWithDlg(final Class<K> pResponseType, final String url,
                              final ProgressDialogFrag.ProgressRequestDlg<K> dlg,
                              final Response.Listener<K> listener,
                              final Response.ErrorListener errorListener) {
            this(pResponseType, Method.GET, url, null, dlg, listener, errorListener);
        }


        private final ProgressDialogFrag.ProgressRequestDlg mDialog;
        protected ProgressDialogFrag createDialog() {
            ProgressDialogFrag ret = new ProgressDialogFrag();
            return ret;
        }

        public void showDlg(FragmentManager fm) {
            if (mDialog != null)
                mDialog.show(fm, mDialog.getClass().getSimpleName());
        }

        public void request(RequestQueue queue, FragmentManager fm) {
            Log.d("RequestWithDlg","Url is "+getUrl());
            queue.add(this);
            queue.getSequenceNumber();
            showDlg(fm);
        }

        @Override
        public void cancel() {
            super.cancel();

            dismiss();
        }

        public void dismiss(){
            if (mDialog != null && mDialog.isShowing) {
                mDialog.dismiss();
            }
        }
    }

    /**
     * request dialog with response is a array.
     * @param <I> type of item in returned array list
     */
    public static class ArrayRequestWithDlg<I> extends
            RequestWithDlg<ArrayList<I>> {

        private Class<I> mItemType;

        public ArrayRequestWithDlg(final Class<I> pItemType, final int method,
                                   final String url, final String requestBody,
                                   final ProgressDialogFrag.ProgressRequestDlg<ArrayList<I>> dlg,
                                   final Response.Listener<ArrayList<I>> listener,
                                   final Response.ErrorListener errorListener) {
            super(null, method, url, requestBody, dlg, listener, errorListener);
            mItemType = pItemType;
        }

        public ArrayRequestWithDlg(final Class<I> pItemType, final String url,
                                   final ProgressDialogFrag.ProgressRequestDlg<ArrayList<I>> dlg,
                                   final Response.Listener<ArrayList<I>> listener,
                                   final Response.ErrorListener errorListener) {
            this(pItemType, Method.GET, url, null, dlg, listener, errorListener);
        }

        @Override
        protected ArrayList<I> readResponse(final String res) {
            return JsonUtil.fromStringToList(res, mItemType);
        }
    }
}

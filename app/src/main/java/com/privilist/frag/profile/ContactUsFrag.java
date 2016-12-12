package com.privilist.frag.profile;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.privilist.R;
import com.privilist.component.JacksonRequest;
import com.privilist.frag.ProgressDialogFrag;
import com.privilist.frag.base.BaseHeaderFrag;
import com.privilist.model.Contact;
import com.privilist.util.ApiHelper;
import com.privilist.util.Utils;

import java.lang.ref.WeakReference;

/**
 * Created by SonH on 2015-08-14.
 */
public class ContactUsFrag extends BaseHeaderFrag implements Response.ErrorListener {

    WeakReference<TextView> mTvServiceNumber, mTvServiceMail, mTvFb, mTvTw;

    public ContactUsFrag() {
    }

    @Override
    protected CharSequence getTitle() {
        return getString(R.string.contact_us);
    }

    @Override
    protected View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_us, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tv;
        tv = (TextView) view.findViewById(R.id.contact_us_tv_service_number);
        mTvServiceNumber = new WeakReference<TextView>(tv);
        tv = (TextView) view.findViewById(R.id.contact_us_tv_service_email);
        mTvServiceMail = new WeakReference<TextView>(tv);
        tv = (TextView) view.findViewById(R.id.contact_us_tv_fb);
        mTvFb = new WeakReference<TextView>(tv);
        tv = (TextView) view.findViewById(R.id.contact_us_tv_tw);
        mTvTw = new WeakReference<TextView>(tv);
        doGetContact();
    }

    private void doGetContact() {
        ProgressDialogFrag.ProgressRequestDlg<Contact> dlg = new
                ProgressDialogFrag.ProgressRequestDlg<Contact>();

        Response.Listener<Contact> listener = new Response.Listener<Contact>() {
            @Override
            public void onResponse(final Contact response) {
                TextView tv;
                tv = Utils.getVal(mTvServiceNumber);
                tv.setText(Html.fromHtml(getString(R.string.service_number, response.service_number, response.text_call_during)));
                tv = Utils.getVal(mTvServiceMail);
                tv.setText(Html.fromHtml(getString(R.string.service_email, response.service_email)));
                tv = Utils.getVal(mTvFb);
                tv.setText(response.fb);
                tv = Utils.getVal(mTvTw);
                tv.setText(response.tw);
            }
        };
        JacksonRequest.RequestWithDlg<Contact>
                cr = new JacksonRequest.RequestWithDlg<Contact>(Contact.class,
                ApiHelper.getIns().getContactUsUrl(), dlg, listener, this);
        cr.setShouldCache(true);
        cr.request(getApp().getRequestQueue(), getChildFragmentManager());
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        commonRequestErr(error);
    }
}

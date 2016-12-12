package com.privilist.frag.book;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.braintreepayments.api.dropin.BraintreePaymentActivity;
import com.privilist.R;
import com.privilist.component.JacksonRequest;
import com.privilist.component.widget.DrinkChildView;
import com.privilist.define.Constant;
import com.privilist.frag.ProgressDialogFrag;
import com.privilist.frag.base.BaseHeaderFrag;
import com.privilist.model.BookDetail;
import com.privilist.model.BookIPO;
import com.privilist.model.BookOPO;
import com.privilist.model.BrainTreeToken;
import com.privilist.model.Drink;
import com.privilist.model.Image;
import com.privilist.model.LocalBookingIPO;
import com.privilist.util.ApiHelper;
import com.privilist.util.UserHelper;
import com.privilist.util.Utils;

import java.util.ArrayList;


/**
 * Created by minhtdh on 6/29/15.
 */
public class CheckOutFrag extends BaseHeaderFrag implements View.OnClickListener, Response.ErrorListener {
    public static final String DATE_DISPLAY_FORMAT = "EEEE MM/dd/yyyy";

    private LocalBookingIPO mBookingInput;

    public static CheckOutFrag newInstance(LocalBookingIPO input) {
        CheckOutFrag frag = new CheckOutFrag();
        frag.mBookingInput = input;
        return frag;
    }

    @Override
    protected CharSequence getTitle() {
        return getString(R.string.check_out_title);
    }

    @Override
    protected View createContentView(final LayoutInflater inflater, final ViewGroup container,
                                     final Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.check_out_frag, container, false);
        addBottles((ViewGroup) ret.findViewById(R.id.vg_bottles));
        if (mBookingInput != null) {
            Utils.setText(ret.findViewById(R.id.tv_venue_name), mBookingInput.venue.name);
            Utils.setText(ret.findViewById(R.id.tv_date),
                    mBookingInput.date.toString(DATE_DISPLAY_FORMAT));
            Utils.setText(ret.findViewById(R.id.tv_table_name), mBookingInput.table.table_title);
            Utils.setText(ret.findViewById(R.id.tv_guest_limit),
                    getString(R.string.guests_limit_format, mBookingInput.table.people));
            // TODO highlight color for total text
            Utils.setText(ret.findViewById(R.id.tv_total), Html.fromHtml(getString(R.string
                    .check_out_total_format, UserHelper
                    .getIns().getCurrency(), String.valueOf(mBookingInput.total))));
        }
        Utils.setOnclick(ret.findViewById(R.id.btnCompleteBook), this);
        return ret;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BRAINTREE) {
            if (resultCode == BraintreePaymentActivity.RESULT_OK) {
                paymentMethodNonce = data.getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);
//                doRequest(paymentMethodNonce);
            }
        }
    }

    private String paymentMethodNonce;

    @Override
    public void onResume() {
        super.onResume();
        if (isAdded() && paymentMethodNonce != null) {
            String tmp = paymentMethodNonce;
            paymentMethodNonce = null;
            doRequest(tmp);
        }
    }

    @Override
    public void onClick(final View v) {
        if (R.id.btnCompleteBook == v.getId()) {
            if (mBookingInput == null || mBookingInput.date == null || mBookingInput.table == null) {
                return;
            }

            // TODO check card valid
            CreditCardFrag frag =
                    (CreditCardFrag) getChildFragmentManager().findFragmentById(R.id.frag_credit);
            if (frag == null || !frag.isValid()) {
                return;
            }
            // do request
//            if (getApp() == null || !UserHelper.getIns().isLogin()) {
//                showAlert(getString(R.string.login_timeout));
//                return;
//            }

            String access_token = UserHelper.getIns().getSavedAccessToken(getActivity());
            if (access_token != null) {
                ProgressDialogFrag.ProgressRequestDlg<BrainTreeToken> dlg = new ProgressDialogFrag
                        .ProgressRequestDlg<BrainTreeToken>();
                JacksonRequest.RequestWithDlg<BrainTreeToken> request = new JacksonRequest
                        .RequestWithDlg<BrainTreeToken>(BrainTreeToken.class, ApiHelper.getIns()
                        .getBrainTreeTokenUrl(access_token), dlg, new Response.Listener<BrainTreeToken>() {
                    @Override
                    public void onResponse(final BrainTreeToken response) {
                        Intent intent = new Intent(getActivity(), BraintreePaymentActivity.class);
                        intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, response.client_token);
                        startActivityForResult(intent, REQUEST_CODE_BRAINTREE);
                    }
                }, this);
                request.request(getApp().getRequestQueue(), getChildFragmentManager());
            }
        }
    }

    public static final int REQUEST_CODE_BRAINTREE = 8888;

    private void doRequest(String paymentMethodNonce) {
        if (mBookingInput == null || mBookingInput.date == null || mBookingInput.table == null) {
            return;
        }
        // do request
//        if (getApp() == null || !UserHelper.getIns().isLogin()) {
//            showAlert(getString(R.string.login_timeout));
//            return;
//        }

        BookIPO input = new BookIPO();
        input.access_token = UserHelper.getIns().getSavedAccessToken(getActivity());
        input.payment_method_nonce = paymentMethodNonce;
        input.currency = UserHelper.getIns().getCurrency();
        input.date = mBookingInput.date;
        input.table_id = mBookingInput.table.id;
        input.total = String.valueOf(mBookingInput.total);
        input.event_id = (int) mBookingInput.eventID;
        input.details = getBookDetails();

        ProgressDialogFrag.ProgressRequestDlg<BookOPO> dlg = new ProgressDialogFrag
                .ProgressRequestDlg<BookOPO>();
        JacksonRequest.RequestWithDlg<BookOPO> request = new JacksonRequest
                .RequestWithDlg<BookOPO>(BookOPO.class, Request.Method.POST, ApiHelper.getIns
                ().getBookUrl(input), ApiHelper.getIns().buildBookRequest(input), dlg,
                new Response.Listener<BookOPO>() {
                    @Override
                    public void onResponse(final BookOPO response) {
                        // TODO get real data
                        String name = mBookingInput.venue.name;
                        String code = response.code;
                        Image img = Utils.getFirstItem(mBookingInput.venue.images);
                        String imagePath = img == null ? Constant.EMPTY : img.url;
                        BookSuccessFrag frag = BookSuccessFrag.newInstance
                                (mBookingInput.date.toString
                                                (BookSuccessFrag.BOOK_SUCCESS_DATE_FORMAT), name, code,
                                        imagePath);
                        move(frag);
                        return;
                    }
                }, this);
        request.addSuccessCode(ApiHelper.BOOK_SUCCESS_CODE);
        request.setContentType(ApiHelper.CONTENT_TYPE_FORM);
        request.request(getApp().getRequestQueue(), getChildFragmentManager());
    }

    @Override
    public void onErrorResponse(final VolleyError error) {
        commonRequestErr(error);
    }

    private void addBottles(ViewGroup vg) {
        if (vg == null) {
            return;
        }
        vg.removeAllViews();
        if (mBookingInput != null && mBookingInput.drinks != null) {
            for (Drink d : mBookingInput.drinks) {
                DrinkChildView.CheckOutDrinkView child = new DrinkChildView.CheckOutDrinkView
                        (getActivity());
                child.setDrink(d);
                vg.addView(child);
            }
        }
    }

    private ArrayList<BookDetail> getBookDetails() {
        ArrayList<BookDetail> ret = new ArrayList<BookDetail>();
        if (mBookingInput != null && mBookingInput.drinks != null) {
            for (Drink d : mBookingInput.drinks) {
                BookDetail bd = new BookDetail();
                bd.drink_id = d.id;
                bd.quantity = d.number;
                ret.add(bd);
            }
        }
        return ret;
    }
}

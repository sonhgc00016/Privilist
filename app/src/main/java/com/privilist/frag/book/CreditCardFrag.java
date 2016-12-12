package com.privilist.frag.book;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.privilist.R;
import com.privilist.frag.base.BaseFrag;
import com.privilist.util.Utils;

import org.joda.time.LocalDate;

import java.lang.ref.WeakReference;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * Created by minhtdh on 6/29/15.
 */
public class CreditCardFrag extends BaseFrag implements View.OnClickListener {
    private WeakReference<TextView> mTvName, mTvCardNum, mTvDate, mTvCode, mTvPhone;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final
    Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.credit_card_frag, container, false);
        Utils.setOnclick(v.findViewById(R.id.btn_scan), this);
        mTvName = new WeakReference<TextView>((TextView) v.findViewById(R.id.edt_name));
        mTvCardNum = new WeakReference<TextView>((TextView) v.findViewById(R.id.edt_card_num));
        mTvDate = new WeakReference<TextView>((TextView) v.findViewById(R.id.edt_date));
        mTvCode = new WeakReference<TextView>((TextView) v.findViewById(R.id.edt_security_code));
        mTvPhone = new WeakReference<TextView>((TextView) v.findViewById(R.id.edt_phone));
        return v;
    }

    public boolean isValid() {
        // TODO validate value in field
        return true;
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.btn_scan) {
            // TODO call scan;
            Intent scanIntent = new Intent(getActivity(), CardIOActivity.class);

            // customize these values to suit your needs.
            scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
            scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
            scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

            // hides the manual entry button
            // if set, developers should provide their own manual entry mechanism in the app
            scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default: false

            // matches the theme of your application
            scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, false); // default: false

            // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
            startActivityForResult(scanIntent, SCAN_REQUEST_CODE);
        }
    }
    public static final int SCAN_REQUEST_CODE = 65478;
    public static final String EXPIRED_DATE_FORMAT = "MM/dd";
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_REQUEST_CODE) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                if (scanResult.isExpiryValid()) {
                    showAlert("Card expired!!!");
                    return;
                }
                Utils.setText(Utils.getVal(mTvCardNum), scanResult.getFormattedCardNumber());
                Utils.setText(Utils.getVal(mTvDate), LocalDate.now().withMonthOfYear(scanResult
                        .expiryMonth).withYear(scanResult.expiryYear).toString(EXPIRED_DATE_FORMAT));
            }
        }
    }
}

package com.privilist.frag;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.privilist.R;
import com.privilist.component.JacksonRequest;
import com.privilist.frag.base.BaseDialog;

/**
 * Created by minhtdh on 6/17/15.
 */
public class ProgressDialogFrag extends BaseDialog {

    public static ProgressDialogFrag newInstance(CharSequence msg) {
        ProgressDialogFrag pdf = new ProgressDialogFrag();
        pdf.mMsg = msg;
        return pdf;
    }

    private CharSequence mMsg;

    public CharSequence getMsg() {
        return mMsg;
    }

    public void setMsg(final CharSequence pMsg) {
        mMsg = pMsg;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder pd = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.progress_layout, null, false);
        ((TextView) (v.findViewById(android.R.id.message))).setText(mMsg);
        pd.setView(v);
        // SonH June 30, 2015
//        pd.setCancelable(true);
        pd.setCancelable(false);
        // SonH June 30, 2015 End.
        AlertDialog dlg = pd.create();
        dlg.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    // do nothing on back press
                }
                return true;
            }
        });
        dlg.setCancelable(false);
        dlg.setCanceledOnTouchOutside(false);
        customizeDlg(dlg);
        return dlg;
    }

    public static class ProgressRequestDlg<T> extends  ProgressDialogFrag implements Response
    .Listener<T>, Response.ErrorListener {
        public boolean isShowing = false;
        private JacksonRequest.RequestWithDlg<T> mRequest;

        private Response
                .Listener<T> mResListener;
        private Response.ErrorListener mResErrListenner;

        public JacksonRequest.RequestWithDlg<T> getRequest() {
            return mRequest;
        }

        public void setRequest(final JacksonRequest.RequestWithDlg<T> pRequest) {
            mRequest = pRequest;
        }

        public Response.Listener<T> getResListener() {
            return mResListener;
        }

        public void setResListener(final Response.Listener<T> pResListener) {
            mResListener = pResListener;
        }

        public Response.ErrorListener getResErrListenner() {
            return mResErrListenner;
        }

        public void setResErrListenner(final Response.ErrorListener pResErrListenner) {
            mResErrListenner = pResErrListenner;
        }

        @Override
        public void onPause() {
            super.onPause();
            dismiss();
        }

        @Override
        public void onCancel(final DialogInterface dialog) {
            Log.d("ProgressRequestDlg","onCancel");
            super.onCancel(dialog);
            if (mRequest != null) {
                mRequest.cancel();
            }
        }

        @Override
        public void onErrorResponse(final VolleyError error) {
            if (isResumed()) {
                Log.d("ProgressRequestDlg","onErrorResponse");
                dismiss();
                if (mResErrListenner != null) {
                    mResErrListenner.onErrorResponse(error);
                }
            }
        }

        @Override
        public void onResponse(final T response) {
            if (isResumed()) {
                Log.d("ProgressRequestDlg","response");
                dismiss();
                if (mResListener != null) {
                    mResListener.onResponse(response);
                }
            }
        }

        @Override
        public void show(FragmentManager manager, String tag) {
            Log.d("ProgressRequestDlg","show Dialog 2 "+ this);
            isShowing = true;
            super.show(manager, tag);
        }

        @Override
        public void dismiss() {
            Log.d("ProgressRequestDlg","dismiss "+ this);
            isShowing = false;
            super.dismiss();
        }
    }
}

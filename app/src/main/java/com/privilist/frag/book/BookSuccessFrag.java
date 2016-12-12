package com.privilist.frag.book;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.privilist.R;
import com.privilist.frag.base.BackToHomeFrag;
import com.privilist.util.GlideHelper;
import com.privilist.util.Utils;

/**
 * Created by minhtdh on 6/26/15.
 */
public class BookSuccessFrag extends BackToHomeFrag {

    public static final String BOOK_SUCCESS_DATE_FORMAT = "MMMM dd";

    private String mDate, mClubName, mCode, mImagePath;

    public static BookSuccessFrag newInstance(String date, String name, String code, String
            imagePath) {
        BookSuccessFrag frag = new BookSuccessFrag();
        frag.mDate = date;
        frag.mClubName = name;
        frag.mCode = code;
        frag.mImagePath = imagePath;
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final
    Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.book_success_frag, container, false);
        // TODO set date color
        Utils.setText(ret.findViewById(R.id.tv_book_date), mDate);
        Utils.setText(ret.findViewById(R.id.tv_club_name), mClubName);
        Utils.setText(ret.findViewById(R.id.tv_book_code), mCode);
        GlideHelper.loadImage(mImagePath, (ImageView) ret.findViewById(R.id.img_table));
        return ret;
    }
}

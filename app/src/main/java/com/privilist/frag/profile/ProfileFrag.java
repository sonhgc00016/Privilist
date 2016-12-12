package com.privilist.frag.profile;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.GlideCircleTransform;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.privilist.R;
import com.privilist.component.JacksonRequest;
import com.privilist.define.Constant;
import com.privilist.frag.BookingsFrag;
import com.privilist.frag.ProgressDialogFrag;
import com.privilist.frag.base.BaseFrag;
import com.privilist.frag.profile.accountsetting.AccountSettingsFrag;
import com.privilist.frag.profile.profiledetails.ProfileDetailsFrag;
import com.privilist.model.Booking;
import com.privilist.model.City;
import com.privilist.model.Reward;
import com.privilist.model.User;
import com.privilist.util.ApiHelper;
import com.privilist.util.GlideHelper;
import com.privilist.util.Log;
import com.privilist.util.UserHelper;
import com.privilist.util.Utils;

import org.apache.http.Header;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by SonH on 6/26/15.
 */
public class ProfileFrag extends BaseFrag implements View.OnClickListener {

    private User mUser;
    private City mCurrentLocation;
    private WeakReference<ImageView> mImgAvatar, mImgRank, mImgUpcommingBooking, mImgArrow;
    private WeakReference<TextView> mTvName, mTvRank, mTvBookDate, mTvClubName, mTvBookcode;
    private WeakReference<RelativeLayout> mRltUpcommingBooking;
    private final ProgressDialogFrag.ProgressRequestDlg mDialog = new ProgressDialogFrag.ProgressRequestDlg();
    private Booking mUpcommingBooking = null;

    public void setmCurrentLocation(City mCurrentLocation) {
        this.mCurrentLocation = mCurrentLocation;
    }

    public ProfileFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDialog.setCancelable(false);

        ImageView imv;
        imv = (ImageView) view.findViewById(R.id.profile_imgAvatar);
        mImgAvatar = new WeakReference<ImageView>(imv);
        Utils.setOnclick(view.findViewById(R.id.profile_imgAvatar), this);
        imv = (ImageView) view.findViewById(R.id.profile_imgRank);
        mImgRank = new WeakReference<ImageView>(imv);
        imv = (ImageView) view.findViewById(R.id.profile_imgRank);
        mImgRank = new WeakReference<ImageView>(imv);
        imv = (ImageView) view.findViewById(R.id.profile_imgArrow);
        mImgArrow = new WeakReference<ImageView>(imv);

        TextView tv;
        tv = (TextView) view.findViewById(R.id.profile_tvName);
        mTvName = new WeakReference<TextView>(tv);
        tv = (TextView) view.findViewById(R.id.profile_tvRank);
        mTvRank = new WeakReference<TextView>(tv);

        RelativeLayout rlt;
        rlt = (RelativeLayout) view.findViewById(R.id.profile_rltUpcommingBooking);
        mRltUpcommingBooking = new WeakReference<RelativeLayout>(rlt);

        imv = (ImageView) view.findViewById(R.id.profile_imgUpcommingBooking);
        mImgUpcommingBooking = new WeakReference<ImageView>(imv);

        tv = (TextView) view.findViewById(R.id.profile_tvBookDate);
        mTvBookDate = new WeakReference<TextView>(tv);
        tv = (TextView) view.findViewById(R.id.profile_tvClubName);
        mTvClubName = new WeakReference<TextView>(tv);
        tv = (TextView) view.findViewById(R.id.profile_tvBookCode);
        mTvBookcode = new WeakReference<TextView>(tv);

        Utils.setOnclick(view.findViewById(R.id.profile_tvClose), this);
        Utils.setOnclick(view.findViewById(R.id.profile_tvAddBooking), this);
        Utils.setOnclick(view.findViewById(R.id.profile_tvName), this);
        Utils.setOnclick(view.findViewById(R.id.profile_tvBookings), this);
        Utils.setOnclick(view.findViewById(R.id.profile_tvReferAFriend), this);
        Utils.setOnclick(view.findViewById(R.id.profile_tvRewards), this);
        Utils.setOnclick(view.findViewById(R.id.profile_tvAccountSettings), this);
        Utils.setOnclick(view.findViewById(R.id.profile_tvPayment), this);
        Utils.setOnclick(view.findViewById(R.id.profile_tvContactUs), this);
        Utils.setOnclick(view.findViewById(R.id.profile_tvLogout), this);
        Utils.setOnclick(view.findViewById(R.id.profile_imgArrow), this);
        Utils.setOnclick(view.findViewById(R.id.profile_rltUpcommingBooking), this);

        doGetUserProfile();
        doGetUpcommingBooking();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.profile_tvClose:
                finish();
                break;
            case R.id.profile_tvAddBooking:
                // TODO Open Add Booking
                AddBookingFrag pAddBookingFrag = new AddBookingFrag();
                move(pAddBookingFrag);
                break;
            case R.id.profile_tvName:
                // TODO Open Profile Details
                if (mUser != null) {
                    ProfileDetailsFrag pProfileDetailsFrag = new ProfileDetailsFrag();
                    pProfileDetailsFrag.setmUser(mUser);
                    move(pProfileDetailsFrag);
                }
                break;
            case R.id.profile_imgArrow:
                // TODO Open Profile Details
                if (mUser != null) {
                    ProfileDetailsFrag profileDetailsFrag = new ProfileDetailsFrag();
                    profileDetailsFrag.setmUser(mUser);
                    move(profileDetailsFrag);
                }
                break;
            case R.id.profile_tvBookings:
                // TODO Open Bookings
                BookingsFrag bookingsFrag = new BookingsFrag();
                move(bookingsFrag);
                break;
            case R.id.profile_tvReferAFriend:
                // TODO Open Refer A Friend
                if (mUser != null) {
                    ReferAFriendFrag frag = ReferAFriendFrag.newInstance(mUser.referral_code);
                    move(frag);
                }
                break;
            case R.id.profile_tvRewards:
                // TODO Open Rewards
                if (mCurrentLocation != null) {
                    doGetRewwards(mCurrentLocation);
                }
                break;
            case R.id.profile_tvAccountSettings:
                // TODO Open Account Settings
                if (mUser != null) {
                    AccountSettingsFrag frag = new AccountSettingsFrag();
                    frag.setmUser(mUser);
                    move(frag);
                }
                break;
            case R.id.profile_tvPayment:
                // TODO Open Payment
                break;
            case R.id.profile_tvContactUs:
                // TODO Open Contact Us
                ContactUsFrag frag = new ContactUsFrag();
                move(frag);
                break;
            case R.id.profile_tvLogout:
                // TODO Logout
                // logout
                UserHelper.getIns().logout(getActivity());
                break;
            case R.id.profile_imgAvatar:
                // TODO Change profile picture
                if (mUser != null) {
                    selectImage();
                }
                break;
            case R.id.profile_rltUpcommingBooking:
                // TODO Open Upcomming Booking
                if (mUpcommingBooking != null) {
                    UpComingBooking upComingBooking = new UpComingBooking();
                    upComingBooking.setmBooking(mUpcommingBooking);
                    move(upComingBooking);
                }
                break;
            default:
                break;
        }
    }

    private void doGetUserProfile() {
        String access_token = UserHelper.getIns().getSavedAccessToken(getActivity());
        if (access_token != null) {
            ProgressDialogFrag.ProgressRequestDlg<User> dlg = new
                    ProgressDialogFrag.ProgressRequestDlg<User>();

            Response.Listener<User> mListener = new Response.Listener<User>() {
                @Override
                public void onResponse(final User response) {
                    if (getApp() != null) {
                        // TODO with response
                        mUser = response;
                        // set user info
                        if (mUser != null) {
                            ImageView imv;
                            TextView tv;
                            imv = Utils.getVal(mImgAvatar);
                            if (mUser.image != null && !mUser.image.url.isEmpty() && !mUser.image.url.equals("")) {
                                try {
                                    URL url = new URL(mUser.image.url);
                                    Uri uri = Uri.parse(url.toURI().toString());
                                    Glide.with(getActivity())
                                            .load(uri)
                                            .transform(new GlideCircleTransform(getActivity()))
                                            .into(imv);
                                    // set border for image
                                    imv.setBackgroundResource(R.drawable.avatar_border);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (UserHelper.getIns().getLoginInfo() != null && UserHelper.getIns().getLoginInfo().avatarFBUrl != null) {
                                    urlFBAvatar = UserHelper.getIns().getLoginInfo().avatarFBUrl;
                                    try {
                                        URL imageURL = new URL(urlFBAvatar);
                                        Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                                        uploadAvatarToServer(ApiHelper.getIns().uploadImageUrl(), bitmap, mPhotoName);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    // set border for image
                                    imv.setBackgroundResource(R.drawable.avatar_border);
                                }
                            }
                            imv = Utils.getVal(mImgArrow);
                            imv.setBackgroundResource(R.drawable.arrow_profile);

                            tv = Utils.getVal(mTvName);
                            tv.setText(mUser.full_name);
                            tv = Utils.getVal(mTvRank);
                            tv.setText(mUser.rank.name + " member");
                            tv.setTextColor(Color.parseColor(mUser.rank.color));

                            imv = Utils.getVal(mImgRank);
                            if (!mUser.rank.icon.isEmpty() && !mUser.rank.icon.equals("")) {
                                GlideHelper.loadImage(mUser.rank.icon, imv);
                                imv.setVisibility(View.VISIBLE);
                            } else {
                                imv.setVisibility(View.GONE);
                            }
                        }
                        ;
                    }
                }
            };

            JacksonRequest.RequestWithDlg<User> st =
                    new JacksonRequest.RequestWithDlg<User>
                            (User.class, ApiHelper.getIns().getUserInfoUrl(access_token), dlg,
                                    mListener,
                                    onErrorLis);
            st.setContentType(ApiHelper.CONTENT_TYPE_FORM);
            st.request(getApp().getRequestQueue(), getChildFragmentManager());
        }
    }

    private void doGetUpcommingBooking() {
        String access_token = UserHelper.getIns().getSavedAccessToken(getActivity());
        if (access_token != null) {
            ProgressDialogFrag.ProgressRequestDlg<ArrayList<Booking>> dlg = new
                    ProgressDialogFrag.ProgressRequestDlg<ArrayList<Booking>>();

            Response.Listener<ArrayList<Booking>> listener = new Response.Listener<ArrayList<Booking>>() {
                @Override
                public void onResponse(final ArrayList<Booking> response) {
                    RelativeLayout rlt;
                    rlt = Utils.getVal(mRltUpcommingBooking);
                    if (response != null && response.size() > 0) {
                        for (Booking booking : response) {
                            if (!booking.isExpired())
                                mUpcommingBooking = booking;
                        }
                        if (mUpcommingBooking != null) {
                            ImageView imv;
                            TextView tv;
                            imv = Utils.getVal(mImgUpcommingBooking);
                            if (mUpcommingBooking.getEvent() != null) {
                                GlideHelper.loadImage(mUpcommingBooking.getEvent().images.get(0).url + "?expired=0", imv);
                            } else {
                                GlideHelper.loadImage(mUpcommingBooking.getTable().getVenue().images.get(0).url + "?expired=0", imv);
                            }
                            if (mUpcommingBooking.getBooking_date() != null) {
                                LocalDate localDate = LocalDate.parse(String.valueOf(mUpcommingBooking.getBooking_date()), DateTimeFormat.forPattern(Constant.DATE_FORMAT));
                                tv = Utils.getVal(mTvBookDate);
                                tv.setText(localDate.toString("MMMM dd"));
                            }
                            tv = Utils.getVal(mTvClubName);
                            tv.setText(mUpcommingBooking.getTable().getVenue().name);
                            tv = Utils.getVal(mTvBookcode);
                            tv.setText(mUpcommingBooking.getCode());
                            rlt.setVisibility(View.VISIBLE);
                        }
                    } else {
                        rlt.setVisibility(View.GONE);
                    }
                }
            };

            JacksonRequest.ArrayRequestWithDlg<Booking>
                    cr = new JacksonRequest.ArrayRequestWithDlg<Booking>(Booking.class,
                    ApiHelper.getIns().getBookingsUrl(access_token), dlg, listener, onErrorLis);
            cr.setShouldCache(true);
            cr.request(getApp().getRequestQueue(), getChildFragmentManager());
        }
    }

    /**
     * @param pCity
     */
    private void doGetRewwards(City pCity) {
        ProgressDialogFrag.ProgressRequestDlg<ArrayList<Reward>> dlg = new
                ProgressDialogFrag.ProgressRequestDlg<ArrayList<Reward>>();

        Response.Listener<ArrayList<Reward>> listener = new Response.Listener<ArrayList<Reward>>() {
            @Override
            public void onResponse(final ArrayList<Reward> response) {
                if (mUser != null) {
                    RewardListFrag rewardListFrag = new RewardListFrag();
                    rewardListFrag.setmUser(mUser);
                    rewardListFrag.setmArrRewards(response);
                    move(rewardListFrag);
                }
            }
        };

        JacksonRequest.ArrayRequestWithDlg<Reward>
                cr = new JacksonRequest.ArrayRequestWithDlg<Reward>(Reward.class,
                ApiHelper.getIns().getRewardsUrl(pCity.id), dlg, listener, onErrorLis);
        cr.setShouldCache(true);
        cr.request(getApp().getRequestQueue(), getChildFragmentManager());
    }

    private Response.ErrorListener onErrorLis = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(final VolleyError error) {
            CharSequence errMsg = error == null ? Constant.EMPTY : error.getLocalizedMessage();
            errMsg = errMsg == null ? Constant.EMPTY : errMsg;
            if (Log.isDLoggable(this)) {
                String err = null;
                try {
                    err = new String(error.networkResponse.data, "UTF-8");
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
                Log.d(TAG, new StringBuilder("onErrorResponse ").append(" err=").append(err).append(
                        this).toString());
            }
            showAlert(getString(R.string.getting_data_error, errMsg));
            commonRequestErr(error);
        }
    };

    public static final int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String mPhotoPath;
    private File mPhotoFile;
    private String mPhotoName;
    private String urlFBAvatar;

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        try {
                            mPhotoFile = createImageFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // Continue only if the File was successfully created
                        if (mPhotoFile != null) {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(mPhotoFile));
                            startActivityForResult(intent, REQUEST_CAMERA);
                        }
                    }
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                galleryAddPic();
        }
    }

    /**
     * save picture captured to gallery and replace new picture to imgAvatar
     */
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
        /*Glide.with(getActivity())
                .load(mPhotoPath)
                .transform(new GlideCircleTransform(getActivity()))
                .into(imgAvatar);*/
        // post to server
        if (mPhotoPath != null && mPhotoName != null) {
            mPhotoFile = new File(mPhotoPath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath, options);
            uploadAvatarToServer(ApiHelper.getIns().uploadImageUrl(), bitmap, mPhotoName);
        }
    }

    /**
     * @param data
     */
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        mPhotoPath = cursor.getString(columnIndex);
        cursor.close();

        /*Glide.with(getActivity())
                .load(mPhotoPath)
                .transform(new GlideCircleTransform(getActivity()))
                .into(imgAvatar);*/

        if (mPhotoPath != null) {
            File file = new File(mPhotoPath);
            mPhotoName = file.getName();
            // post to server
            if (mPhotoPath != null && mPhotoName != null) {
                mPhotoFile = new File(mPhotoPath);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath, options);
                uploadAvatarToServer(ApiHelper.getIns().uploadImageUrl(), bitmap, mPhotoName);
            }
        }
    }

    /**
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mPhotoName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                mPhotoName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mPhotoPath = image.getAbsolutePath();
        return image;
    }

    // SonH July 09, 2015
    // add method change avatar
    public void uploadAvatarToServer(String url, Bitmap bitmap, String pPhotoName) {
        String access_token = UserHelper.getIns().getSavedAccessToken(getActivity());
        if (access_token != null) {
            mDialog.show(getFragmentManager(), mDialog.getClass().getSimpleName());
            RequestParams params = new RequestParams();
            params.put("access_token", access_token);
            int imageHeight = bitmap.getHeight();
            int imageWidth = bitmap.getWidth();
            Bitmap newBitmap = getResizedBitmap(bitmap, 512, 512 * imageWidth / imageHeight);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] imageBytes = baos.toByteArray();
            params.put("user_image", new ByteArrayInputStream(imageBytes), pPhotoName); // Upload a File

            AsyncHttpClient client = new AsyncHttpClient();
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    mDialog.dismiss();
                    Log.i("onSuccess", "statusCode" + statusCode + "headers" + headers + "responseBody" + responseBody);
                    doGetUserProfile();
                    // upload image success image saved to server remove old url
                    if (urlFBAvatar != null)
                        UserHelper.getIns().getLoginInfo().avatarFBUrl = null;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    mDialog.dismiss();
                    showAlert(getString(R.string.default_request_network_err_msg));
                    Log.i("onFailure", "statusCode" + statusCode + "headers" + headers + "responseBody" + responseBody + "error" + error);
                }
            });
        }
    }

    /**
     * @param bm
     * @param newHeight
     * @param newWidth
     * @return
     */
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // Create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // Resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // Recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;

    }
    // SonH July 09, 2015 End.
}

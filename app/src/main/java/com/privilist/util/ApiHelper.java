package com.privilist.util;

import com.android.volley.Request;
import com.privilist.define.Constant;
import com.privilist.model.BookIPO;
import com.privilist.model.ChangePasswordIPO;
import com.privilist.model.EventForDateIPO;
import com.privilist.model.LoginIPO;
import com.privilist.model.RegistrationIPO;
import com.privilist.model.SpecialRequestIPO;
import com.privilist.model.TableListIPO;
import com.privilist.model.UpdateUserIPO;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class ApiHelper {

    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

    protected ApiHelper() {
        super();
    }

    static class Holder {
        static final ApiHelper sInstance = new ApiHelper();
    }

    public static ApiHelper getIns() {
        return Holder.sInstance;
    }

    protected StringBuilder initUrl() {
        return new StringBuilder(Constant.DOMAIN_NAME);
    }

    public static final int SIGNUP_SUCCESS_CODE = 201;
    public static final int SIGNUP_FAILURE_CODE = 422;

    public String getSignUpUrl() {
        return initUrl().append("v1/users").toString();
    }

    public String buildSignUpRequest(RegistrationIPO input) {
        String ret = null;
        if (input != null) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("first_name", input.first_name);
            params.put("last_name", input.last_name);
            params.put("email", input.email);
            if (input.phone_number != 0) {
                params.put("phone_number", String.valueOf(input.phone_number));
            }
            params.put("password", input.password);
            params.put("birthday", input.birthday);
            params.put("gender", input.gender);
            ret = Request.encodeParametersToString(params, Request.DEFAULT_PARAMS_ENCODING);
        }
        return ret;
    }

    public static final String SIGNIN_DF_GRANT_TYPE = "password";
    public static final String SIGNIN_DF_CLIENT_ID = "MY8jF8cBZDDXOHh4";
    public static final String SIGNIN_DF_CLIENT_SECRET = "KTCC95N0FygW9j1HdpCXVNgztiykBQk1";

    public static final int SIGNIN_FAILURE_CODE = 401;

    public String getSigninUrl() {
        return initUrl().append("oauth/access_token").toString();
    }

    public String buildSigninRequest(LoginIPO input) {
        String ret = null;
        if (input != null) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("grant_type", input.grant_type);
            params.put("client_id", input.client_id);
            params.put("client_secret", input.client_secret);
            params.put("username", input.username);
            params.put("password", input.password);
            ret = Request.encodeParametersToString(params, Request.DEFAULT_PARAMS_ENCODING);
        }
        return ret;
    }

    // SonH June 25, 2015
    // work with FB SDK
    public String getSigninFBUrl() {
        return initUrl().append("oauth/facebook").toString();
    }

    public String buildSigninFBRequest(LoginIPO input) {
        String ret = null;
        if (input != null) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("client_id", input.client_id);
            params.put("client_secret", input.client_secret);
            params.put("username", input.emailFB);
            params.put("first_name", input.firstname);
            params.put("last_name", input.lastname);
            params.put("facebook_id", input.idFB);
            params.put("hash", input.hash);
            ret = Request.encodeParametersToString(params, Request.DEFAULT_PARAMS_ENCODING);
        }
        return ret;
    }
    // SonH June 25, 2015 End.

    public String getUserInfoUrl(String accessToken) {
        return initUrl().append("v1/me?access_token=").append(accessToken).toString();
    }

    public String getLocationsUrl() {
        return initUrl().append("v1/locations").toString();
    }

    public String getVenues(long location_id) {
        return initUrl().append("v1/locations/").append(location_id).append("/venues").toString();
    }

    // luanlq July 3,2015
    public String getSearchData(long location_id, String searchKey) {
        return initUrl().append("v1/locations/").append(location_id).append("/search?q=")
                .append(searchKey).toString();
    }

    // luanlq July 3,2015 End.
    public String getTonightEvents(long location_id) {
        return initUrl().append("v1/locations/").append(location_id).append("/tonight").toString();
    }

    //luanlq July 10,2015
    public String getUpcomingEvents(long location_id) {
        return initUrl().append("v1/locations/").append(location_id).append("/events").toString();
    }
    //End.

    public String getDrinkUrl(long venueId) {
        return initUrl().append("v1/venues/").append(venueId).append("/drinks").toString();
    }

    public String getNoDateTableListUrl(TableListIPO input) {
        return initUrl().append("v1/venues/").append(input.venueID).append(
                "?_with=tables&access_token=").append(input.access_token).toString();
    }

    public String getTableListUrl(TableListIPO input) {
        return initUrl().append("v1/venues/").append(input.venueID).append
                ("?_with=tables&booking_date=").append(input.date.toString(Constant.DF_DATE_FORMAT))
                .append("&access_token=").append(input.access_token)
                .toString();
    }

    //luanlq July,22 2015
    public String getEventForTableListUrl(EventForDateIPO input) {
        return initUrl().append("v1/events/").append("?_with=images&date=")
                .append(input.date.toString(Constant.DF_DATE_FORMAT)).append("&venue_id=")
                .append(input.venueID).toString();
    }

    //luanlq July,22 2015 End.
    public static final int SPECIAL_REQUEST_SUCCESS_CODE = 201;

    public String getSpecialRequestUrl(long venueId) {
        return initUrl().append("v1/venues/").append(venueId).append("/request").toString();
    }

    public String buildSpecialRequest(SpecialRequestIPO input) {
        String ret = null;
        if (input != null) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("access_token", input.access_token);
            params.put("minimum_spend", String.valueOf(input.minimum_spend));
            params.put("request_date", input.request_date);
            if (input.guest > 0) {
                params.put("guest", String.valueOf(input.guest));
            }
            if (Utils.notEmpty(input.requests)) {
                params.put("requests", input.requests);
            }
            if (Utils.notEmpty(input.request_phone_number)) {
                params.put("request_phone_number", input.request_phone_number);
            }
            ret = Request.encodeParametersToString(params, Request.DEFAULT_PARAMS_ENCODING);
        }
        return ret;
    }

    public static final int BOOK_SUCCESS_CODE = 201;

    public String getBookUrl(BookIPO input) {
        return initUrl().append("v1/bookings/book/").append(input.table_id).toString();
    }

    public String buildBookRequest(BookIPO input) {
        String ret = null;
        if (input != null) {

            Map<String, String> params = new HashMap<String, String>();
            params.put("access_token", input.access_token);
            params.put("total", String.valueOf(input.total));
            params.put("details", JsonUtil.writeString(input.details));
            params.put("payment-method-nonce", input.payment_method_nonce);
            params.put("currency", input.currency);
            if (input.date != null) {
                // TODO need confirm time send to server
                LocalDateTime time = LocalDateTime.now().withDate(input.date.getYear(), input.date
                        .getMonthOfYear(), input.date.getDayOfMonth());
                DateTimeZone dtz = DateTimeZone.forTimeZone(TimeZone.getDefault());
                Date d = new Date(dtz.convertUTCToLocal(time.toDate().getTime()));
                LocalDateTime ldt = LocalDateTime.fromDateFields(d);
                params.put("booking_date", ldt.toString(Constant.DATE_FORMAT));
            }
            if (input.event_id > 0) {
                params.put("event_id", String.valueOf(input.event_id));
            }
            ret = Request.encodeParametersToString(params, Request.DEFAULT_PARAMS_ENCODING);
        }
        return ret;
    }

    // SonH June 28, 2015
    // add url request get point history
    public String getPointHistoryUrl(String accessToken) {
        return initUrl().append("v1/me/point-history?access_token=").append(accessToken).toString();
    }
    // SonH June 28, 2015 End.

    public String getBrainTreeTokenUrl(String access_token) {
        return initUrl().append("v1/payment/token?access_token=" + access_token).toString();
    }

    // SonH July 02, 2015
    // add url request get rank
    public String getRankUrl() {
        return initUrl().append("v1/rank").toString();
    }

    // add url request get bookings
    public String getBookingsUrl(String accessToken) {
        return initUrl().append("v1/me/bookings?access_token=").append(accessToken).toString();
    }
    // SonH July 02, 2015

    // SonH July 03, 2015
    // add url request get booking by booking_id
    public String getBookingUrl(long booking_id) {
        return initUrl().append("v1/bookings/").append(booking_id).append("?_with=table.venue.images,details.drink").toString();
    }
    // SonH July 03, 2015 End.

    // SonH July 08, 2015
    // add url request get rewards by location
    public String getRewardsUrl(long location_id) {
        return initUrl().append("v1/rewards?location_id=").append(location_id).toString();
    }

    // add url request redeem reward
    public String getRedeemRewardUrl(long reward_id) {
        return initUrl().append("v1/rewards/").append(reward_id).append("/redeem").toString();
    }

    public static final int REDEEM_REWARD_SUCCESS_CODE = 201;

    public String buildRedeemRewardRequest(String access_token) {
        String ret;
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", access_token);
        ret = Request.encodeParametersToString(params, Request.DEFAULT_PARAMS_ENCODING);
        return ret;
    }

    // add url request add booking
    public String addBookingUrl(String code) {
        return initUrl().append("v1/bookings/add/").append(code).toString();
    }

    public static final int ADD_BOOKING_FAILURE_ALREADY_BOOKED_CODE = 422;
    public static final int ADD_BOOKING_FAILURE_NOT_FOUND_CODE = 404;

    public String buildAddBookingRequest(String access_token) {
        String ret;
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", access_token);
        ret = Request.encodeParametersToString(params, Request.DEFAULT_PARAMS_ENCODING);
        return ret;
    }

    // add url request upload image
    public String uploadImageUrl() {
        return initUrl().append("v1/me/upload-avatar").toString();
    }

    // add url request get rewards by location
    public String getMyRewardsUrl(String accessToken) {
        return initUrl().append("v1/me/rewards?access_token=").append(accessToken).toString();
    }

    // add url request get Upcomming/Expired Booking
    public String getBookingInfo(long pBookingId) {
        return initUrl().append("v1/bookings/").append(pBookingId).append("?_with=table.venue.images,details.drink").toString();
    }

    // SonH July 08, 2015 End.

    // SonH August 12, 2015
    // Add url request Update user
    public String updateUserUrl(long pUserId) {
        return initUrl().append("v1/users/").append(pUserId).toString();
    }

    public String buildUpdateUserRequest(UpdateUserIPO input) {
        String ret;
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", input.access_token);
        params.put("first_name", input.first_name);
        params.put("last_name", input.last_name);
        params.put("birthday", input.birthday);
        params.put("gender", input.gender);
        params.put("phone_number", input.phone_number);
        ret = Request.encodeParametersToString(params, Request.DEFAULT_PARAMS_ENCODING);
        return ret;
    }

    // Add url request Change Password
    public String changePassUrl() {
        return initUrl().append("v1/me/password").toString();
    }

    public String buildChangePassRequest(ChangePasswordIPO input) {
        String ret;
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", input.access_token);
        params.put("old_password", input.old_password);
        params.put("password", input.password);
        ret = Request.encodeParametersToString(params, Request.DEFAULT_PARAMS_ENCODING);
        return ret;
    }

    // Add url request get Contact
    public String getContactUsUrl() {
        return initUrl().append("v1/contact").toString();
    }
    // SonH August 12, 2015 End.
}

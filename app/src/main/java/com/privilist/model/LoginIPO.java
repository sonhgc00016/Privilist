package com.privilist.model;

import com.privilist.util.ApiHelper;

public class LoginIPO {

    public String grant_type = ApiHelper.SIGNIN_DF_GRANT_TYPE;
    public String client_id = ApiHelper.SIGNIN_DF_CLIENT_ID;
    public String client_secret = ApiHelper.SIGNIN_DF_CLIENT_SECRET;
    public String username;
    public String password;

    // SonH June 25, 2015
    // login facebook
    public String idFB;
    public String emailFB;
    public String hash;
    public String firstname;
    public String lastname;
    // SonH June 25, 2015 End.
}

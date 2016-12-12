package com.privilist.define;

/**
 * Created by minhtdh on 6/10/15.
 */
public interface Constant {

    String EMPTY = "";
    //luanlq July,31 2015
    String NO_TABLE = "0 TABLES LEFT";
    //luanlq July,31 2015 End.

    int REQUEST_TIME_OUT = 20000;// 20 s
    /**
     * size in bytes, 64MB
     */
    int MEM_CACHE_SIZE = 1024 * 1024 * 64;// 64 MB
    /**
     * size in bytes, 256MB
     */
    int DISK_CACHE_SIZE = 1024 * 1024 * 256;// 256 MB
    /**
     * size in bytes, 64MB
     */
    int POOL_CACHE_SIZE = 1024 * 1024 * 64;// 64 MB
    
    String COMMON_ACTION_ROOT = "com.privilist.";
    
    String ACTION_TOKEN_EXPIRES = new StringBuilder(COMMON_ACTION_ROOT).append("token_expires")
            .toString();
    
    String DOMAIN_NAME = "http://api.privilist.cf/";
    
    String MALE = "male";
    String FEMALE = "female";

    String DF_DATE_FORMAT = "yyyy-MM-dd";
    int THRESHOLD_AGE = 21;

    String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    long UNKNOW_ID = -1;

}

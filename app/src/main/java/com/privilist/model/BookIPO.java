package com.privilist.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;

import java.util.ArrayList;

/**
 * Created by minhtdh on 6/26/15.
 */
public class BookIPO {
    @JsonIgnore
    public long table_id;

    @JsonIgnore
    public LocalDate date;

    public String access_token;
    public String total;
    public ArrayList<BookDetail> details;
    @JsonProperty("payment-method-nonce")
    public String payment_method_nonce;
    public String currency;
    public int event_id;
}

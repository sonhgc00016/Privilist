package com.privilist.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
public class RegistrationOPO extends RegistrationIPO {

    public String updated_at;
    public String created_at;
    public long id;
}

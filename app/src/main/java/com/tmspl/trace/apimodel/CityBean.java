package com.tmspl.trace.apimodel;

/**
 * Created by Manan on 01/10/2015.
 */
public class CityBean {

    String Key,Value;

    public CityBean(String key, String value) {
        Key = key;
        Value = value;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}

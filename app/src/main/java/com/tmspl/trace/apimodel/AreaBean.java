package com.tmspl.trace.apimodel;

/**
 * Created by Manan on 01/10/2015.
 */
public class AreaBean {
    String Key,Value,LatLong;

    public AreaBean(String key, String value, String latLong) {
        Key = key;
        Value = value;
        LatLong = latLong;
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

    public String getLatLong() {
        return LatLong;
    }

    public void setLatLong(String latLong) {
        LatLong = latLong;
    }
}

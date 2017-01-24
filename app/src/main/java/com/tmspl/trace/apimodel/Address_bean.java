package com.tmspl.trace.apimodel;

/**
 * Created by rakshit.sathwara on 1/24/2017.
 */

public class Address_bean {

    String Key,Name,Value,LatLong,data_city;

    public Address_bean(String key, String name, String value, String latLong,String data_city) {
        Key = key;
        Name = name;
        Value = value;
        LatLong = latLong;
        this.data_city=data_city;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public String getData_city() {
        return data_city;
    }

    public void setData_city(String data_city) {
        this.data_city = data_city;
    }
}
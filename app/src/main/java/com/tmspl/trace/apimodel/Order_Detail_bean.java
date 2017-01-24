package com.tmspl.trace.apimodel;

/**
 * Created by rakshit.sathwara on 1/24/2017.
 */
public class Order_Detail_bean {

    String name,address_id,address_line_1,address_line_2,area_key,city_key,area_value,city_value,state,pin,mobile,individual_price,km,lat_long;
    int index;

    public Order_Detail_bean() {
    }

    public Order_Detail_bean(String name, String address_id, String address_line_1, String address_line_2, String area_key, String city_key, String area_value, String city_value, String state, String pin, String mobile, String individual_price, String km, String lat_long,int index) {
        this.name = name;
        this.address_id = address_id;
        this.address_line_1 = address_line_1;
        this.address_line_2 = address_line_2;
        this.area_key = area_key;
        this.city_key = city_key;
        this.area_value = area_value;
        this.city_value = city_value;
        this.state = state;
        this.pin = pin;
        this.mobile = mobile;
        this.individual_price = individual_price;
        this.km = km;
        this.lat_long = lat_long;
        this.index=index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getAddress_line_1() {
        return address_line_1;
    }

    public void setAddress_line_1(String address_line_1) {
        this.address_line_1 = address_line_1;
    }

    public String getAddress_line_2() {
        return address_line_2;
    }

    public void setAddress_line_2(String address_line_2) {
        this.address_line_2 = address_line_2;
    }

    public String getArea_key() {
        return area_key;
    }

    public void setArea_key(String area_key) {
        this.area_key = area_key;
    }

    public String getCity_key() {
        return city_key;
    }

    public void setCity_key(String city_key) {
        this.city_key = city_key;
    }

    public String getArea_value() {
        return area_value;
    }

    public void setArea_value(String area_value) {
        this.area_value = area_value;
    }

    public String getCity_value() {
        return city_value;
    }

    public void setCity_value(String city_value) {
        this.city_value = city_value;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIndividual_price() {
        return individual_price;
    }

    public void setIndividual_price(String individual_price) {
        this.individual_price = individual_price;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getLat_long() {
        return lat_long;
    }

    public void setLat_long(String lat_long) {
        this.lat_long = lat_long;
    }
}


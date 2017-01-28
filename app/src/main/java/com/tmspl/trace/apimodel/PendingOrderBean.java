package com.tmspl.trace.apimodel;

/**
 * Created by Manan on 28/09/2015.
 */
public class PendingOrderBean {

    String parcel_img, start, end, count, amount, order_id;

    public PendingOrderBean(String parcel_img, String start, String end, String count, String amount, String order_id) {
        this.parcel_img = parcel_img;
        this.start = start;
        this.end = end;
        this.count = count;
        this.amount = amount;
        this.order_id = order_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getParcel_img() {
        return parcel_img;
    }

    public void setParcel_img(String parcel_img) {
        this.parcel_img = parcel_img;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

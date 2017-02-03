package com.tmspl.trace.apimodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rakshit on 03-02-2017 at 16:51.
 */

public class RiderPendingOrderResponse {


    /**
     * status : 1
     * responseJson : [{"order_track_id":"BVATJP1486120721","order_id":"2","image":"noimage.jpg","total_amount":"245.40000915527344","total_km":"34.400001525878906","total_deliveries":"1","from_area":"M G Road, Block B, Gurgaon, Haryana, India","to_area":"Noida Link Road, Mayur Vihar Phase 1, New Delhi, Delhi, India","lat_long":"28.4769794,77.06597359999999"}]
     * responseMessage : Successfully get pending order list!
     */

    @SerializedName("status")
    private int status;
    @SerializedName("responseMessage")
    private String responseMessage;
    @SerializedName("responseJson")
    private List<ResponseJsonBean> responseJson;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public List<ResponseJsonBean> getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(List<ResponseJsonBean> responseJson) {
        this.responseJson = responseJson;
    }

    public static class ResponseJsonBean {
        /**
         * order_track_id : BVATJP1486120721
         * order_id : 2
         * image : noimage.jpg
         * total_amount : 245.40000915527344
         * total_km : 34.400001525878906
         * total_deliveries : 1
         * from_area : M G Road, Block B, Gurgaon, Haryana, India
         * to_area : Noida Link Road, Mayur Vihar Phase 1, New Delhi, Delhi, India
         * lat_long : 28.4769794,77.06597359999999
         */

        @SerializedName("order_track_id")
        private String orderTrackId;
        @SerializedName("order_id")
        private String orderId;
        @SerializedName("image")
        private String image;
        @SerializedName("total_amount")
        private String totalAmount;
        @SerializedName("total_km")
        private String totalKm;
        @SerializedName("total_deliveries")
        private String totalDeliveries;
        @SerializedName("from_area")
        private String fromArea;
        @SerializedName("to_area")
        private String toArea;
        @SerializedName("lat_long")
        private String latLong;

        public String getOrderTrackId() {
            return orderTrackId;
        }

        public void setOrderTrackId(String orderTrackId) {
            this.orderTrackId = orderTrackId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getTotalKm() {
            return totalKm;
        }

        public void setTotalKm(String totalKm) {
            this.totalKm = totalKm;
        }

        public String getTotalDeliveries() {
            return totalDeliveries;
        }

        public void setTotalDeliveries(String totalDeliveries) {
            this.totalDeliveries = totalDeliveries;
        }

        public String getFromArea() {
            return fromArea;
        }

        public void setFromArea(String fromArea) {
            this.fromArea = fromArea;
        }

        public String getToArea() {
            return toArea;
        }

        public void setToArea(String toArea) {
            this.toArea = toArea;
        }

        public String getLatLong() {
            return latLong;
        }

        public void setLatLong(String latLong) {
            this.latLong = latLong;
        }
    }
}

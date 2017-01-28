package com.tmspl.trace.apimodel;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rakshit.sathwara on 1/28/2017.
 */

public class LoginNewResponse {


    /**
     * status : 1
     * responseJson : {"rider_query_result":[{"rider_id":"1","vehicle_type_id":"1","email":"rider@gmail.com","password":"e10adc3949ba59abbe56e057f20f883e","first_name":"Demo","last_name":"       Demo Rider","mobile":"9033110270","vehicle_name":"splender","vehicle_number":"0654","dockier_type_id":"1","rider_image":"2.jpg","licence_image":" ","registration_date_time":"2015-11-01 00:40:34","address_line_1":"Samarpan Appt.","area_name":"asadas","city_name":"","state":"gujarat","country":"india","pin":"380055","lat_long":"","place_id":null,"licence_id":"adadsad","is_deleted":"n","is_validate_user":"y","is_approved":"y","forgot_password":"","area":null,"city":"5","admin_id":"1","gcm_id":"-"}],"rider_usertype":3,"user_query_result":[{"user_id":"1","email":"user@gmail.com","password":"e10adc3949ba59abbe56e057f20f883e","first_name":"Demo","last_name":"","mobile":"9033110270","user_image":"","registration_date_time":"2015-11-01 00:38:13","is_validate_user":"y","forgot_password":"","is_deleted":"n","gcm_id":"-","facebook_id":"''","google_id":"''","current_balance":"0"}],"user_usertype":1}
     * responseMessage : Successfully login!
     */

    @SerializedName("status")
    private int status;
    @SerializedName("responseJson")
    private ResponseJsonBean responseJson;
    @SerializedName("responseMessage")
    private String responseMessage;

    public static LoginNewResponse objectFromData(String str) {

        return new Gson().fromJson(str, LoginNewResponse.class);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ResponseJsonBean getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(ResponseJsonBean responseJson) {
        this.responseJson = responseJson;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public static class ResponseJsonBean {
        /**
         * rider_query_result : [{"rider_id":"1","vehicle_type_id":"1","email":"rider@gmail.com","password":"e10adc3949ba59abbe56e057f20f883e","first_name":"Demo","last_name":"       Demo Rider","mobile":"9033110270","vehicle_name":"splender","vehicle_number":"0654","dockier_type_id":"1","rider_image":"2.jpg","licence_image":" ","registration_date_time":"2015-11-01 00:40:34","address_line_1":"Samarpan Appt.","area_name":"asadas","city_name":"","state":"gujarat","country":"india","pin":"380055","lat_long":"","place_id":null,"licence_id":"adadsad","is_deleted":"n","is_validate_user":"y","is_approved":"y","forgot_password":"","area":null,"city":"5","admin_id":"1","gcm_id":"-"}]
         * rider_usertype : 3
         * user_query_result : [{"user_id":"1","email":"user@gmail.com","password":"e10adc3949ba59abbe56e057f20f883e","first_name":"Demo","last_name":"","mobile":"9033110270","user_image":"","registration_date_time":"2015-11-01 00:38:13","is_validate_user":"y","forgot_password":"","is_deleted":"n","gcm_id":"-","facebook_id":"''","google_id":"''","current_balance":"0"}]
         * user_usertype : 1
         */

        @SerializedName("rider_usertype")
        private int riderUsertype;
        @SerializedName("user_usertype")
        private int userUsertype;
        @SerializedName("rider_query_result")
        private List<RiderQueryResultBean> riderQueryResult;
        @SerializedName("user_query_result")
        private List<UserQueryResultBean> userQueryResult;

        public static ResponseJsonBean objectFromData(String str) {

            return new Gson().fromJson(str, ResponseJsonBean.class);
        }

        public int getRiderUsertype() {
            return riderUsertype;
        }

        public void setRiderUsertype(int riderUsertype) {
            this.riderUsertype = riderUsertype;
        }

        public int getUserUsertype() {
            return userUsertype;
        }

        public void setUserUsertype(int userUsertype) {
            this.userUsertype = userUsertype;
        }

        public List<RiderQueryResultBean> getRiderQueryResult() {
            return riderQueryResult;
        }

        public void setRiderQueryResult(List<RiderQueryResultBean> riderQueryResult) {
            this.riderQueryResult = riderQueryResult;
        }

        public List<UserQueryResultBean> getUserQueryResult() {
            return userQueryResult;
        }

        public void setUserQueryResult(List<UserQueryResultBean> userQueryResult) {
            this.userQueryResult = userQueryResult;
        }

        public static class RiderQueryResultBean {
            /**
             * rider_id : 1
             * vehicle_type_id : 1
             * email : rider@gmail.com
             * password : e10adc3949ba59abbe56e057f20f883e
             * first_name : Demo
             * last_name :        Demo Rider
             * mobile : 9033110270
             * vehicle_name : splender
             * vehicle_number : 0654
             * dockier_type_id : 1
             * rider_image : 2.jpg
             * licence_image :
             * registration_date_time : 2015-11-01 00:40:34
             * address_line_1 : Samarpan Appt.
             * area_name : asadas
             * city_name :
             * state : gujarat
             * country : india
             * pin : 380055
             * lat_long :
             * place_id : null
             * licence_id : adadsad
             * is_deleted : n
             * is_validate_user : y
             * is_approved : y
             * forgot_password :
             * area : null
             * city : 5
             * admin_id : 1
             * gcm_id : -
             */

            @SerializedName("rider_id")
            private String riderId;
            @SerializedName("vehicle_type_id")
            private String vehicleTypeId;
            @SerializedName("email")
            private String email;
            @SerializedName("password")
            private String password;
            @SerializedName("first_name")
            private String firstName;
            @SerializedName("last_name")
            private String lastName;
            @SerializedName("mobile")
            private String mobile;
            @SerializedName("vehicle_name")
            private String vehicleName;
            @SerializedName("vehicle_number")
            private String vehicleNumber;
            @SerializedName("dockier_type_id")
            private String dockierTypeId;
            @SerializedName("rider_image")
            private String riderImage;
            @SerializedName("licence_image")
            private String licenceImage;
            @SerializedName("registration_date_time")
            private String registrationDateTime;
            @SerializedName("address_line_1")
            private String addressLine1;
            @SerializedName("area_name")
            private String areaName;
            @SerializedName("city_name")
            private String cityName;
            @SerializedName("state")
            private String state;
            @SerializedName("country")
            private String country;
            @SerializedName("pin")
            private String pin;
            @SerializedName("lat_long")
            private String latLong;
            @SerializedName("place_id")
            private Object placeId;
            @SerializedName("licence_id")
            private String licenceId;
            @SerializedName("is_deleted")
            private String isDeleted;
            @SerializedName("is_validate_user")
            private String isValidateUser;
            @SerializedName("is_approved")
            private String isApproved;
            @SerializedName("forgot_password")
            private String forgotPassword;
            @SerializedName("area")
            private Object area;
            @SerializedName("city")
            private String city;
            @SerializedName("admin_id")
            private String adminId;
            @SerializedName("gcm_id")
            private String gcmId;

            public static RiderQueryResultBean objectFromData(String str) {

                return new Gson().fromJson(str, RiderQueryResultBean.class);
            }

            public String getRiderId() {
                return riderId;
            }

            public void setRiderId(String riderId) {
                this.riderId = riderId;
            }

            public String getVehicleTypeId() {
                return vehicleTypeId;
            }

            public void setVehicleTypeId(String vehicleTypeId) {
                this.vehicleTypeId = vehicleTypeId;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getFirstName() {
                return firstName;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public String getLastName() {
                return lastName;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getVehicleName() {
                return vehicleName;
            }

            public void setVehicleName(String vehicleName) {
                this.vehicleName = vehicleName;
            }

            public String getVehicleNumber() {
                return vehicleNumber;
            }

            public void setVehicleNumber(String vehicleNumber) {
                this.vehicleNumber = vehicleNumber;
            }

            public String getDockierTypeId() {
                return dockierTypeId;
            }

            public void setDockierTypeId(String dockierTypeId) {
                this.dockierTypeId = dockierTypeId;
            }

            public String getRiderImage() {
                return riderImage;
            }

            public void setRiderImage(String riderImage) {
                this.riderImage = riderImage;
            }

            public String getLicenceImage() {
                return licenceImage;
            }

            public void setLicenceImage(String licenceImage) {
                this.licenceImage = licenceImage;
            }

            public String getRegistrationDateTime() {
                return registrationDateTime;
            }

            public void setRegistrationDateTime(String registrationDateTime) {
                this.registrationDateTime = registrationDateTime;
            }

            public String getAddressLine1() {
                return addressLine1;
            }

            public void setAddressLine1(String addressLine1) {
                this.addressLine1 = addressLine1;
            }

            public String getAreaName() {
                return areaName;
            }

            public void setAreaName(String areaName) {
                this.areaName = areaName;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getPin() {
                return pin;
            }

            public void setPin(String pin) {
                this.pin = pin;
            }

            public String getLatLong() {
                return latLong;
            }

            public void setLatLong(String latLong) {
                this.latLong = latLong;
            }

            public Object getPlaceId() {
                return placeId;
            }

            public void setPlaceId(Object placeId) {
                this.placeId = placeId;
            }

            public String getLicenceId() {
                return licenceId;
            }

            public void setLicenceId(String licenceId) {
                this.licenceId = licenceId;
            }

            public String getIsDeleted() {
                return isDeleted;
            }

            public void setIsDeleted(String isDeleted) {
                this.isDeleted = isDeleted;
            }

            public String getIsValidateUser() {
                return isValidateUser;
            }

            public void setIsValidateUser(String isValidateUser) {
                this.isValidateUser = isValidateUser;
            }

            public String getIsApproved() {
                return isApproved;
            }

            public void setIsApproved(String isApproved) {
                this.isApproved = isApproved;
            }

            public String getForgotPassword() {
                return forgotPassword;
            }

            public void setForgotPassword(String forgotPassword) {
                this.forgotPassword = forgotPassword;
            }

            public Object getArea() {
                return area;
            }

            public void setArea(Object area) {
                this.area = area;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getAdminId() {
                return adminId;
            }

            public void setAdminId(String adminId) {
                this.adminId = adminId;
            }

            public String getGcmId() {
                return gcmId;
            }

            public void setGcmId(String gcmId) {
                this.gcmId = gcmId;
            }
        }

        public static class UserQueryResultBean {
            /**
             * user_id : 1
             * email : user@gmail.com
             * password : e10adc3949ba59abbe56e057f20f883e
             * first_name : Demo
             * last_name :
             * mobile : 9033110270
             * user_image :
             * registration_date_time : 2015-11-01 00:38:13
             * is_validate_user : y
             * forgot_password :
             * is_deleted : n
             * gcm_id : -
             * facebook_id : ''
             * google_id : ''
             * current_balance : 0
             */

            @SerializedName("user_id")
            private String userId;
            @SerializedName("email")
            private String email;
            @SerializedName("password")
            private String password;
            @SerializedName("first_name")
            private String firstName;
            @SerializedName("last_name")
            private String lastName;
            @SerializedName("mobile")
            private String mobile;
            @SerializedName("user_image")
            private String userImage;
            @SerializedName("registration_date_time")
            private String registrationDateTime;
            @SerializedName("is_validate_user")
            private String isValidateUser;
            @SerializedName("forgot_password")
            private String forgotPassword;
            @SerializedName("is_deleted")
            private String isDeleted;
            @SerializedName("gcm_id")
            private String gcmId;
            @SerializedName("facebook_id")
            private String facebookId;
            @SerializedName("google_id")
            private String googleId;
            @SerializedName("current_balance")
            private String currentBalance;

            public static UserQueryResultBean objectFromData(String str) {

                return new Gson().fromJson(str, UserQueryResultBean.class);
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getFirstName() {
                return firstName;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public String getLastName() {
                return lastName;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getUserImage() {
                return userImage;
            }

            public void setUserImage(String userImage) {
                this.userImage = userImage;
            }

            public String getRegistrationDateTime() {
                return registrationDateTime;
            }

            public void setRegistrationDateTime(String registrationDateTime) {
                this.registrationDateTime = registrationDateTime;
            }

            public String getIsValidateUser() {
                return isValidateUser;
            }

            public void setIsValidateUser(String isValidateUser) {
                this.isValidateUser = isValidateUser;
            }

            public String getForgotPassword() {
                return forgotPassword;
            }

            public void setForgotPassword(String forgotPassword) {
                this.forgotPassword = forgotPassword;
            }

            public String getIsDeleted() {
                return isDeleted;
            }

            public void setIsDeleted(String isDeleted) {
                this.isDeleted = isDeleted;
            }

            public String getGcmId() {
                return gcmId;
            }

            public void setGcmId(String gcmId) {
                this.gcmId = gcmId;
            }

            public String getFacebookId() {
                return facebookId;
            }

            public void setFacebookId(String facebookId) {
                this.facebookId = facebookId;
            }

            public String getGoogleId() {
                return googleId;
            }

            public void setGoogleId(String googleId) {
                this.googleId = googleId;
            }

            public String getCurrentBalance() {
                return currentBalance;
            }

            public void setCurrentBalance(String currentBalance) {
                this.currentBalance = currentBalance;
            }
        }
    }
}

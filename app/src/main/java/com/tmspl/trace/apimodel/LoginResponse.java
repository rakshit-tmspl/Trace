package com.tmspl.trace.apimodel;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rakshit.sathwara on 1/18/2017.
 */

public class LoginResponse {

    /**
     * status : 1
     * responseJson : {"query_result":[{"user_id":"1","email":"user@gmail.com","password":"e10adc3949ba59abbe56e057f20f883e","first_name":"Demo","last_name":"","mobile":"7041204430","user_image":"","registration_date_time":"2015-11-01 00:38:13","is_validate_user":"y","forgot_password":"","is_deleted":"n","gcm_id":"APA91bG8jUcuAXcoUUzJ_3r7R5f4U3_nO_tHKbVZTSZkxVxL_kcwvZurVWVMLmtEUN8ngmRM0hsWAYh7mI46HcXgs2cvzj2x9EWdzooO9jW30OLRjnRwNKaQjCzH5YocTLkkX1Gm8Nwh","facebook_id":"''","google_id":"''","current_balance":"0"}],"usertype":1}
     * responseMessage : Successfully login!
     */

    @SerializedName("status")
    private int status;
    @SerializedName("responseJson")
    private ResponseJsonBean responseJson;
    @SerializedName("responseMessage")
    private String responseMessage;

    public static LoginResponse objectFromData(String str) {

        return new Gson().fromJson(str, LoginResponse.class);
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
         * query_result : [{"user_id":"1","email":"user@gmail.com","password":"e10adc3949ba59abbe56e057f20f883e","first_name":"Demo","last_name":"","mobile":"7041204430","user_image":"","registration_date_time":"2015-11-01 00:38:13","is_validate_user":"y","forgot_password":"","is_deleted":"n","gcm_id":"APA91bG8jUcuAXcoUUzJ_3r7R5f4U3_nO_tHKbVZTSZkxVxL_kcwvZurVWVMLmtEUN8ngmRM0hsWAYh7mI46HcXgs2cvzj2x9EWdzooO9jW30OLRjnRwNKaQjCzH5YocTLkkX1Gm8Nwh","facebook_id":"''","google_id":"''","current_balance":"0"}]
         * usertype : 1
         */

        @SerializedName("usertype")
        private int usertype;
        @SerializedName("query_result")
        private List<QueryResultBean> queryResult;

        public static ResponseJsonBean objectFromData(String str) {

            return new Gson().fromJson(str, ResponseJsonBean.class);
        }

        public int getUsertype() {
            return usertype;
        }

        public void setUsertype(int usertype) {
            this.usertype = usertype;
        }

        public List<QueryResultBean> getQueryResult() {
            return queryResult;
        }

        public void setQueryResult(List<QueryResultBean> queryResult) {
            this.queryResult = queryResult;
        }

        public static class QueryResultBean {
            /**
             * user_id : 1
             * email : user@gmail.com
             * password : e10adc3949ba59abbe56e057f20f883e
             * first_name : Demo
             * last_name :
             * mobile : 7041204430
             * user_image :
             * registration_date_time : 2015-11-01 00:38:13
             * is_validate_user : y
             * forgot_password :
             * is_deleted : n
             * gcm_id : APA91bG8jUcuAXcoUUzJ_3r7R5f4U3_nO_tHKbVZTSZkxVxL_kcwvZurVWVMLmtEUN8ngmRM0hsWAYh7mI46HcXgs2cvzj2x9EWdzooO9jW30OLRjnRwNKaQjCzH5YocTLkkX1Gm8Nwh
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

            public static QueryResultBean objectFromData(String str) {

                return new Gson().fromJson(str, QueryResultBean.class);
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

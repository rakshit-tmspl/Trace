package com.tmspl.trace.extra;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;
import org.json.JSONTokener;

public class HttpHelper {
    HttpManager httpCall;
    private Context mContext;

    public HttpHelper(Context context) {

        mContext = context;
        httpCall = new HttpManager();
    }

    public JSONObject Login(String body) throws Exception {
        JSONObject SessionDetails = null;
        try {
            String fileName = "login.php";
            String response = httpCall.ConnectToStudyPostServer(fileName, body);

            Log.e("registration Response :", "" + response);

            SessionDetails = (JSONObject) new JSONTokener(response.trim()
                    .toString()).nextValue();

            Log.e("JSONArray : ", "" + SessionDetails.toString());
        } catch (Exception ex) {
            throw ex;
        }

        return SessionDetails;
    }

    public JSONObject UserRegistration(String body) throws Exception {
        JSONObject SessionDetails = null;
        try {
            String fileName = "add_user_data";
            String response = httpCall.ConnectToStudyPostServer(fileName, body);

            Log.e("registration Response :", "" + response);

            SessionDetails = (JSONObject) new JSONTokener(response.trim()
                    .toString()).nextValue();

            Log.e("JSONArray : ", "" + SessionDetails.toString());
        } catch (Exception ex) {
            throw ex;
        }

        return SessionDetails;
    }

    public JSONObject RiderRegistration(String body) throws Exception {
        JSONObject SessionDetails = null;
        try {
            String fileName = "add_rider_data";
            String response = httpCall.ConnectToStudyPostServer(fileName, body);

            Log.e("registration Response :", "" + response);

            SessionDetails = (JSONObject) new JSONTokener(response.trim()
                    .toString()).nextValue();

            Log.e("JSONArray : ", "" + SessionDetails.toString());
        } catch (Exception ex) {
            throw ex;
        }

        return SessionDetails;
    }

    public JSONObject DepotRegistration(String body) throws Exception {
        JSONObject SessionDetails = null;
        try {
            String fileName = "add_depot_data";
            String response = httpCall.ConnectToStudyPostServer(fileName, body);

            Log.e("registration Response :", "" + response);

            SessionDetails = (JSONObject) new JSONTokener(response.trim()
                    .toString()).nextValue();

            Log.e("JSONArray : ", "" + SessionDetails.toString());
        } catch (Exception ex) {
            throw ex;
        }

        return SessionDetails;
    }


    public JSONObject ForgotPassword(String body) throws Exception {
        JSONObject SessionDetails = null;
        try {
            String fileName = "forgot_password.php";
            String response = httpCall.ConnectToStudyPostServer(fileName, body);

            Log.e("registration Response :", "" + response);

            SessionDetails = (JSONObject) new JSONTokener(response.trim()
                    .toString()).nextValue();

            Log.e("JSONArray : ", "" + SessionDetails.toString());
        } catch (Exception ex) {
            throw ex;
        }
        return SessionDetails;
    }

}

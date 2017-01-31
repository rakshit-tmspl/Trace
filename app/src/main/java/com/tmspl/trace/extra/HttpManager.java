package com.tmspl.trace.extra;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;

public class HttpManager {

    public String connectToConfference(String MethodName, String JSON_body)
            throws Exception {
        String strResponse = "";
        try {
            URI url = new URI(Constants.API_BASE_URL + MethodName);

            Log.e("Opening URL", "" + url.toString());

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            httppost.setHeader("Content-Type",
                    "application/x-www-form-urlencoded;");
            // httppost.setHeader(“Content-Length”, “0″);

            try {
                // Add your data
                if (!JSON_body.equalsIgnoreCase("")) {
                    StringEntity se = new StringEntity(JSON_body);
                    // se.setContentEncoding("UTF-8");
                    se.setContentType("application/x-www-form-urlencoded");
                    httppost.setEntity(se);
                }

                // Execute HTTP Post Request
                // strResponse = httpclient.execute(httppost, responseHandler);
                HttpResponse htpp_strResponse = httpclient.execute(httppost);
                strResponse = EntityUtils
                        .toString(htpp_strResponse.getEntity());

                // Log.e("Responce", "–>" + strResponse);

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return strResponse;
    }

    public String ConnectToStudyPostServer(String MethodName, String JSON_body)
            throws Exception {
        String strResponse = "";
        try {
            URI url = new URI(Constants.API_BASE_URL + MethodName);

            Log.e("Opening URL", "" + url.toString());

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            httppost.setHeader("Content-Type", "application/json;charset=utf-8");
            // httppost.setHeader(“Content-Length”, “0″);

            try {
                // Add your data
                if (!JSON_body.equalsIgnoreCase("")) {
                    StringEntity se = new StringEntity(JSON_body);
                    se.setContentEncoding("UTF-8");
                    se.setContentType("application/json");
                    httppost.setEntity(se);
                }

                // Execute HTTP Post Request
                // strResponse = httpclient.execute(httppost, responseHandler);
                HttpResponse htpp_strResponse = httpclient.execute(httppost);
                strResponse = EntityUtils
                        .toString(htpp_strResponse.getEntity());

                // Log.e("Responce", "–>" + strResponse);

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return strResponse;
    }

    public String connectToConfference_Get(String MethodName, String JSON_body)
            throws Exception {
        String strResponse = "";
        try {

            URI url = new URI(Constants.API_BASE_URL + MethodName + JSON_body);

            Log.e("Opening URL", "" + url.toString());

            HttpClient httpclient = new DefaultHttpClient();

            HttpGet httppost = new HttpGet(url);
            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is
            // established.
            // The default value is zero, that means the timeout is not used.
            int timeoutConnection = 3000;
            HttpConnectionParams.setConnectionTimeout(httpParameters,
                    timeoutConnection);
            // Set the default socket timeout (SO_TIMEOUT)
            // in milliseconds which is the timeout for waiting for data.
            int timeoutSocket = 5000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            httppost.setHeader("Content-Type",
                    "application/json; charset=utf-8");
            try {
                // Add your data
                if (!JSON_body.equalsIgnoreCase("")) {
                    StringEntity se = new StringEntity(JSON_body);
                    se.setContentEncoding("UTF-8");
                    se.setContentType("application/json");
                }

                HttpResponse htpp_strResponse = httpclient.execute(httppost);
                strResponse = EntityUtils
                        .toString(htpp_strResponse.getEntity());

            } catch (ConnectTimeoutException e) {
                e.printStackTrace();
                Log.i("==== Connection Timeout", "===");
                // Close the Dialog
                throw e;
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return strResponse;
    }

}

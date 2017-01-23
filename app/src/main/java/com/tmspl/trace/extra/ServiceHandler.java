package com.tmspl.trace.extra;

/**
 * Created by rakshit.sathwara on 1/23/2017.
 */

import android.annotation.SuppressLint;
import android.os.StrictMode;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.util.List;

public class ServiceHandler {

    public final static int GET = 1;
    public final static int POST = 2;
    static String response = null;

    public ServiceHandler() {

    }

    /*
     * Making service call
     *
     * @url - url to make request
     *
     * @method - http request method
     */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    /*
     * Making service call
     *
     * @url - url to make request
     *
     * @method - http request method
     *
     * @params - http request params
     */
    @SuppressLint("NewApi")
    public String makeServiceCall(String url, int method,
                                  List<NameValuePair> params) {
        //  Log.e("URL", "->" + url);

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            params.add(new BasicNameValuePair("auth", "4JW*BNtp2nX6AbJCAoksWi/1DHoJJGYw"));
            for (NameValuePair property : params) {
                //  Log.e(property.getName(), "->" + property.getValue());
            }
            //Log.e("ServiceHandler", "Service Call");
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 600000);
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }

                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += paramString;
                }
                HttpGet httpGet = new HttpGet(url);

                httpResponse = httpClient.execute(httpGet);

            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
            // Log.e("ServiceHandler", "Response->" + response);
        } catch (Exception e) {
            //  Log.e("Service Handleer", "Error : " + e.toString());
            return "{\"error\":\"No Data Found!\"}";
        }

        return response;

    }
}

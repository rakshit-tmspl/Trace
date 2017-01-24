package com.tmspl.trace.extra;

import android.graphics.Bitmap;

import com.tmspl.trace.apimodel.Order_Detail_bean;

import java.util.ArrayList;

/**
 * Created by rakshit.sathwara on 1/18/2017.
 */

public class Constants {
    public static final String API_BASE_URL = "http://demo.tmsys.co.in:9092/dockier/api_mobile/";
    public static final String AUTH = "4JW*BNtp2nX6AbJCAoksWi/1DHoJJGYw";
    public static String FCM_INSTANCE_ID;
    public static String MATERIAL_TYPE_ID;
    public static String ADDRESS_FLAG;

    public static final String PACKAGE_NAME =
            "com.rutvik.trails";
    public static final String FETCH_TYPE_EXTRA = PACKAGE_NAME + ".FETCH_TYPE_EXTRA";
    public static final String LOCATION_LATITUDE_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_LATITUDE_DATA_EXTRA";
    public static final String LOCATION_LONGITUDE_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_LONGITUDE_DATA_EXTRA";
    public static final String LOCATION_NAME_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_NAME_DATA_EXTRA";
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;

    public static final int USE_ADDRESS_NAME = 1;
    public static final int USE_ADDRESS_LOCATION = 2;

    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String RESULT_ADDRESS = PACKAGE_NAME + ".RESULT_ADDRESS";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";


    public static int isSignCom = 0;
    public static int isDash = 0;
    public static int pSuc = 0;

    public static String order_id;
    public static String address_flag;
    public static String material_type_id;
    public static String promocode;
    public static Float promocode_disc = Float.valueOf(0);
    public static int toCount = 0, payment_method = 0;


    public static Order_Detail_bean order_from_address = new Order_Detail_bean();
    public static ArrayList<Order_Detail_bean> order_to_address = new ArrayList<Order_Detail_bean>();

    public static float basic_cost = 0, pkm_cost = 0;

    public static String tempUser = "";

    public static int flgDelivery = 0;

    public static String name, email, mobile, password;
    public static Bitmap image;


    public static Bitmap parcelBitmap = null;

    public static int camFlg = 0;

    public static int camSender = 0;

    public static int isLogin = 0;

    public static int ut = 0;

    public static String rfitst_name = "", remail = "", rmobile = "", rpassword = "", rcategory = "", rphoto = "";

    public static String data_city = "";

    public static int isCompleted = 0;
}

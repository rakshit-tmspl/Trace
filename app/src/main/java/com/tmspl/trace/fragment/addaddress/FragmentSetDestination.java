package com.tmspl.trace.fragment.addaddress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tmspl.trace.R;
import com.tmspl.trace.activity.AcceptedDeliveriesActivity;
import com.tmspl.trace.apimodel.Order_Detail_bean;
import com.tmspl.trace.extra.Alert;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.Preferences;
import com.tmspl.trace.extra.ServiceHandler;
import com.tmspl.trace.extra.Utility;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;

import static com.tmspl.trace.extra.Constants.order_from_address;
import static com.tmspl.trace.extra.Constants.order_to_address;

/**
 * Created by rakshit.sathwara on 1/23/2017.
 */

public class FragmentSetDestination extends Fragment implements View.OnClickListener {


    public static int clickFlg = 0;

    View view;
    Button btn_confirm, btnAdd_first, btnAdd_second;
    public static TextView txt_from_name, txt_to_name_1, txt_to_name_2, txt_to_name_3, txt_estimate;
    public static TextView txt_from_address, txt_to_address_1, txt_to_address_2, txt_to_address_3;
    LinearLayout ll_from, ll_to_1, ll_secondAddress, ll_thirdAddress;
    public static Activity context;
    public static Float dkm;
    public static Float cost1 = Float.valueOf(0), cost2 = Float.valueOf(0), cost3 = Float.valueOf(0);

    public static EditText edt_promo_code, edt_pickup_time;
    public static Button btn_apply_promo_code;

    public static String duration;
    public static String paymentBy;

    AppCompatRadioButton rbPaymentByYou, rbPaymentByReceiver;
    RadioGroup rgPaymentBy;
    public static String timeDuration;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_set_destination, container, false);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Set Address");
//        order_to_address.add(new Order_Detail_bean());
//        order_to_address.add(new Order_Detail_bean());
        //  Constants.order_to_address.add(new Order_Detail_bean());


        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        resetAll();

                        return false;
                    }
                }
                return false;
            }
        });


        init();
        initAction();

        new getCost(getActivity()).execute();

        return view;
    }

    public void init() {

        txt_estimate = (TextView) view.findViewById(R.id.txt_estimate);
        edt_pickup_time = (EditText) view.findViewById(R.id.edt_pickup_time);

        ll_from = (LinearLayout) view.findViewById(R.id.linear_current_location);
        ll_to_1 = (LinearLayout) view.findViewById(R.id.linear_destination_location);


        txt_from_name = (TextView) view.findViewById(R.id.tv_sender_name);
        txt_to_name_1 = (TextView) view.findViewById(R.id.tv_receiver_name);

        txt_from_address = (TextView) view.findViewById(R.id.tv_sender_address);
        txt_to_address_1 = (TextView) view.findViewById(R.id.tv_receiver_address);

        rgPaymentBy = (RadioGroup) view.findViewById(R.id.rg_payment_by);
        rbPaymentByYou = (AppCompatRadioButton) view.findViewById(R.id.rb_payment_you);
        rbPaymentByReceiver = (AppCompatRadioButton) view.findViewById(R.id.rb_payment_receiver);


//        edt_promo_code = (EditText) view.findViewById(R.id.edt_promo_code);
//        btn_apply_promo_code = (Button) view.findViewById(R.id.btn_promo_apply);


        //btnAdd_first = (Button) rootView.findViewById(R.id.btnAdd_first);
        btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
    }

    private void initAction() {

        ll_from.setOnClickListener(this);
        ll_to_1.setOnClickListener(this);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((cost1) > 0) {
                    timeDuration = edt_pickup_time.getText().toString();
                    payment_option_dialog();
//                    PaymentDialog paymentDialog = new PaymentDialog(getActivity());
//                    paymentDialog.show();
                }
            }
        });

        rgPaymentBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_payment_you) {
                    paymentBy = "s";

                } else {
                    paymentBy = "r";
                }
            }
        });

//        btnAdd_first.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                ll_secondAddress.setVisibility(View.VISIBLE);
//                btnAdd_first.setVisibility(View.GONE);
//                btnAdd_second.setVisibility(View.VISIBLE);
//            }
//        });


        /*btn_apply_promo_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new validate_promo_code(getActivity()).execute();
            }
        });*/

        edt_pickup_time.setFocusable(false);
        edt_pickup_time.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        edt_pickup_time.setClickable(false);

        edt_pickup_time.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    Calendar mcurrentTime = Calendar.getInstance();
                    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    final int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {


                            if (selectedHour < hour || (selectedHour == hour && selectedMinute < minute)) {
                                Alert.ShowAlert(getActivity(), "Pickup time has already been passed!\nPlease select proper time.");
                            }
                            /*else if(selectedHour<10 || selectedHour>18 || (selectedHour==18 && selectedMinute>0)){
                                Alert.ShowAlert(getActivity(), "Please select time between working hours(10 am - 6 pm)!");
                            }*/
                            else {
                                edt_pickup_time.setText(selectedHour + ":" + selectedMinute);
                            }
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
                return false;
            }
        });
    }

    public void payment_option_dialog() {
        final CharSequence[] items = {"Paytm", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Payment Option!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    Constants.payment_method = 1;
                    new PlaceOrder(getActivity()).execute();
                } else if (item == 1) {
                    dialog.dismiss();
                    //getActivity().startActivity(new Intent(getActivity(), PayumoneyActivity.class));
                }
            }
        });
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constants.pSuc == 1) {
            new PlaceOrder(getActivity()).execute();

        } else {
            if (clickFlg == 1) {
                clickFlg = 0;
            }

            if (Constants.promocode_disc > 0) {
                //edt_promo_code.setEnabled(false);
            }

            if (order_from_address.getAddress_id() != null) {
                if (order_from_address.getAddress_id().equals("0")) {
                    txt_from_name.setText(order_from_address.getName());
                    txt_from_address.setText(order_from_address.getAddress_line_1() + ", " + order_from_address.getAddress_line_2() + ", " + order_from_address.getArea_value() + ", " + order_from_address.getCity_value() + ", " + order_from_address.getPin());
                } else {
                    txt_from_name.setText(order_from_address.getName());
                    txt_from_address.setText(order_from_address.getAddress_line_1());
                }
            }
            if (order_to_address.getAddress_id() != null) {
                if (order_to_address.getAddress_id().equals("0")) {
                    txt_to_name_1.setText(order_to_address.getName());
                    txt_to_address_1.setText(order_to_address.getAddress_line_1() + ", " + order_to_address.getAddress_line_2() + ", " + order_to_address.getArea_value() + ", " + order_to_address.getCity_value() + ", " + order_to_address.getPin());
                } else {
                    txt_to_name_1.setText(order_to_address.getName());
                    txt_to_address_1.setText(order_to_address.getAddress_line_1());
                }
            }
       /*     if (Constants.order_to_address.get(1).getAddress_id() != null) {
                ll_secondAddress.setVisibility(View.VISIBLE);
                btnAdd_first.setVisibility(View.GONE);
                btnAdd_second.setVisibility(View.VISIBLE);
                if (Constants.order_to_address.get(1).getAddress_id().equals("0")) {

                    txt_to_name_2.setText(Constants.order_to_address.get(1).getName());
                    txt_to_address_2.setText(Constants.order_to_address.get(1).getAddress_line_1() + ", " + Constants.order_to_address.get(1).getAddress_line_2() + ", " + Constants.order_to_address.get(1).getArea_value() + ", " + Constants.order_to_address.get(1).getCity_value() + ", " + Constants.order_to_address.get(1).getPin());
                } else {
                    txt_to_name_2.setText(Constants.order_to_address.get(1).getName());
                    txt_to_address_2.setText(Constants.order_to_address.get(1).getAddress_line_1());
                }
            }*/
//            if (Constants.order_to_address.get(2).getAddress_id() != null) {
//                ll_thirdAddress.setVisibility(View.VISIBLE);
//                btnAdd_second.setVisibility(View.GONE);
//
//                if (Constants.order_to_address.get(2).getAddress_id().equals("0")) {
//                    txt_to_name_3.setText(Constants.order_to_address.get(2).getName());
//                    txt_to_address_3.setText(Constants.order_to_address.get(2).getAddress_line_1() + ", " + Constants.order_to_address.get(2).getAddress_line_2() + ", " + Constants.order_to_address.get(2).getArea_value() + ", " + Constants.order_to_address.get(2).getCity_value() + ", " + Constants.order_to_address.get(2).getPin());
//                } else {
//                    txt_to_name_3.setText(Constants.order_to_address.get(2).getName());
//                    txt_to_address_3.setText(Constants.order_to_address.get(2).getAddress_line_1());
//                }
//            }
            calculate();
        }

    }

    public void calculate() {

        if (order_from_address.getAddress_id() != null && order_to_address.getAddress_id() != null) {
            dkm = Float.valueOf(0);
            new getClassDistance(getActivity(), order_from_address.getLat_long(), order_to_address.getLat_long(), 0).execute();

        }
       /* if (order_to_address.getAddress_id() != null && order_to_address.getAddress_id() != null) {
            dkm = Float.valueOf(0);
            new getClassDistance(getActivity(), order_to_address.getLat_long(), order_to_address.getLat_long(), 0).execute();

        }*/
       /* if (Constants.order_to_address.get(1).getAddress_id() != null && Constants.order_to_address.get(2).getAddress_id() != null) {
            dkm = Float.valueOf(0);
            new getClassDistance(getActivity(), Constants.order_to_address.get(1).getLat_long(), Constants.order_to_address.get(2).getLat_long(), 2).execute();
        }*/


    }

    public class getClassDistance extends AsyncTask<String, String, String> {

        Activity context;
        AlertDialog pd;
        String start, end;
        int cnt;

        public getClassDistance(Activity context, String start, String end, int cnt) {
            this.context = context;
            this.start = start;
            this.end = end;
            this.cnt = cnt;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context, "Estimating Price..");
            pd.show();
            pd.setCancelable(false);


        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {


                URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?alternatives=true&sensor=false&origin=" + start + "&destination=" + end);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream stream = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(stream));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String jsonResponse = sb.toString();
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Please Enter Addresses!\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Please Enter Addresses!\"}";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                //  Log.e("Response", result);
                JSONObject jobj1 = new JSONObject(result);

                if (jobj1.has("error")) {
                    Alert.ShowAlert(context, jobj1.getString("error"));
                } else {

                    JSONObject response = new JSONObject(result);
                    JSONArray routes = new JSONArray(response.getString("routes"));
                    //  Log.e("Routs", routes.toString());
                    JSONArray dis = new JSONArray(routes.getJSONObject(0).getString("legs"));
                    //  Log.e("Routs", dis.toString());
                    String Distance = dis.getJSONObject(0).getJSONObject("distance").getString("text").toString().split(" ")[0];
                    //  Log.e("Distance", "" + Distance);

                    duration = dis.getJSONObject(0).getJSONObject("duration").getString("text").toString();
                    Log.e("Duration", "onPostExecute: DURATION" + duration);
                    String ss = Distance.replace(",", "").trim();
                    dkm = Float.parseFloat(ss);

                    Float km = dkm;
                    Log.e("KM", "onPostExecute: " + km);
                    if (km <= 4) {

                        Log.e("KM < 4", "onPostExecute: " + "in");
                        order_to_address.setKm(km + "");
                        cost1 = (Constants.basic_cost);
                        Log.e("KM < 4", "onPostExecute: " + "in");
                        order_to_address.setIndividual_price(cost1 + "");
                        Log.e("KM < 4", "onPostExecute: " + "in");
                    } else {
                        Log.e("KM > 4", "onPostExecute: " + "in");
                        order_to_address.setKm(km + "");
//                                Log.e("BEFORE KM", "onPostExecute: " + km);
                        Float remainKm = km - 4;
//                                Log.e("KM", "onPostExecute: " + remainKm + "BASIC COST" + Constants.pkm_cost);
                        Float remainPrice = remainKm * (Constants.pkm_cost);
//                                Log.e("remainPrice", "onPostExecute: " + remainPrice);
                        cost1 = Constants.basic_cost + remainPrice;
                        Log.e("KM > 4", "onPostExecute: " + "in");
                        Log.e("COST", "onPostExecute: " + cost1);
                        Constants.order_to_address.setIndividual_price(cost1 + "");
                    }


                  /*  if (cnt == 1) {
                        if (Constants.order_to_address.get(0).getAddress_id() != null && Constants.order_to_address.get(1).getAddress_id() != null) {
                            Float km = dkm;
                            Constants.order_to_address.get(1).setKm(km + "");
                            cost2 = ((km * Constants.pkm_cost));
                            Constants.order_to_address.get(1).setIndividual_price(cost2 + "");
                        }
                    }
                    if (cnt == 2) {
                        if (Constants.order_to_address.get(1).getAddress_id() != null && Constants.order_to_address.get(2).getAddress_id() != null) {
                            Float km = dkm;
                            Constants.order_to_address.get(2).setKm(km + "");
                            cost3 = ((km * Constants.pkm_cost));
                            Constants.order_to_address.get(2).setIndividual_price(cost3 + "");
                        }
                    }*/
                   /* if (Constants.promocode_disc > 0) {
                        //  Log.e("Discount", Data.promocode_disc + "");
                        cost1 = cost1 - (cost1 * Constants.promocode_disc) / 100;
                        cost2 = cost2 - (cost2 * Constants.promocode_disc) / 100;
                        cost3 = cost3 - (cost3 * Constants.promocode_disc) / 100;

                        Constants.order_to_address.get(0).setIndividual_price(cost1 + "");
                        Constants.order_to_address.get(1).setIndividual_price(cost2 + "");
                        Constants.order_to_address.get(2).setIndividual_price(cost3 + "");
                    }*/
                    //   Log.e("Cost 1", cost1 + "");
                    //  Log.e("Cost 2", cost2 + "");
                    // Log.e("Cost 3", cost3 + "");

                    Preferences.savePreferences(getActivity(), "order_cost", cost1 + "");
                    txt_estimate.setText("Estimated Cost : " + cost1);
                    if (pd.isShowing())
                        pd.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.linear_current_location) {
            Constants.address_flag = "from";
        } else if (v.getId() == R.id.linear_destination_location) {
            Constants.toCount = 0;
            Constants.address_flag = "to";
        }

        Fragment fragmentS1 = new FragmentSetAddress();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentS1).addToBackStack(null).commit();

    }


    public class getCost extends AsyncTask<String, String, String> {

        Activity context;
        AlertDialog pd;

        public getCost(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context);
            pd.setCancelable(false);


        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                ServiceHandler sh = new ServiceHandler();

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL
                                + "getCost", ServiceHandler.POST,
                        nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Please Enter Addresses!\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Please Enter Addresses!\"}";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject jobj1 = new JSONObject(result);

                if (jobj1.has("error")) {
                    Alert.ShowAlert(context, jobj1.getString("error"));
                } else {
                    if (jobj1.getInt("status") == 1) {

                        String res = jobj1.getString("responseJson");
                        JSONArray jarray = new JSONArray(res);
                        Constants.basic_cost = Float.parseFloat(jarray.getJSONObject(0).getString("value"));
                        Constants.pkm_cost = Float.parseFloat(jarray.getJSONObject(1).getString("value"));

                    } else {
                        Alert.showAlertWithFinish(context,
                                jobj1.getString("responseMessage"));
                    }
                }

                if (pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class PlaceOrder extends AsyncTask<String, String, String> {

        Activity context;
        AlertDialog pd;

        public PlaceOrder(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context, "Please wait..");

            pd.show();
            pd.setCancelable(false);


        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                ServiceHandler sh = new ServiceHandler();

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                String Service = "";

                nameValuePairs.add(new BasicNameValuePair("time", timeDuration));
                nameValuePairs.add(new BasicNameValuePair("payment_by", paymentBy));
                nameValuePairs.add(new BasicNameValuePair("duration", duration));
                nameValuePairs.add(new BasicNameValuePair("image", Utility.encodeTobase64(Constants.parcelBitmap)));
                nameValuePairs.add(new BasicNameValuePair("individual_price", String.valueOf(cost1)));
                nameValuePairs.add(new BasicNameValuePair("payment_method", Constants.payment_method + ""));
                if (Preferences.getSavedPreferences(getActivity(), "usertype").equals("1")) {
                    Service = "add_order_user_data";
                    nameValuePairs.add(new BasicNameValuePair("user_id", Preferences.getSavedPreferences(getActivity(), "user_id")));

                } else {
                    Service = "add_order_depot_data";
                    nameValuePairs.add(new BasicNameValuePair("depot_id", Preferences.getSavedPreferences(getActivity(), "depot_id")));

                }
                nameValuePairs.add(new BasicNameValuePair("material_type_id", Constants.material_type_id));
                if (Constants.parcelBitmap != null)
                    nameValuePairs.add(new BasicNameValuePair("image", Utility.encodeTobase64(Constants.parcelBitmap)));
                if (Constants.promocode_disc > 0) {
                    //nameValuePairs.add(new BasicNameValuePair("promo_code", edt_promo_code.getText().toString());
                }
                if (Constants.payment_method == 2) {
                    //nameValuePairs.add(new BasicNameValuePair("payu_trans_id", PayumoneyActivity.payu_trans_id));
                }
                //start order details
                if (order_from_address.getAddress_id().equals("0")) {

                    if (Preferences.getSavedPreferences(getActivity(), "usertype").equals("1")) {
                        nameValuePairs.add(new BasicNameValuePair("user_from_add_id", "0"));
                    } else {
                        nameValuePairs.add(new BasicNameValuePair("depot_from_add_id", "0"));
                    }

                    nameValuePairs.add(new BasicNameValuePair("from_name", order_from_address.getName()));
                    nameValuePairs.add(new BasicNameValuePair("mobile", order_from_address.getMobile()));
                    nameValuePairs.add(new BasicNameValuePair("from_address_line_1", order_from_address.getAddress_line_1()));
                    nameValuePairs.add(new BasicNameValuePair("from_address_line_2", order_from_address.getAddress_line_2()));
                    nameValuePairs.add(new BasicNameValuePair("from_pin", order_from_address.getPin()));
                    nameValuePairs.add(new BasicNameValuePair("area", order_from_address.getArea_key()));
                    nameValuePairs.add(new BasicNameValuePair("city", order_from_address.getCity_key()));
                    nameValuePairs.add(new BasicNameValuePair("from_state", order_from_address.getState()));

                } else {
                    if (Preferences.getSavedPreferences(getActivity(), "usertype").equals("1")) {
                        nameValuePairs.add(new BasicNameValuePair("user_from_add_id", order_from_address.getAddress_id()));
                    } else {
                        nameValuePairs.add(new BasicNameValuePair("depot_from_add_id", order_from_address.getAddress_id()));
                    }

                }

                if (Preferences.getSavedPreferences(getActivity(), "usertype").equals("1")) {
                    Log.e("IN ONE", "doInBackground: " + "IN ONE");
                    if (order_to_address.getAddress_id().equals("0")) {
                        nameValuePairs.add(new BasicNameValuePair("user_to_address_id", "0"));
                        nameValuePairs.add(new BasicNameValuePair("to_name", order_to_address.getName()));
                        nameValuePairs.add(new BasicNameValuePair("to_address_line_1", order_to_address.getAddress_line_1()));
                        nameValuePairs.add(new BasicNameValuePair("pin", order_to_address.getPin()));
                        nameValuePairs.add(new BasicNameValuePair("to_area", order_to_address.getArea_key()));
                        nameValuePairs.add(new BasicNameValuePair("to_city", order_to_address.getCity_key()));
                        nameValuePairs.add(new BasicNameValuePair("to_state", order_to_address.getState()));
                        nameValuePairs.add(new BasicNameValuePair("to_mobile", order_to_address.getMobile()));
                    } else {
                        nameValuePairs.add(new BasicNameValuePair("user_to_address_id", order_to_address.getAddress_id()));
                    }

                    nameValuePairs.add(new BasicNameValuePair("km", order_to_address.getKm()));


                } else {

                    if (order_to_address.getAddress_id().equals("0")) {
                        nameValuePairs.add(new BasicNameValuePair("depot_to_address_id", "0"));
                        nameValuePairs.add(new BasicNameValuePair("to_name", order_to_address.getName()));
                        nameValuePairs.add(new BasicNameValuePair("to_address_line_1", order_to_address.getAddress_line_1()));
                        nameValuePairs.add(new BasicNameValuePair("pin", order_to_address.getPin()));
                        nameValuePairs.add(new BasicNameValuePair("to_area", order_to_address.getArea_key()));
                        nameValuePairs.add(new BasicNameValuePair("to_city", order_to_address.getCity_key()));
                        nameValuePairs.add(new BasicNameValuePair("to_state", order_to_address.getState()));
                        nameValuePairs.add(new BasicNameValuePair("to_mobile", order_to_address.getMobile()));
                    } else {
                        nameValuePairs.add(new BasicNameValuePair("depot_to_address_id", order_to_address.getAddress_id()));
                    }
                    nameValuePairs.add(new BasicNameValuePair("individual_price", order_to_address.getIndividual_price()));
                    nameValuePairs.add(new BasicNameValuePair("km", order_to_address.getKm()));
                }


                //end order dtl

                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL
                                + Service, ServiceHandler.POST,
                        nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Please Enter Addresses!\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Please Enter Addresses!\"}";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject jobj1 = new JSONObject(result);

                if (jobj1.has("error")) {
                    Alert.ShowAlert(context, jobj1.getString("error"));
                } else {
                    if (jobj1.getInt("status") == 1) {
                        if (Constants.pSuc == 1) {
                            Constants.pSuc = 0;
                        }
                        JSONObject responseJson = new JSONObject(jobj1.getString("responseJson"));

                        JSONObject order_data = new JSONObject(responseJson.getString("order_data"));

                        Constants.order_id = order_data.getString("order_id");

                        Alert.showAlertWithPause(getActivity(), "Your order has been placed successfully!");
                        Constants.promocode_disc = Float.valueOf(0);
                        edt_pickup_time.setText("");
                        resetAll();
                        dialog_searching();
                    } else {
                        Alert.showAlertWithFinish(context,
                                jobj1.getString("responseMessage"));
                    }
                }

                if (pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //
    public class validate_promo_code extends AsyncTask<String, String, String> {

        Activity context;
        AlertDialog pd;

        public validate_promo_code(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context, "Validating Promocode..");
            pd.show();
            pd.setCancelable(false);


        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                ServiceHandler sh = new ServiceHandler();

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                if (Preferences.getSavedPreferences(getActivity(), "usertype").equals("1")) {

                    nameValuePairs.add(new BasicNameValuePair("user_id", Preferences.getSavedPreferences(getActivity(), "user_id")));

                } else {

                    nameValuePairs.add(new BasicNameValuePair("depot_id", Preferences.getSavedPreferences(getActivity(), "depot_id")));
                }
                //nameValuePairs.add(new BasicNameValuePair("promo_code", edt_promo_code.getText().toString().toUpperCase()));


                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL
                                + "validate_promo_code", ServiceHandler.POST,
                        nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Promo code Validation Goes Failed Please try again later\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Promo code Validation Goes Failed Please try again later\"}";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject jobj1 = new JSONObject(result);

                if (jobj1.has("error")) {
                    Alert.ShowAlert(context, jobj1.getString("error"));
                } else {
                    if (jobj1.getInt("status") == 1) {
                        //    boolean apply=Alert.showYesNoWithExecutionStop("Hi, you got"+jobj1.getString("responseJson")+"% dicount do you want to apply it?",getActivity());
                        //  if(apply) {
                        Constants.promocode_disc = Float.parseFloat(jobj1.getString("responseJson"));
                        edt_promo_code.setEnabled(false);
                        calculate();
                        //   }
                    } else {
                        Alert.showAlertWithPause(context,
                                jobj1.getString("responseMessage"));
                    }
                }

                if (pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {
                if (pd.isShowing())
                    pd.dismiss();
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_set_location, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        resetAll();
        return false;
    }

    public void resetAll() {
        edt_pickup_time.setText("");
        order_from_address = new Order_Detail_bean();
        order_to_address = new Order_Detail_bean();
        Constants.parcelBitmap = null;
        Constants.data_city = "";
        Constants.payment_method = 0;
        cost1 = Float.valueOf(0);
        cost2 = Float.valueOf(0);
        cost3 = Float.valueOf(0);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    private void dialog_searching() {

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View DialogView = factory.inflate(
                R.layout.custom_layout_for_dialog_searching, null);
        ImageView rotate_IMAGE_TARGET = (ImageView) DialogView.findViewById(R.id.img1);
        final RotateAnimation rotate_ANIMATION = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //manage the speed
        rotate_ANIMATION.setDuration(2000);
        //manage repeat
        rotate_ANIMATION.setRepeatCount(Animation.INFINITE);
        rotate_ANIMATION.setInterpolator(new LinearInterpolator());
        rotate_IMAGE_TARGET.startAnimation(rotate_ANIMATION);
        Button btn_CANCEL = (Button) DialogView.findViewById(R.id.btn_Stop);

        final Dialog dialog_location = new Dialog(getActivity());
        dialog_location.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog_location.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog_location.getWindow().setAttributes(lp);
        dialog_location.setContentView(DialogView);
        dialog_location.show();

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                //call to my UI thread every one second
            }

            public void onFinish() {
                //final call to my UI thread after 90 seconds
                rotate_ANIMATION.cancel();
                rotate_ANIMATION.reset();
                dialog_location.dismiss();
                if (clickFlg == 0) {
                    startActivity(new Intent(getActivity(), AcceptedDeliveriesActivity.class));
                }
            }
        }.start();

        btn_CANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stop animation
                rotate_ANIMATION.cancel();
                rotate_ANIMATION.reset();
                dialog_location.dismiss();
                if (clickFlg == 0) {
                    clickFlg = 1;
                }
                startActivity(new Intent(getActivity(), AcceptedDeliveriesActivity.class));
            }
        });
    }
}

package com.tmspl.trace.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.tmspl.trace.R;
import com.tmspl.trace.activity.AcceptedDeliveriesActivity;
import com.tmspl.trace.extra.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tmspl.trace.fragment.addaddress.FragmentSetDestination.clickFlg;

/**
 * Created by rakshit.sathwara on 2/14/2017.
 */

public class PaymentDialog extends CustomDialog {
    @BindView(R.id.tv_payment)
    CustomTextView tvPayment;
    @BindView(R.id.iv_paytm)
    ImageView ivPaytm;
    @BindView(R.id.iv_dialog_cancel)
    ImageView ivDialogCancel;

    private Context context;

    public PaymentDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_dialog);
        ButterKnife.bind(this);
        //  context = getContext();

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);


        ivDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        ivPaytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new PlaceOrder(context).execute();
            }
        });


    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    @Override
    public void onClick(View view) {

    }

  /*  public class PlaceOrder extends AsyncTask<String, String, String> {

        Context context;
        AlertDialog pd;

        public PlaceOrder(Context context) {
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
                if (Preferences.getSavedPreferences(context, "usertype").equals("1")) {
                    Service = "add_order_user_data";
                    nameValuePairs.add(new BasicNameValuePair("user_id", Preferences.getSavedPreferences(context, "user_id")));

                } else {
                    Service = "add_order_depot_data";
                    nameValuePairs.add(new BasicNameValuePair("depot_id", Preferences.getSavedPreferences(context, "depot_id")));

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

                    if (Preferences.getSavedPreferences(context, "usertype").equals("1")) {
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
                    if (Preferences.getSavedPreferences(context, "usertype").equals("1")) {
                        nameValuePairs.add(new BasicNameValuePair("user_from_add_id", order_from_address.getAddress_id()));
                    } else {
                        nameValuePairs.add(new BasicNameValuePair("depot_from_add_id", order_from_address.getAddress_id()));
                    }

                }

                if (Preferences.getSavedPreferences(context, "usertype").equals("1")) {
                    Log.e("IN ONE", "doInBackground: " + "IN ONE");
                    for (int i = 0; i < Constants.order_to_address.size(); i++) {
                        if (Constants.order_to_address.get(i).getAddress_id() != null) {
                            if (Constants.order_to_address.get(i).getAddress_id().equals("0")) {
                                nameValuePairs.add(new BasicNameValuePair("user_to_address_id[" + i + "]", "0"));
                                nameValuePairs.add(new BasicNameValuePair("to_name[" + i + "]", Constants.order_to_address.get(i).getName()));
                                nameValuePairs.add(new BasicNameValuePair("to_address_line_1[" + i + "]", Constants.order_to_address.get(i).getAddress_line_1()));
                                nameValuePairs.add(new BasicNameValuePair("to_address_line_2[" + i + "]", Constants.order_to_address.get(i).getAddress_line_2()));
                                nameValuePairs.add(new BasicNameValuePair("pin[" + i + "]", Constants.order_to_address.get(i).getPin()));
                                nameValuePairs.add(new BasicNameValuePair("to_area[" + i + "]", Constants.order_to_address.get(i).getArea_key()));
                                nameValuePairs.add(new BasicNameValuePair("to_city[" + i + "]", Constants.order_to_address.get(i).getCity_key()));
                                nameValuePairs.add(new BasicNameValuePair("to_state[" + i + "]", Constants.order_to_address.get(i).getState()));
                                nameValuePairs.add(new BasicNameValuePair("to_mobile[" + i + "]", Constants.order_to_address.get(i).getMobile()));
                            } else {
                                nameValuePairs.add(new BasicNameValuePair("user_to_address_id[" + i + "]", Constants.order_to_address.get(i).getAddress_id()));
                            }

                            nameValuePairs.add(new BasicNameValuePair("km[" + i + "]", Constants.order_to_address.get(i).getKm()));

                        }
                    }
                } else {
                    for (int i = 0; i < Constants.order_to_address.size(); i++) {
                        if (Constants.order_to_address.get(i).getAddress_id() != null) {

                            if (Constants.order_to_address.get(i).getAddress_id().equals("0")) {
                                nameValuePairs.add(new BasicNameValuePair("depot_to_address_id[]", "0"));
                                nameValuePairs.add(new BasicNameValuePair("to_name[" + i + "]", Constants.order_to_address.get(i).getName()));
                                nameValuePairs.add(new BasicNameValuePair("to_address_line_1[" + i + "]", Constants.order_to_address.get(i).getAddress_line_1()));
                                nameValuePairs.add(new BasicNameValuePair("to_address_line_2[" + i + "]", Constants.order_to_address.get(i).getAddress_line_2()));
                                nameValuePairs.add(new BasicNameValuePair("pin[" + i + "]", Constants.order_to_address.get(i).getPin()));
                                nameValuePairs.add(new BasicNameValuePair("to_area[" + i + "]", Constants.order_to_address.get(i).getArea_key()));
                                nameValuePairs.add(new BasicNameValuePair("to_city[" + i + "]", Constants.order_to_address.get(i).getCity_key()));
                                nameValuePairs.add(new BasicNameValuePair("to_state[" + i + "]", Constants.order_to_address.get(i).getState()));
                                nameValuePairs.add(new BasicNameValuePair("to_mobile[" + i + "]", Constants.order_to_address.get(i).getMobile()));
                            } else {
                                nameValuePairs.add(new BasicNameValuePair("depot_to_address_id[" + i + "]", Constants.order_to_address.get(i).getAddress_id()));
                            }
                            nameValuePairs.add(new BasicNameValuePair("individual_price[" + i + "]", Constants.order_to_address.get(i).getIndividual_price()));
                            nameValuePairs.add(new BasicNameValuePair("km[" + i + "]", Constants.order_to_address.get(i).getKm()));
                            nameValuePairs.add(new BasicNameValuePair("duration[" + i + "]", duration));

                        }
                    }
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

                        Alert.showAlertWithPause(context, "Your order has been placed successfully!");
                        Constants.promocode_disc = Float.valueOf(0);
                        dialog_searching();
                    } else {
                        Alert.ShowAlert(context,
                                jobj1.getString("responseMessage"));
                    }
                }

                if (pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/


    private void dialog_searching() {

        LayoutInflater factory = LayoutInflater.from(context);
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

        final Dialog dialog_location = new Dialog(context);
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
                    context.startActivity(new Intent(context, AcceptedDeliveriesActivity.class));
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
                context.startActivity(new Intent(context, AcceptedDeliveriesActivity.class));
            }
        });
    }
}

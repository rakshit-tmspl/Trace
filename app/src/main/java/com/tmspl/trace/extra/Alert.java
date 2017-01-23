package com.tmspl.trace.extra;

/**
 * Created by rakshit.sathwara on 1/23/2017.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.tmspl.trace.R;


public class Alert extends Activity {
    public static boolean flg;
    static boolean mResult;

    @SuppressLint("NewApi")
    public static void showInternetConnError(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        builder
                .setMessage("Please Check Your Internet Connection...!!!")
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(context.getApplicationInfo().loadLabel(context.getPackageManager()))
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @SuppressLint("NewApi")
    public static void ShowAlert(Context context, String Message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle(context.getApplicationInfo().loadLabel(context.getPackageManager()))
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(Message)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @SuppressLint("NewApi")
    public static void showError(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle(context.getApplicationInfo().loadLabel(context.getPackageManager()))
                .setMessage("Something Goes Wrong Please Try Again...!!!")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @SuppressLint("NewApi")
    public static void showAlertWithPause(Context context, String msg) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                throw new RuntimeException();
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle(context.getApplicationInfo().loadLabel(context.getPackageManager()))
                .setMessage(msg)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                handler.sendMessage(handler.obtainMessage());
                            }
                        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        try {
            Looper.loop();
        } catch (RuntimeException e2) {
        }
    }

    @SuppressLint("NewApi")
    public static void showAlertWithFinish(final Activity context, String msg) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                throw new RuntimeException();
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle(context.getApplicationInfo().loadLabel(context.getPackageManager()))
                .setMessage(msg)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                handler.sendMessage(handler.obtainMessage());
                                context.finish();
                            }
                        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        try {
            Looper.loop();
        } catch (RuntimeException e2) {
        }
    }

    @SuppressLint("NewApi")
    public static boolean showYesNoWithExecutionStop(String message, Context context) {
        // make a handler that throws a runtime exception when a message is received
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                throw new RuntimeException();
            }
        };

        // make a text input dialog and show it
        AlertDialog.Builder alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        alert.setTitle(context.getApplicationInfo().loadLabel(context.getPackageManager()));
        alert.setCancelable(false);
        alert.setMessage(message);
        alert.setIcon(R.mipmap.ic_launcher);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mResult = true;
                handler.sendMessage(handler.obtainMessage());
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mResult = false;
                handler.sendMessage(handler.obtainMessage());
            }
        });
        alert.show();

        // loop till a runtime exception is triggered.
        try {
            Looper.loop();
        } catch (RuntimeException e2) {
        }

        return mResult;
    }


}

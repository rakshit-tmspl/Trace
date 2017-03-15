package com.tmspl.trace.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by rakshit.sathwara on 2/14/2017.
 */

public abstract class CustomDialog extends Dialog implements View.OnClickListener, DialogInterface.OnClickListener {
    public CustomDialog(Context context) {
        super(context);
    }

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);


        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}

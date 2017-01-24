package com.tmspl.trace.extra;

import android.widget.EditText;


public class CheckEditValue {

    public static boolean isInValid(EditText editText) {
        if (editText.getText().toString().length() > 0)
            return false;
        return true;
    }

    public static boolean isValidEmail(EditText editText) {
        if (editText.getText().toString() == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString()).matches();
        }
    }
}

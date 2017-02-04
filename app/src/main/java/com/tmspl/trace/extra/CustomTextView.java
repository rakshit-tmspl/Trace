package com.tmspl.trace.extra;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Rakshit on 04-02-2017 at 11:07.
 */

public class CustomTextView extends TextView {
    public CustomTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Ubuntu-Medium.ttf", context);
        setTypeface(customFont);

    }
}

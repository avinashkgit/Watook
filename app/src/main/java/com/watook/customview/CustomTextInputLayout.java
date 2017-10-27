package com.watook.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.EditText;

import java.lang.reflect.Field;

/**
 * Created by Rajesh Jadi on 6/20/2016.
 */
public class CustomTextInputLayout extends TextInputLayout {

    public CustomTextInputLayout(Context context) {
        super(context);
        initFont(context);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFont(context);
    }

    private void initFont(Context context) {
        final Typeface typeface = Typeface.createFromAsset(
                context.getAssets(), "fonts/ProximaNovaLight0.otf");

        EditText editText = getEditText();
        if (editText != null) {
            editText.setTypeface(typeface);
            //editText.setText(Color.BLACK);
        }
        try {
            // Retrieve the CollapsingTextHelper Field
            final Field cthf = TextInputLayout.class.getDeclaredField("mCollapsingTextHelper");
            cthf.setAccessible(true);

            // Retrieve an instance of CollapsingTextHelper and its TextPaint
            final Object cth = cthf.get(this);
            final Field tpf = cth.getClass().getDeclaredField("mTextPaint");
            tpf.setAccessible(true);

            // Apply your Typeface to the CollapsingTextHelper TextPaint
            ((TextPaint) tpf.get(cth)).setTypeface(typeface);
        } catch (Exception ignored) {
            // Nothing to do
        }
    }
}

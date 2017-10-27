package com.watook.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * TODO: document your custom view class.
 */
public class CustomButtonView extends Button {


    public CustomButtonView(Context context) {
        super(context);
        if (!isInEditMode()) {
            init(null);
        }
    }

    public CustomButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(attrs);
        }
    }

    public CustomButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            init(attrs);
        }
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, com.watook.R.styleable.CustomButtonView);
            String fontName = a.getString(com.watook.R.styleable.CustomButtonView_fontName_ButtonText);
            if (fontName != null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }
}

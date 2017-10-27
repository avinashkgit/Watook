package com.watook.customview;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.watook.R;

/**
 * Created by Rajesh Jadi on 7/1/2016.
 */
@SuppressWarnings("ALL")
public class CustomProgressDialog extends ProgressDialog {
    private String strMessage;
    private Context context;

    public CustomProgressDialog(Context context) {
        super(context);
    }

    public ProgressDialog CreateDialog(Context context, String message) {
        CustomProgressDialog dialog = new CustomProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        this.strMessage = message;
        this.context = context;
        return dialog;
    }

    // TODO Remove unused code found by UCDetector
    // public CustomProgressDialog(Context context, int theme) {
    // super(context, theme);
    // }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.temp);
        progressBar.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_ATOP);
        CustomTextView txtMessage = (CustomTextView) findViewById(R.id.txtMessage);
        txtMessage.setText(strMessage);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}


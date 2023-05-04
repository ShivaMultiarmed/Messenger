package com.example.messenger;

import android.content.Context;
import android.content.res.ColorStateList;

import androidx.appcompat.widget.AppCompatRadioButton;

public class CustomRadio extends AppCompatRadioButton {

    protected boolean secure;
    protected String purpose;

    public CustomRadio(Context context, boolean secure, String purpose, String placeholder)
    {
        super(context);

        init(secure, purpose);
        setStyle(placeholder);
    }

    protected  void init(boolean secure, String purpose)
    {
        this.secure = secure;
        this.purpose = purpose;
    }
    protected void setStyle(String placeholder)
    {
        this.setText(placeholder);
        //setButtonTintList(new ColorStateList());
    }
}

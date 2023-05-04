package com.example.messenger;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;

public class CustomButton extends AppCompatButton {

    protected String purpose, text;
    protected LinearLayout.LayoutParams layout;

    public CustomButton(Context context, String purpose, String text)
    {
        super(context);

        this.init(purpose);
        this.setStyle(text);
    }

    protected void init(String purpose)
    {
        this.purpose = purpose;
    }

    protected void setStyle(String text)
    {
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setMargins(0,10,0,10);
        layout.gravity = Gravity.CENTER;
        this.setLayoutParams(layout);

        this.setPadding(15, 5,15,5);

        this.setTextColor(Color.argb(255,255,255,255));
        this.setTextSize(20);
        this.setText(text);
        this.setAllCaps(false);

        PaintDrawable bg = new PaintDrawable();
        bg.getPaint().setColor(Color.argb(255,100,200,255));
        bg.getPaint().setStrokeWidth(3);
        bg.setIntrinsicWidth(this.getMeasuredWidth());
        bg.setIntrinsicHeight(this.getMeasuredHeight());
        bg.setCornerRadius(20);
        this.setBackground(bg);
    }
}

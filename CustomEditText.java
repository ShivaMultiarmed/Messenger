package com.example.messenger;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;

public class CustomEditText extends AppCompatEditText {

    protected String purpose;
    protected boolean secure;

    protected LinearLayout.LayoutParams layout;

    public CustomEditText (Context context, boolean secure, String purpose, String placeloder)
    {
        super(context);

        init(secure, purpose);

        setStyle(placeloder);

    }

    public CustomEditText (Context context, String placeholder)
    {
        super(context);
        setStyle(placeholder);
    }

    protected void init(boolean secure, String purpose)
    {
        this.secure = secure;
        this.purpose = purpose;
    }

    protected void setStyle(String placeholder)
    {
        this.setHint(placeholder);
        //this.setInputType(AppCompatEditText.);

        /*PaintDrawable bg = new PaintDrawable();
        bg.setIntrinsicWidth(this.getMeasuredWidth());
        bg.setIntrinsicHeight(this.getMeasuredHeight());
        bg.getPaint().setColor(Color.argb(255,255,255,255));*/
        this.setBackground(getResources().getDrawable(R.drawable.viewborder));

        layout = new LinearLayout.LayoutParams(800, 100);
        layout.setMargins(0,20, 0 ,40);
        layout.gravity = Gravity.CENTER;
        this.setLayoutParams(layout);

        setPadding(15, 5,15,5);
        setTextSize(20);
        setTextColor(Color.argb(255,50,50,50));
    }


}

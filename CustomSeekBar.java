package com.example.messenger;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;

public class CustomSeekBar  extends AppCompatSeekBar {

    protected boolean secure;
    protected String purpose;

    public CustomSeekBar(Context context, boolean secure, String purpose, String placeholder)
    {
        super(context);

        init(secure,purpose);
        setStyle(placeholder);
    }

    protected void init(boolean secure, String purpose)
    {
        this.secure = secure;
        this.purpose = purpose;
        setMax(150);
    }

    protected void setStyle(String placeholder)
    {
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(800, 100);
        layout.gravity = Gravity.CENTER;
        layout.setMargins(0, 20, 0 , 40);
        this.setLayoutParams(layout);
        
        //ColorDrawable thumb = new ColorDrawable();
        //thumb.setTint(Color.argb(255,100,200,255));
        //setThumb(thumb);
    }

}

package com.example.messenger;

import android.content.Context;
import android.view.Gravity;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomRadioGroup extends RadioGroup {

    protected TextView label;
    protected ArrayList<CustomRadio> radios;

    public CustomRadioGroup(Context context, String placeholder, ArrayList<CustomRadio> radios) {
        super(context);
        init(radios);
        setStyle(placeholder);
    }

    private void init(ArrayList<CustomRadio> radios)
    {
        this.radios = radios;
    }
    protected void setStyle(String placeholder)
    {
        RadioGroup.LayoutParams layout = new RadioGroup.LayoutParams(800, RadioGroup.LayoutParams.WRAP_CONTENT);
        layout.gravity = Gravity.CENTER;
        this.setLayoutParams(layout);

        label = new TextView(getContext());
        label.setText(placeholder);
        label.setTextSize(20);
        this.addView(label);

        for (CustomRadio r: radios)
            this.addView(r);
    }
}

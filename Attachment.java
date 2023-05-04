package com.example.messenger;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Attachment extends LinearLayout {

    private long attId, msgId;
    public String url;
    private LayoutParams layout;

    public Attachment(Context context, long attId, long msgId, String format)
    {
        super(context);
        this.attId = attId;
        this.msgId = msgId;
        url = "http://somespace.ru/attachments/"+attId+"."+format;
    }

    public void setLayout()
    {
        setOrientation(HORIZONTAL);
        layout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(layout);
    }

}

package com.example.messenger;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;

public class ChatInfo extends LinearLayout {

    private LayoutParams layout;

    public long chatid, userid;
    public String nick, last, dt; // last - last message, dt - datetime

    private ShapeableImageView img;
    private TextView nickLabel, lastMessage, dateTime;
    private LinearLayout details, msg;

    private Context context;

    public ChatInfo(Context context, long chatid, long userid, String nick, String lastMessage, String dt, DBConnector dbConnector) {
        super(context);
        this.context = context;
        initInfo(chatid, userid, nick, lastMessage, dt, dbConnector);
        setLayout();
    }

    public void initInfo(long chatid, long userid, String nick, String lastMessage, String dt, DBConnector dbConnector)
    {
        this.chatid = chatid;
        this.userid = userid;
        this.nick = nick;
        this.last = lastMessage;
        this.dt = dt;

        this.setIntent(dbConnector);
    }

    public void setLayout()
    {
        this.setOrientation(HORIZONTAL);
        this.layout = new LayoutParams(LayoutParams.MATCH_PARENT, 300);
        this.layout.bottomMargin = 5;
        this.setPadding(40, 10 ,40, 10);
        this.setLayoutParams(this.layout);

        this.setBackgroundColor(Color.argb(255,255,255,255));

        img  = new ShapeableImageView(getContext());
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        ShapeAppearanceModel model = img.getShapeAppearanceModel().toBuilder().setAllCorners(CornerFamily.ROUNDED,125).build();
        img.setShapeAppearanceModel(model);
        LayoutParams imgLayout = new LayoutParams(250, 250);
        imgLayout.gravity = Gravity.CENTER_VERTICAL;
        img.setLayoutParams(imgLayout);
        GradientDrawable  circle = new GradientDrawable();
        circle.setColor(Color.argb(255,255,255,255));
        circle.setCornerRadius(125);
        //img.setClipBounds();
        img.setBackground(circle);

        details = new LinearLayout(getContext());
        details.setOrientation(LinearLayout.VERTICAL);
        LayoutParams detailsLayout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        detailsLayout.gravity = Gravity.CENTER_VERTICAL;
        detailsLayout.leftMargin = 40;
        details.setLayoutParams(detailsLayout);


        nickLabel = new TextView(getContext());
        nickLabel.setText(nick);
        nickLabel.setTextSize(20);
        nickLabel.setTextColor(Color.argb(255,50,50,50));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            nickLabel.setTypeface(Typeface.create(null, 700, false));
        }
        details.addView(nickLabel);

        msg = new LinearLayout(getContext());
        msg.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams msgLayout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        msg.setLayoutParams(msgLayout);
        details.addView(msg);

        lastMessage = new TextView(getContext());
        lastMessage.setText(last);
        lastMessage.setTextSize(17);
        lastMessage.setTextColor(Color.argb(255,50,50,50));
        msg.addView(lastMessage);


        dateTime = new TextView(getContext());
        dateTime.setText(dt);
        LayoutParams dtLayout = new LayoutParams(150,100);
        dtLayout.gravity = Gravity.END;
        dateTime.setLayoutParams(dtLayout);
        dateTime.setTextSize(14);
        dateTime.setTextColor(Color.argb(255,200,200,200));
        msg.addView(dateTime);

        this.addView(img);
        this.addView(details);
    }

    private void setIntent(DBConnector dbConnector)
    {
        this.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(context, Chat.class);
                        Bundle bundle = new Bundle();
                        bundle.putLong("userid",userid);
                        bundle.putLong("chatid", chatid);
                        bundle.putString("nick", nick);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                }
        );

    }
    // id is userid if chat is private
    // id is chatid if chat is not private
    public void setBitmap(long id, boolean isPrivate)
    {
        URL url = null;
        Bitmap bitmap = null;
        try {
            if (!isPrivate)
                url = new URL("http://somespace.ru/chats/"+id+"/ava.png");
            else
            {
                url = new URL("http://somespace.ru/users/"+id+"/ava.png");
            }
            InputStream is = url.openStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
            img.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace(System.err);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

    }
    public void setChatName(String chatName)
    {
        this.nick = chatName;
        nickLabel.setText(nick);
    }
}

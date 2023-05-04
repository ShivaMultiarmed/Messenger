package com.example.messenger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Message extends LinearLayout {
    private LayoutParams layout, textBoxLayout;
    private TextView nickLabel, textLabel, dateLabel;
    private long userid, chatid;
    public long messageid;
    private String dateTime, text;
    public String nick;
    private boolean isMine, isRepeated, hasAttachments;
    private LinearLayout textBox;
    private ShapeableImageView avatar;
    private Bitmap bitmap;
    private LinearLayout extraBox;
    private ArrayList<Long> attIds; // attachments ids
    public Message(Context context, long chatid, long userid, long messageid)
    {
        super(context);
        this.chatid = chatid;
        this.userid = userid;
        this.messageid = messageid;

        hasAttachments = false;
    }

    public void setDetails(String nick, String dateTime, String text, boolean isMine, boolean isRepeated)
    {
        this.nick = nick;
        this.dateTime = dateTime;
        this.text = text;
        this.isMine = isMine;
        this.isRepeated = isRepeated;
    }
    public void setBitmap(Bitmap ava)
    {
        avatar.setImageBitmap(ava);
    }
    public boolean getHasAttachments()
    {
        return hasAttachments;
    }
    public void setHasAttachments(boolean hasAttachments)
    {
        this.hasAttachments = hasAttachments;
        attIds = new ArrayList<>();
        extraBox = new LinearLayout(getContext());
        extraBox.setOrientation(LinearLayout.VERTICAL);
        textBox.addView(extraBox, textBox.getChildCount()-1);
    }
    public void addAttachment(long attId, Attachment attachment)
    {
        attIds.add(attId);
        extraBox.addView(attachment);
    }

    public void setLayout()
    {
        this.setOrientation(LinearLayout.HORIZONTAL);

        layout = new LayoutParams(700, LayoutParams.WRAP_CONTENT);
        if (isMine)
            layout.gravity = Gravity.RIGHT;
        else
            layout.gravity = Gravity.LEFT;
        layout.setMargins(0, 15,0,15);
        this.setLayoutParams(layout);

        if (isMine)
        {
            this.setLayoutDirection(LAYOUT_DIRECTION_LTR);
        }
        else
        {
            this.setLayoutDirection(LAYOUT_DIRECTION_RTL);
        }

        if (!isRepeated)
        {
            avatar = new ShapeableImageView(getContext());

            avatar.setImageBitmap(bitmap);
            //avatar.setImageResource(R.drawable.person);
            avatar.setScaleType(ImageView.ScaleType.FIT_XY);
            LayoutParams avatarLayout = new LayoutParams(80, 80);
            avatarLayout.setMargins(10, 25,10,0);
            avatar.setLayoutParams(avatarLayout);
            ShapeAppearanceModel model = avatar.getShapeAppearanceModel().toBuilder().setAllCorners(CornerFamily.ROUNDED,avatarLayout.width/2).build();
            avatar.setShapeAppearanceModel(model);

        }

        textBox = new LinearLayout(getContext());
        textBox.setPadding(35,35,35,35);
        textBox.setOrientation(LinearLayout.VERTICAL);
        textBoxLayout = new LayoutParams(600,LayoutParams.WRAP_CONTENT);
        textBox.setLayoutParams(textBoxLayout);
        this.addView(textBox);
        if (!isRepeated)
            this.addView(avatar);

        PaintDrawable bg = new PaintDrawable();
        bg.setIntrinsicWidth(getMeasuredWidth());
        bg.setIntrinsicHeight(getMeasuredHeight());
        bg.setCornerRadius(20);

        if (isMine)
        {
            bg.getPaint().setColor(Color.argb(255,187, 217, 250));
        }
        else
        {
            bg.getPaint().setColor(Color.argb(255,240, 240, 240));

        }
        textBox.setBackground(bg);


        if (!isRepeated)
        {
            nickLabel = new TextView(getContext());
            nickLabel.setText(nick);
            LayoutParams nickLayout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            nickLabel.setLayoutParams(nickLayout);
            textBox.addView(nickLabel);
            nickLabel.setTextColor(Color.argb(255,10, 83, 171));
            nickLabel.setTextSize(16);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                nickLabel.setTypeface(Typeface.create(null, 700, false));
            }
        }


        textLabel = new TextView(getContext());
        textLabel.setText(text);
        LayoutParams textLayout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        textLayout.topMargin = 10;
        textLabel.setLayoutParams(textLayout);
        textBox.addView(textLabel);
        textLabel.setTextColor(Color.argb(255, 20, 20, 20));
        textLabel.setTextSize(16);

        dateLabel = new TextView(getContext());
        LayoutParams dateLayout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        dateLabel.setLayoutParams(dateLayout);
        dateLabel.setText(dateTime);
        textBox.addView(dateLabel);
        dateLabel.setTextSize(12);

    }


}

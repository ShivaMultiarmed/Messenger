package com.example.messenger;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.imageview.ShapeableImageView;

public class ImageAttachment extends Attachment{

    private ShapeableImageView img;
    public ImageAttachment(Context context, long attId, long msgId, String format)
    {
        super(context, attId, msgId, format);
    }


    public void setLayout(Bitmap bitmap)
    {
        super.setLayout();
        LayoutParams layout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layout.setMargins(0, 5, 0, 5);
        setLayoutParams(layout);

        img = new ShapeableImageView(getContext());
        img.setImageBitmap(bitmap);
        LayoutParams imgLayout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        img.setLayoutParams(imgLayout);
        addView(img);

    }

}

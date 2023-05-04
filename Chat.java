package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.net.ftp.FTPClient;

public class Chat extends AppCompatActivity {

    private TreeMap<Long, Bitmap> bitmaps;

    private FTPImproved ftpClient;

    private DBConnector dbConnector;
    public Bundle bundle;
    private long userid, chatid;
    private String nick;
    private LinearLayout content;
    private TextView title;
    private LinkedHashMap<Long,Message> msgs;
    private LinearLayout.LayoutParams layout;

    private LinearLayout bottomBar, msgForm;
    private EditText textarea;
    private ImageView sendbtn, attachBtn;
    private ScrollView SV;
    private FileInputStream ChosenFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        ChatAsync initTask = new ChatAsync(ChatAsync.INIT);
        initTask.execute();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
    private void init ()
    {
        SV = (ScrollView) findViewById(R.id.chatScrollView);
        content = (LinearLayout)findViewById(R.id.content);

        textarea  = findViewById(R.id.textarea);
        textarea.setHint("Сообщение");

        title = (TextView)findViewById(R.id.thetitle);
        title.setText(nick);

        sendbtn = findViewById(R.id.sendbtn);
        attachBtn = findViewById(R.id.attach);
        attachBtn.setOnClickListener(view-> {
            pickFile();
        });
        initSend();
    }

    private void getBundle()
    {
        bundle = this.getIntent().getExtras();
        userid = bundle.getLong("userid");
        chatid = bundle.getLong("chatid");
        nick = bundle.getString("nick");

        dbConnector = new DBConnector();
        dbConnector.connect();

        ftpClient = new FTPImproved();
        ftpClient.connect();
    }

    private void getAllMessages(long chatid)
    {
        msgs = new LinkedHashMap<>();
        try{
            ResultSet set = dbConnector.getAllMessages(chatid);

            long prevUid = 0; // previous userid
            while(set.next())
            {
                long uid = set.getLong("userid"); //userid
                String nick = set.getString("nick");
                String text = set.getString("text");
                Timestamp dt = set.getTimestamp("datetime");
                String datetime  = dt.getHours() + ":"+dt.getMinutes();// datetime
                Bitmap b = null;
                long mid = set.getLong("messageid"); //message id
                if (prevUid != uid)
                    b= bitmaps.get(uid);

                Message m = new Message(this, chatid, uid, mid);
                m.setDetails(nick, datetime, text, uid == userid, prevUid==uid);
                m.setLayout();
                if(b!=null)
                    m.setBitmap(b);
                msgs.put(mid, m);

                prevUid = uid;

            }
            set.getStatement().close();
            set.close();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

    }

    private void addAllMessages()
    {
        for (Map.Entry<Long, Message> msg : msgs.entrySet())
        {
            this.content.addView(msg.getValue()); // retrieving message object
        }
    }

    private void setLayout()
    {
       /* layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        this.content.setLayoutParams(layout);*/

        this.content.setPadding(50, 50, 50, 50);

        content.setBackgroundColor(Color.argb(255,255,255,255));

        msgForm = this.findViewById(R.id.msgForm);

        textarea.setTextColor(Color.argb(255,50, 50, 50));
        textarea.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        /*LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
                        if (p.height == 100)
                            p.height = 1800;
                        else
                            p.height = 100;
                        textarea.setLayoutParams(p);*/
                    }
                }
        );
    }
    private void clearAllMesages()
    {
        content.removeAllViews();
    }

    private void initSend()
    {
        sendbtn.setOnClickListener(view -> {
            if (!textarea.getText().toString().equals(""))
                new ChatAsync(ChatAsync.SEND_MESSAGE).execute();
        });
    }

    private Bitmap getBitmap(String url)
    {
        Bitmap b = null;
        try {
            URL u = new URL(url);
            InputStream s = (InputStream) u.getContent();
            //File f = new File(url);
            //InputStream s = new FileInputStream(url);
            b = BitmapFactory.decodeStream(s);
            s.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return b;
    }
    private void getAllBitmaps(long chatid)
    {
        bitmaps = new TreeMap<>();
        try {
            ResultSet usersInChat = dbConnector.getAllUsers(chatid);
            while(usersInChat.next())
            {
                long uid = usersInChat.getLong("userid");
                System.out.println("bitmap of " + usersInChat.getString("nick") + " uid: "+uid);
                System.out.println("here //");

                bitmaps.put(uid, getBitmap("http://somespace.ru/users/"+uid+"/ava.png"));
            }
            usersInChat.getStatement().close();
            usersInChat.close();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }
    private class ChatAsync extends AsyncTask<Object, Void, Void> {

        public static final byte INIT = 1, REFRESH = 2, SEND_MESSAGE = 3;

        private final byte type;

        public ChatAsync(byte type)
        {
            this.type = type;
        }

        @Override
        protected Void doInBackground(Object... objects) {
            switch (type)
            {
                case INIT:
                    getBundle();
                    getAllBitmaps(chatid);
                    getAllMessages(chatid);
                    getAllAttachments(chatid);
                    break;
                case REFRESH:
                    getAllMessages(chatid);
                    break;
                case SEND_MESSAGE:
                    sendMessage();
                    getAllMessages(chatid);

                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            switch (type)
            {
                case INIT:
                    init();
                    setLayout();
                    addAllMessages();
                    break;
                case REFRESH:
                    clearAllMesages();
                    addAllMessages();
                    break;
                case SEND_MESSAGE:
                    clearAllMesages();
                    addAllMessages();
                    SV.scrollTo(0, SV.getBottom());
                    textarea.setText("");

                    break;
            }

        }

    }

    private void sendMessage() {
        long userid = this.userid, chatid = this.chatid;
        String text = textarea.getText().toString();

        dbConnector.sendMessage(userid, chatid, text);
    }

    private void getAllAttachments(long chatid)
    {
        ResultSet a  = dbConnector.getAllAttachments(chatid);//attachments

        try {
            while(a.next())
            {
                long attId = a.getLong("attid"), mId = a.getLong("messageid");
                String type = a.getString("type"), format = a.getString("format");
                Attachment some = createAttachment(attId, mId, type, format);
                Message curMsg = msgs.get(mId);
                if (!curMsg.getHasAttachments())
                    curMsg.setHasAttachments(true);
                curMsg.addAttachment(attId, some);
            }

            a.getStatement().close();
            a.close();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }
    private Attachment createAttachment(long attId,long mId, String type, String format)
    {
        Attachment something = null;

        switch(type)
        {
            case "image":
                ImageAttachment someImg = new ImageAttachment(this, attId, mId, format);
                Bitmap b = getBitmap(someImg.url);
                someImg.setLayout(b);
                something = someImg;
                break;
        }

        return something;
    }
    private void pickFile()
    {
        Intent filePicker = new Intent(Intent.ACTION_GET_CONTENT);
        filePicker.setType("*/*");
        startActivityForResult(filePicker,1);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent responseIntent)
    {
        super.onActivityResult(requestCode, resultCode, responseIntent);

        switch (requestCode)
        {
            case 1: // pick a file
                if (resultCode == Activity.RESULT_OK)
                    if (responseIntent != null)
                    {
                        Uri fileURI = responseIntent.getData();
                        System.out.println("Chosen file is "+fileURI.toString());
                        try {
                            FileInputStream f = new FileInputStream(fileURI.getPath().replace("/document/primary:", "/"));
                            int c;
                            System.out.println("file bytes are here");
                            while ((c = f.read()) != -1) {
                                System.out.println(c);
                            }
                        }
                        catch (IOException ex)
                        {
                            ex.printStackTrace(System.err);
                        }
                    }
                break;
        }
    }
}
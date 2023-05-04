package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

public class AllChats extends AppCompatActivity {

    private DBConnector dbConnector;
    private Bundle bundle;
    private LinearLayout content;
    private ArrayList<ChatInfo> infos;

    private long userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_chats);

        ChatsListAsync initTask = new ChatsListAsync(ChatsListAsync.INIT);
        initTask.execute();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }

    private void getBundle()
    {
        dbConnector = new DBConnector();
        dbConnector.connect();

        bundle = this.getIntent().getExtras();
        this.userid = bundle.getLong("userid");
    }

    private void init()
    {
        this.content = (LinearLayout) this.findViewById(R.id.content);
    }

    private void getAllChats()
    {
        try {

            ResultSet infos = dbConnector.getAllChatInfos(userid);

            this.infos = new ArrayList<ChatInfo>();
            while(infos.next())
            {
                Timestamp dt = infos.getTimestamp("datetime");
                String dateTime = dt.getHours() + ":"+dt.getMinutes();
                ChatInfo chat = new ChatInfo(this, infos.getLong("chatid"), userid, infos.getString("chatname"), infos.getString("text"),dateTime, dbConnector);
                if (!infos.getBoolean("chattype")) // not private chat
                    chat.setBitmap(infos.getLong("chatid"), infos.getBoolean("chattype"));
                else
                {
                    ResultSet p  = dbConnector.getPartner(infos.getLong("chatid"), userid);// partner
                    if (p.next()) {
                        chat.setBitmap(p.getLong("userid"), infos.getBoolean("chattype"));
                        chat.setChatName(p.getString("nick"));
                    }
                    p.getStatement().close();
                    p.close();
                }// private chat
                this.infos.add(chat);
            }
            infos.getStatement().close();
            infos.close();

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    private void addChats()
    {
        for (int i = 0; i< this.infos.size(); i++)
        {
            this.content.addView(infos.get(i));
        }
    }

    private class ChatsListAsync extends AsyncTask<Void, Void, Void> {

        public static final byte INIT = 1;
        private final byte type;
        public ChatsListAsync(byte type)
        {
            this.type = type;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            switch (type)
            {
                case INIT:
                    getBundle();
                    getAllChats();
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
                    addChats();
                    break;
            }

        }

    }
}
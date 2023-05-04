package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private TextView responseLabel;
    private DBConnector dbConnector;

    private LinearLayout loginWrapper;
    private CustomEditText nick, password;
    private CustomButton submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        new AsyncLogin().execute("init");
    }

    private void getBundle()
    {
        dbConnector  = new DBConnector();
        dbConnector.connect();
    }
    private void initLayout()
    {
        loginWrapper = (LinearLayout) this.findViewById(R.id.loginWrapper);
        nick = new CustomEditText(this,false, "nick", "Логин");
        password = new CustomEditText(this, true, "password", "Пароль");
        submit = new CustomButton(this,"login", "Войти");
        loginWrapper.addView(nick);
        loginWrapper.addView(password);
        loginWrapper.addView(submit);

        submit.setOnClickListener(view -> { login(nick.getText().toString(), password.getText().toString()); } );

    }
    private void login(String nick, String password)
    {
        if (responseLabel == null)
        {
            responseLabel = new TextView(this);
            loginWrapper.addView(responseLabel);
        }
        if (nick.equals("") || password.equals(""))
            responseLabel.setText("Поля пусты");
        else
            new AsyncLogin().execute("login", nick, password);
    }
    private void onResponse(long response)
    {
        if (responseLabel == null)
        {
            responseLabel = new TextView(this);
            loginWrapper.addView(responseLabel);
        }
        if (response > 0)
            openAllChats(dbConnector,response); // response is the userid here
        else
        {
            if (response == DBConnector.LOGIN_USER_NOT_FOUND)
                responseLabel.setText("Пользователь не найден");
            else if (response == DBConnector.LOGIN_PASSWORD_INCORRECT)
                responseLabel.setText("Пароль неверный.");
        }
    }
    private void openAllChats(DBConnector dbConnector, long userid)
    {

        Intent allChatsIntent = new Intent(this, AllChats.class);
        Bundle bundle = new Bundle();
        bundle.putLong("userid", userid);
        allChatsIntent.putExtras(bundle);
        startActivity(allChatsIntent);
    }
    private class AsyncLogin extends AsyncTask<String, Void, Long>
    {
        private String type;
        private String nick, password;
        private Long result;

        public AsyncLogin()
        { }
        @Override
        protected Long doInBackground(String... strings) {
            this.type = strings[0];
            result = null;

            switch (type)
            {
                case "init":
                    getBundle();
                    break;
                case "login":
                    nick = strings[1]; password = strings[2];
                    result = dbConnector.logIn(nick, password);
                    break;
            }

            return result;
        }
        @Override
        public void onPostExecute(Long response)
        {
            super.onPostExecute(response);
            switch (type)
            {
                case "init":
                    initLayout();
                    break;
                case "login":
                    onResponse(response);
                    break;
            }

        }
    }
}
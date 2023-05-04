package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private CustomEditText[] inputs;
    private CustomSeekBar age;
    private CustomButton submit;
    private LinearLayout content;
    private CustomRadioGroup sex;
    private HashMap<String,String> purposes, data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        content = this.findViewById(R.id.content);
        content.setPadding(0,30,0,30);

        initForm();
        setForm();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }

    protected void initForm()
    {

        purposes = new HashMap<String,String>();
        purposes.put("name", "Имя");
        purposes.put("nick", "Ник-нейм");
        purposes.put("e-mail", "E-mail");
        purposes.put("password", "Пароль");

        inputs = new CustomEditText[purposes.size()];

        int i = 0;
        for (Map.Entry<String, String> item : purposes.entrySet())
            inputs[i++] = new CustomEditText(this,false,item.getKey(),item.getValue());

        age = new CustomSeekBar(this, true, "age", "Введите свой возраст");


        ArrayList<CustomRadio> r = new ArrayList<CustomRadio>();
        r.add(new CustomRadio(this,false, "male", "Мужской"));
        r.add(new CustomRadio(this,false, "female", "Женский"));
        sex = new CustomRadioGroup(this, "Выберите пол", r);

    }

    private void setForm()
    {


        for (int i=0;i<inputs.length;i++)
        {
            content.addView(inputs[i]);
        }
        this.content.addView(sex);
        content.addView(age);
        submit = new CustomButton(this,"signup", "Зарегистрироваться");

        content.addView(submit);
        submit.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        doSignUp();
                    }
                }
        );
    }

    private void doSignUp()
    {
        TextView info = new TextView(this);
        String text = null;
        boolean valid = true;

        for (int i=0;i<inputs.length;i++)
            if (inputs[i].getText().equals(""))
            {
                text = "Поле " + inputs[i].getHint() + " не заполнено.";
                valid = false;
                break;
            }
        if (valid)
        {


        }
        info.setText(text);
        this.content.addView(info);
    }
}
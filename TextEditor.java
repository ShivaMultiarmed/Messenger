package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TextEditor extends AppCompatActivity {

    private File file;
    private android.widget.EditText textarea;
    private Button open, save, cancel;

    public int request = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_text);

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        file = new File(Environment.getRootDirectory().getAbsolutePath().toString()+"/some.txt");
        System.out.println(file.getAbsolutePath());
        if (file.exists())
            System.out.println("IT EXISTS !!!!!!!!" + file.getAbsolutePath());
        else
            System.out.println("IT DOESN'T EXIST");

        readFile(file.getAbsolutePath());

        textarea = findViewById(R.id.textarea);

        textarea.setText("Some initial text here\n");

        this.open = this.findViewById(R.id.open);
        this.save = this.findViewById(R.id.save);
        this.cancel = this.findViewById(R.id.cancel);

        this.open.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        openFileChooser();
                    }
                }
        );
        this.save.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        textarea.append("Saved\n");
                    }
                }
        );
        this.cancel.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        textarea.append("Canceled\n");
                    }
                }
        );
    }

    protected void readFile(String path)
    {
        try {
            FileInputStream fis = new FileInputStream(file);

            int c;
            char ch;
            while((c = fis.read())!=-1)
            {
                ch = (char)c;
                textarea.append(ch+"");
            }

            fis.close();
        }

        catch(FileNotFoundException ex)
        {
            System.out.println("File not found");
        }
        catch(IOException ex)
        {
            System.out.println("Io exception");
        }

    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    private static final int FILE_SELECT_CODE = 0;

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            if (data == null)
                return;
            Uri uri = data.getData();
            System.out.println("!!!!!!!! URI: "+ uri.toString());
            textarea.setText(uri.toString());
            file  = new File(uri.toString());
            System.out.println(file.toURI().toString());
            readFile(file.toURI().toString());
        }
    }
}

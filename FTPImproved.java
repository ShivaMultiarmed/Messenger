package com.example.messenger;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.ArrayList;

public class FTPImproved  extends FTPClient {

    private FTPAsyncTask asyncTask;

    public FTPImproved()
    {
        super();
        asyncTask = new FTPAsyncTask();
    }
    public void connect()
    {
        try
        {
            connect("31.31.196.183", 21);
            login("u1964695","76b9Q31gPKxAyGQx");
            //enterLocalPassiveMode();
            setFileType(FTP.BINARY_FILE_TYPE);
        } catch (SocketException e) {
            e.printStackTrace(System.err);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public Bitmap getImageBitmap(String url)
    {
        connect();
        //asyncTask.execute("bitmap", "/www/tellme/person-icon.png");
        InputStream is = getFileContent(url);
        Bitmap bitmap = BitmapFactory.decodeStream(is);


        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        return bitmap;
    }

    public InputStream getFileContent(String url)
    {
        InputStream is = null;
        try {
            is = retrieveFileStream(url); // by URL

        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return is;
    }

    private class FTPAsyncTask extends AsyncTask<Object, Object, Object>
    {

        @Override
        protected Object doInBackground(Object... objects) {

            Object result = null;

            String type = (String)objects[0];

            switch(type)
            {
                case "bitmap": // getting a bitmap by URL
                    Bitmap bitmap = null;
                    InputStream bis = null;

                    try {
                        bis = retrieveFileStream((String)objects[1]); // by URL
                        bitmap = BitmapFactory.decodeStream(bis);
                    } catch (IOException e) {
                        e.printStackTrace(System.err);
                    }

                    result = bitmap;

                    break;
            }

            return result;
        }
    }

    public boolean downloadFile(String localPath, String remotePath)
    {
        boolean done = false;
        try
        {
          FileOutputStream fos = new FileOutputStream(localPath);
          done = retrieveFile(remotePath, fos);
          fos.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace(System.err);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return done;
    }
    public boolean uploadFile(String localPath, String remotePath)
    {
        boolean done = false;
        try {
            FileInputStream fis = new FileInputStream(localPath);
            done = storeFile(remotePath, fis);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace(System.err);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return done;
    }
}

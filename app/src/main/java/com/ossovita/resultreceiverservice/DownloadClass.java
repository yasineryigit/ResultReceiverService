package com.ossovita.resultreceiverservice;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class DownloadClass extends IntentService {


    public DownloadClass() {
        super("");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String currentThread = Thread.currentThread().getName();
        Log.d(TAG,"onHandleIntent: "+currentThread);
        String result = "";
        String urlInput = intent.getStringExtra("url");
        //receiver key'i ile yolladığımız result receiver objesini aldık
        ResultReceiver resultReceiver = intent.getParcelableExtra("receiver");
        URL url;
        HttpURLConnection httpURLConnection = null;

        try{
            url = new URL(urlInput);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            int data = inputStreamReader.read() ;
            while(data!=-1){
                char current = (char) data;
                result+=current;
                data=inputStreamReader.read();//sonuna kadar okumaya devam et
            }
            Bundle bundle = new Bundle();
            bundle.putString("websiteResult",result);
            //buradaki result code permissionlardaki gibi kontrol amaçlı result kod
            resultReceiver.send(1,bundle);


        }catch (Exception e){
            Bundle bundle = new Bundle();
            bundle.putString("websiteResult","Result dönemedi");
            //buradaki result code permissionlardaki gibi kontrol amaçlı result kod
            resultReceiver.send(1,bundle);

            e.printStackTrace();
        }


    }

    @Override
    public void onCreate() {
        super.onCreate();
        String currentThread = Thread.currentThread().getName();
        Log.d(TAG,"onCreate: "+currentThread);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        String currentThread = Thread.currentThread().getName();
        Log.d(TAG,"onDestroy: "+currentThread);
    }
}

package com.ossovita.resultreceiverservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);


    }

    public void download(View v){
        ResultReceiver myResultReceiver = new MyResultReceiver(null);
        //resultReceiver burada sıradan bir result receiver objesidir,
        //result receiver olsun diye kendi oluşturduğumuz classtan türettik(çünkü o class result receiverı extend ediyordu)
       textView.setText("downloading..");
        startService(new Intent(this,DownloadClass.class)
                .putExtra("url",editText.getText().toString())
                .putExtra("receiver",myResultReceiver));

    }

    public class MyResultReceiver extends ResultReceiver{

        public MyResultReceiver(Handler handler) {
            super(handler);
        }
        //onReceiveResult ile dönen sonuçkarı handler ile kullanabilir hale getiririz
        //Handler'lar ile başka thread'de çalışan DownloadService ile MainThread arasında köprü kuruyoruz
        //handler'lar tek başına bir şey ifade etmez, runnable'lar ile ne yapacağını söylüyoruz
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if(resultCode==1&&resultData!=null){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //websiteResult key'i ile koyduğumuz bundle'ı aldık
                        String result = resultData.getString("websiteResult");
                        textView.setText(result);

                    }
                });
            }

        }
    }
}
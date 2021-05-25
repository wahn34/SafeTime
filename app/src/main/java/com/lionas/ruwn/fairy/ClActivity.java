package com.lionas.ruwn.fairy;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.*;
import android.view.View;
import android.widget.*;
import java.io.*;
import java.util.*;


public class ClActivity extends AppCompatActivity {
    private TextView txtView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cl);
        System.out.println("시작 되었음1");
        context = this;
        txtView = (TextView)findViewById(R.id.txtView);
        Button btnCl = (Button)findViewById(R.id.btnCl);
        btnCl.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
               //String returnedFileContent = readFileFromRawDirectory(R.raw.fcl);
                //txtView.setText(returnedFileContent);
                txtView.setText(readTxt());
            }
        });
    }
    private String readTxt(){

        InputStream inputStream = getResources().openRawResource(R.raw.fcl);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int i;
        try {
            i = inputStream.read();
            while (i != -1)
            {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
                System.out.println(i);
            }
            inputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return byteArrayOutputStream.toString();
    }
    // 무조건 읽기(전체)
    private String readFileFromRawDirectory(int resourceId){
        InputStream iStream = context.getResources().openRawResource(resourceId);
        ByteArrayOutputStream byteStream = null;
        try {
            byte[] buffer = new byte[iStream.available()];
            iStream.read(buffer);
            byteStream = new ByteArrayOutputStream();
            byteStream.write(buffer);
            byteStream.close();
            iStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteStream.toString();
    }

}

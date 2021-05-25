package com.lionas.ruwn.fairy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FairyGPSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fairy_gps);

        Button btnGetGPS =(Button) findViewById(R.id.btn_getGPS);

        btnGetGPS.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

            }
        });
    }
}

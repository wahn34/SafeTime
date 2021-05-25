package com.lionas.ruwn.fairy;

import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LionaConnectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liona_connect);

        final EditText editPhone = (EditText) findViewById(R.id.editPhone);
        final EditText editCode = (EditText) findViewById(R.id.editCode);
        Button btnConnect = (Button) findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editPhone.getText().toString().length() != 0 && editCode.getText().toString().length() != 0)
                {
                    savePhoneNumber(editPhone.getText().toString()) ;
                    Snackbar.make(getWindow().getDecorView().getRootView(), "번호와 코드가 등록되었습니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
    }

    private void getPhoneNumber(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        pref.getString("ph", "");
    }

    private void savePhoneNumber(String strNumber){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ph", strNumber);
        editor.commit();
    }

    private void getOpenCode(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        pref.getString("cd", "");
    }

    private void saveOpenCode(String strNumber){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("cd", strNumber);
        editor.commit();
    }

    private void removePhoneNumber(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("ph");
        editor.commit();
    }

    private void removeAllPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

}

package com.lionas.ruwn.fairy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class MyConnectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_connect);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        final EditText editPhone = (EditText) findViewById(R.id.editPhone);
        final EditText editCode = (EditText) findViewById(R.id.editCode);

        LionaSetData lionaSetData = new LionaSetData();
        editCode.setText(lionaSetData.getLionaCode());
        editPhone.setText(lionaSetData.getLionaPhone());

        Button btnConnect = (Button) findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editPhone.getText().toString().length() != 0 && editCode.getText().toString().length() != 0)
                {
                    savePhoneNumber(editPhone.getText().toString()) ;
                    saveOpenCode(editCode.getText().toString()) ;
                    Snackbar.make(getWindow().getDecorView().getRootView(), "번호와 코드가 등록되었습니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    getAllLionaSettingData();
                    LionaSetData lionaSetData = new LionaSetData();
                    editCode.setText(lionaSetData.getLionaCode());
                    editPhone.setText(lionaSetData.getLionaPhone());
                }
            }
        });


        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
    }

    private void getAllLionaSettingData()
    {
        LionaSetData lionaSetData = new LionaSetData();
        lionaSetData.setLionaPhone(getLionaSettingNumber());
        lionaSetData.setLionaLock(getLionaSettingLock());
        lionaSetData.setLionaCode(getLionaSettingCode());
    }
    private String getLionaSettingNumber()
    {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String str = pref.getString("ph", "");
        return str;
    }
    private Boolean getLionaSettingLock()
    {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String str = pref.getString("lk", "");
        Boolean bol = true;
        if (str.contains("5")) bol= false;
        return bol;
    }
    private String getLionaSettingCode()
    {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String str = pref.getString("cd", "");
        return str;
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

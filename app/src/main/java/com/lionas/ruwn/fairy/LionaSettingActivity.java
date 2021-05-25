package com.lionas.ruwn.fairy;

import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;

public class LionaSettingActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_liona_setting);

        TabHost tabHost = getTabHost();
        LayoutInflater.from(this).inflate(R.layout.activity_liona_setting, tabHost.getTabContentView(), true);

        tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator("기기연결")
                .setContent(new Intent(this, MyConnectActivity.class)));

        tabHost.addTab(tabHost.newTabSpec("tab2")
                .setIndicator("기기설정")
                .setContent(new Intent(this, MySettingActivity.class)));

        tabHost.addTab(tabHost.newTabSpec("tab3")
                .setIndicator("버전정보")
                .setContent(new Intent(this, InfoActivity.class)));
    }
}

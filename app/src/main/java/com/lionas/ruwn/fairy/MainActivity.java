package com.lionas.ruwn.fairy;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public TextView txtCity;
    public TextView txtTemp;
    public ImageView imageView;

    private LionaGPS lionaGPS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getAllLionaSettingData();
        if (isServiceRunning("com.lionas.ruwn.fairy.LionaService")) {
            System.out.println("////////서비스 실행 중/////////////");
        }
        else
        {
            //Alarm();
            Intent intent = new Intent(this, LionaService.class);
            startService(intent);
        }
        setContentView(R.layout.activity_main);
        LionaWeather lionaWeather = new LionaWeather();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Intent sintent = new Intent(MainActivity.this, SplashscreenActivity.class);
        Intent sintent = new Intent(MainActivity.this, SplashActivity.class);
        startActivity(sintent);
        //finish();
        txtCity = (TextView) findViewById(R.id.txtCity);
        txtTemp = (TextView) findViewById(R.id.txtTemp);
        imageView = (ImageView) findViewById(R.id.imageView);

        Button btnPatLocaion = (Button)findViewById(R.id.btnPatLocaion);
        btnPatLocaion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                //Intent intent = new Intent(getApplicationContext(), ClActivity.class);
                //Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                //startActivity(intent);
                Intent intent = new Intent(getApplicationContext(), DetailMapActivity.class);
                //Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
            }
        });

        Button btnCom = (Button)findViewById(R.id.btnCom);
        btnCom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), ComuActivity.class);
                //Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(intent);

                //finish()
            }
        });

        Button btnPatUsage = (Button)findViewById(R.id.btnPatUsage);
        btnPatUsage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                //Intent intent = new Intent(getApplicationContext(), DataBaseActivity.class);
                //Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                Intent intent = new Intent(getApplicationContext(), LionaPatternActivity.class);
                startActivity(intent);

                //finish()
            }
        });

        Button btnNavCar = (Button)findViewById(R.id.btnNavCar);
        btnNavCar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), LionaNaviActivity.class);
                startActivity(intent);
            }
        });
        Button btnNavFoot = (Button)findViewById(R.id.btnNavFoot);
        btnNavFoot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                //Intent intent = new Intent(getApplicationContext(), CommunityActivity.class);

                //Snackbar.make(getWindow().getDecorView().getRootView(), "준비 중인 기능입니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                if (getLionaSettingID().matches(""))
                {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent); //미구현
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), CommunityActivity.class);
                    startActivity(intent); //미구현
                }
            }
        });
        Button btnSetting = (Button)findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), LionaSettingActivity.class);
                startActivity(intent);
                //Snackbar.make(getWindow().getDecorView().getRootView(), "준비 중인 기능입니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        lionaWeather.getWeather();
        txtCity.setText(lionaWeather.get_City());
        txtTemp.setText(lionaWeather.get_Temp());

        String strTemp = txtTemp.getText().toString();
        String[]array;
        array = strTemp.split(", ");
        if (array[0].contains("연무") || array[0].contains("박무") || array[0].contains("갬")) {
            imageView.setBackgroundResource(R.drawable.wd_littlesun);
        }
        else if (array[0].contains("구름많음"))
        {
            imageView.setBackgroundResource(R.drawable.wd_cl);
        }
        else if (array[0].contains("흐림"))
        {
            imageView.setBackgroundResource(R.drawable.wd_raincl);
        }
        else if (array[0].contains("소나기") || array[0].contains("비"))
        {
            imageView.setBackgroundResource(R.drawable.wd_rain);
        }
        else if (array[0].contains("눈"))
        {
            imageView.setBackgroundResource(R.drawable.wd_snow);
        }
        else if (array[0].contains("천둥"))
        {
            imageView.setBackgroundResource(R.drawable.wd_th);
        }
        else if (array[0].contains("바람"))
        {
            imageView.setBackgroundResource(R.drawable.wd_windy);
        }
        else if (array[0].contains("맑음"))
        {
            imageView.setBackgroundResource(R.drawable.wd_windy);
        }
    }
    private void getAllLionaSettingData()
    {
        LionaSetData lionaSetData = new LionaSetData();
        lionaSetData.setLionaPhone(getLionaSettingNumber());
        lionaSetData.setLionaLock(getLionaSettingLock());
        lionaSetData.setLionaCode(getLionaSettingCode());
        lionaSetData.setLionaID(getLionaSettingID());
        lionaSetData.setLionaPW(getLionaSettingPW());
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
    private String getLionaSettingID()
    {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String str = pref.getString("id", "");
        return str;
    }
    private String getLionaSettingPW()
    {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String str = pref.getString("pw", "");
        return str;
    }
    public Boolean isServiceRunning(String serviceName) {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo :
                activityManager.getRunningServices(Integer.MAX_VALUE)) {

            if (serviceName.equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public void Alarm() {
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, LionaAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        //알람시간 calendar에 set해주기
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 16, 25, 0);
        //알람 예약
        am.set(AlarmManager.ELAPSED_REALTIME, calendar.getTimeInMillis(), sender);
        System.out.println("알람등록됨");
    }

}

package com.lionas.ruwn.fairy;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import java.io.FileOutputStream;
import java.io.FileInputStream;

import android.os.Environment;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Broadcast extends BroadcastReceiver {
    private PatternDBHelper patternDBHelper;
    private LionaGPS lionaGPS;
    static Boolean lionaReceive = false;

    LionaPattern lionaPattern = new LionaPattern();

    public void saveOnDataPatternDatabase(Context context, String strDo, String strLocation)
    {
        if (patternDBHelper == null) {
            patternDBHelper = new PatternDBHelper(context, "Pattern", null, 1);
        }
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time = sdfNow.format(new Date(System.currentTimeMillis()));

        LionaPattern lionaPattern = new LionaPattern();
        lionaPattern.setDate(time);
        lionaPattern.setDo(strDo);
        lionaPattern.setLocation(strLocation);

        patternDBHelper.addLionaPatternData(lionaPattern);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String strLocation = "";
        
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("onReceive()", "부팅완료");
        }
        if (Intent.ACTION_SCREEN_ON == intent.getAction()) {
            Log.d("onReceive()", "스크린 ON");
            if (patternDBHelper == null) {
                patternDBHelper = new PatternDBHelper(context, "Pattern", null, 1);
            }

            lionaGPS = new LionaGPS(context);
            // GPS 사용유무 가져오기
            if (lionaGPS.isGetLocation()) {

                double latitude = lionaGPS.getLatitude();
                double longitude = lionaGPS.getLongitude();
                //Toast.makeText(getApplicationContext(),"당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,Toast.LENGTH_LONG).show();
                strLocation = String.format(latitude + "," + longitude);
            } else {
                // GPS 를 사용할수 없으므로
                strLocation = "0,0";
            }

            saveOnDataPatternDatabase(context, "ON", strLocation);
            patternDBHelper.readLionaDatabasePattern();
        }
        if (Intent.ACTION_SCREEN_OFF == intent.getAction()) {
            Log.d("onReceive()", "스크린 OFF");
            if (patternDBHelper == null) {
                patternDBHelper = new PatternDBHelper(context, "Pattern", null, 1);
            }

            lionaGPS = new LionaGPS(context);
            // GPS 사용유무 가져오기
            if (lionaGPS.isGetLocation()) {
                double latitude = lionaGPS.getLatitude();
                double longitude = lionaGPS.getLongitude();
                //Toast.makeText(getApplicationContext(),"당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,Toast.LENGTH_LONG).show();
                strLocation = String.format(latitude + "," + longitude);
            } else {
                // GPS 를 사용할수 없으므로
                strLocation = "0,0";
            }
            saveOnDataPatternDatabase(context, "OFF", strLocation);
            patternDBHelper.readLionaDatabasePattern();
        }

        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Log.d("onReceive()", "문자가 수신되었습니다");

            // SMS 메시지를 파싱합니다.
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for (int i = 0; i < messages.length; i++) {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                smsMessage[i] = SmsMessage.createFromPdu((byte[]) messages[i]);
            }

            // SMS 수신 시간 확인
            Date curDate = new Date(smsMessage[0].getTimestampMillis());
            Log.d("문자 수신 시간", curDate.toString());
            //String str = String.format("문자 수신 시간", curDate.toString());
            //Toast.makeText(context, "Message!", Toast.LENGTH_LONG).show();

            // SMS 발신 번호 확인
            String origNumber = smsMessage[0].getOriginatingAddress();

            // SMS 메시지 확인
            String message = smsMessage[0].getMessageBody().toString();

            //Intent i = new Intent(context, NotiLiona.class);
            //i.putExtra("title", "sexy_girl");
            //context.startActivity(i);
            if (LionaSetData.lionaLock)
            {
                if (origNumber.contains(LionaSetData.lionaPhone))
                {
                    if (lionaReceive)
                    {
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(LionaSetData.lionaPhone, null,origNumber +"에서 문자 받음(" + curDate.toString()+")\n" +message, null, null);

                        if(message.contains(LionaSetData.lionaCode))
                        {
                            lionaReceive = Boolean.FALSE;
                            NotificationManager nm =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            nm.cancel(1234);
                            sms.sendTextMessage(origNumber, null,"문자 당겨오기 서비스를 종료합니다.", null, null);
                        }
                    }
                    else
                    {
                        if(message.contains(LionaSetData.lionaCode))
                        {
                            lionaReceive = Boolean.TRUE;
                            SmsManager sms = SmsManager.getDefault();
                            sms.sendTextMessage(origNumber, null,"문자 당겨오기 서비스를 시작합니다.", null, null);

                            Intent notificationIntent = new Intent(context, NotiLiona.class);
                            notificationIntent.putExtra("notificationId", 1588); //전달할 값
                            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

                            builder.setContentTitle("문자 당겨오기 시작")
                                    .setContentText("문자 당겨오기 서비스가 실행 중입니다.")
                                    .setTicker("문자 당겨오기 시작됨")
                                    .setOngoing(true)
                                    .setSmallIcon(R.drawable.ico_icon)
                                    .setContentIntent(contentIntent)
                                    .setAutoCancel(false)
                                    .setWhen(System.currentTimeMillis())
                                    .setDefaults(Notification.DEFAULT_ALL);


                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                builder.setCategory(Notification.CATEGORY_MESSAGE)
                                        .setPriority(Notification.PRIORITY_HIGH)
                                        .setVisibility(Notification.VISIBILITY_PUBLIC);
                            }

                            NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                            nm.notify(1234, builder.build());
                        }
                    }
                }
            }
            else
            {

            }
            //Log.d("문자 내용", "발신자 : "+origNumber+", 내용 : " + message);
        }
    }

}
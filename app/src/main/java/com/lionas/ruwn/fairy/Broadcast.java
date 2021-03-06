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
            Log.d("onReceive()", "λΆνμλ£");
        }
        if (Intent.ACTION_SCREEN_ON == intent.getAction()) {
            Log.d("onReceive()", "μ€ν¬λ¦° ON");
            if (patternDBHelper == null) {
                patternDBHelper = new PatternDBHelper(context, "Pattern", null, 1);
            }

            lionaGPS = new LionaGPS(context);
            // GPS μ¬μ©μ λ¬΄ κ°μ Έμ€κΈ°
            if (lionaGPS.isGetLocation()) {

                double latitude = lionaGPS.getLatitude();
                double longitude = lionaGPS.getLongitude();
                //Toast.makeText(getApplicationContext(),"λΉμ μ μμΉ - \nμλ: " + latitude + "\nκ²½λ: " + longitude,Toast.LENGTH_LONG).show();
                strLocation = String.format(latitude + "," + longitude);
            } else {
                // GPS λ₯Ό μ¬μ©ν μ μμΌλ―λ‘
                strLocation = "0,0";
            }

            saveOnDataPatternDatabase(context, "ON", strLocation);
            patternDBHelper.readLionaDatabasePattern();
        }
        if (Intent.ACTION_SCREEN_OFF == intent.getAction()) {
            Log.d("onReceive()", "μ€ν¬λ¦° OFF");
            if (patternDBHelper == null) {
                patternDBHelper = new PatternDBHelper(context, "Pattern", null, 1);
            }

            lionaGPS = new LionaGPS(context);
            // GPS μ¬μ©μ λ¬΄ κ°μ Έμ€κΈ°
            if (lionaGPS.isGetLocation()) {
                double latitude = lionaGPS.getLatitude();
                double longitude = lionaGPS.getLongitude();
                //Toast.makeText(getApplicationContext(),"λΉμ μ μμΉ - \nμλ: " + latitude + "\nκ²½λ: " + longitude,Toast.LENGTH_LONG).show();
                strLocation = String.format(latitude + "," + longitude);
            } else {
                // GPS λ₯Ό μ¬μ©ν μ μμΌλ―λ‘
                strLocation = "0,0";
            }
            saveOnDataPatternDatabase(context, "OFF", strLocation);
            patternDBHelper.readLionaDatabasePattern();
        }

        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Log.d("onReceive()", "λ¬Έμκ° μμ λμμ΅λλ€");

            // SMS λ©μμ§λ₯Ό νμ±ν©λλ€.
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for (int i = 0; i < messages.length; i++) {
                // PDU ν¬λ§·μΌλ‘ λμ΄ μλ λ©μμ§λ₯Ό λ³΅μν©λλ€.
                smsMessage[i] = SmsMessage.createFromPdu((byte[]) messages[i]);
            }

            // SMS μμ  μκ° νμΈ
            Date curDate = new Date(smsMessage[0].getTimestampMillis());
            Log.d("λ¬Έμ μμ  μκ°", curDate.toString());
            //String str = String.format("λ¬Έμ μμ  μκ°", curDate.toString());
            //Toast.makeText(context, "Message!", Toast.LENGTH_LONG).show();

            // SMS λ°μ  λ²νΈ νμΈ
            String origNumber = smsMessage[0].getOriginatingAddress();

            // SMS λ©μμ§ νμΈ
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
                        sms.sendTextMessage(LionaSetData.lionaPhone, null,origNumber +"μμ λ¬Έμ λ°μ(" + curDate.toString()+")\n" +message, null, null);

                        if(message.contains(LionaSetData.lionaCode))
                        {
                            lionaReceive = Boolean.FALSE;
                            NotificationManager nm =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            nm.cancel(1234);
                            sms.sendTextMessage(origNumber, null,"λ¬Έμ λΉκ²¨μ€κΈ° μλΉμ€λ₯Ό μ’λ£ν©λλ€.", null, null);
                        }
                    }
                    else
                    {
                        if(message.contains(LionaSetData.lionaCode))
                        {
                            lionaReceive = Boolean.TRUE;
                            SmsManager sms = SmsManager.getDefault();
                            sms.sendTextMessage(origNumber, null,"λ¬Έμ λΉκ²¨μ€κΈ° μλΉμ€λ₯Ό μμν©λλ€.", null, null);

                            Intent notificationIntent = new Intent(context, NotiLiona.class);
                            notificationIntent.putExtra("notificationId", 1588); //μ λ¬ν  κ°
                            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

                            builder.setContentTitle("λ¬Έμ λΉκ²¨μ€κΈ° μμ")
                                    .setContentText("λ¬Έμ λΉκ²¨μ€κΈ° μλΉμ€κ° μ€ν μ€μλλ€.")
                                    .setTicker("λ¬Έμ λΉκ²¨μ€κΈ° μμλ¨")
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
            //Log.d("λ¬Έμ λ΄μ©", "λ°μ μ : "+origNumber+", λ΄μ© : " + message);
        }
    }

}
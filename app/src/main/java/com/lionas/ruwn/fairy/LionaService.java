package com.lionas.ruwn.fairy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;


public class LionaService extends Service
{
    //private final Context mContext;
/*
    public LionaService(Context context) {
        this.mContext = context;
    }*/

    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000 * 60 * 30;
    private static final float LOCATION_DISTANCE = 100f;

    private LocationDBHelper locationDBHelper;
    LionaLocation lionaLocation = new LionaLocation();

    public void saveOnDataLocationDatabase(Context context, String strAccuracy, String strLocation)
    {
        if (locationDBHelper == null) {
            locationDBHelper = new LocationDBHelper(context, "Location", null, 1);
        }

        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time = sdfNow.format(new Date(System.currentTimeMillis()));

        LionaLocation lionaLocation = new LionaLocation();
        lionaLocation.setTime(time);
        lionaLocation.setAccuracy(strAccuracy);
        lionaLocation.setAddress(strLocation);

        locationDBHelper.addLionaLocationData(lionaLocation);
        locationDBHelper.readLionaDatabaseLocation();
    }


    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            System.out.println("LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            //System.out.println("onLocationChanged: " + location);
            //System.out.println(location);
            if (location.getAccuracy() <= 100)
            {
                System.out.println(location.getLatitude());
                System.out.println(location.getLongitude());
                System.out.println(location.getAccuracy());
                mLastLocation.set(location);
                saveOnDataLocationDatabase(getApplicationContext(), location.getAccuracy()+"",location.getLatitude()+","+location.getLongitude());
            }
            else
            {
                System.out.println(" 데이터 불량 : " + location.getLatitude());
                System.out.println(" 데이터 불량 : " + location.getLongitude());
                System.out.println(" 데이터 불량 : " + location.getAccuracy());
            }
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            System.out.println("onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            System.out.println("onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            System.out.println("onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        System.out.println("onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        System.out.println("onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            System.out.println("fail to request location update, ignore");
        } catch (IllegalArgumentException ex) {
            System.out.println("network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            System.out.println("fail to request location update, ignore");
        } catch (IllegalArgumentException ex) {
            System.out.println("gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                }
            }
        }
    }

    private void initializeLocationManager() {
        System.out.println("initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        BroadcastReceiver myReceiver = new Broadcast();
        System.out.println("서비스 등록");
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(myReceiver, intentFilter);
        Log.d("onCreate()","브로드캐스트리시버 등록됨");
    }
}

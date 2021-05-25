package com.lionas.ruwn.fairy;

import android.*;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

public class SplashscreenActivity extends AppCompatActivity {
    private MatchCommnucation matchCommnucation = new MatchCommnucation();
    private DBHelper dbHelper;
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 0;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private int[] MyPemission;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                //delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.WRITE_CONTACTS, android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, MODE_PRIVATE);

        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS);

        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            // 권한 없음
            Snackbar.make(getWindow().getDecorView().getRootView(), "앱을 사용하기 위해 권한이 필요합니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }else{
            // 권한 있음
            Snackbar.make(getWindow().getDecorView().getRootView(), "권한 확인이 완료되었습니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            setStartApplicationSetting();
        }

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS);

        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            Snackbar.make(getWindow().getDecorView().getRootView(), "앱을 사용하기 위해 권한이 필요합니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else
        {
            Snackbar.make(getWindow().getDecorView().getRootView(), "권한 확인이 완료되었습니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            setStartApplicationSetting();
        }
    }

    private void setStartApplicationSetting()
    {
        //txtSplash.setText("초기 설정 준비 중..");
        if (dbHelper == null) {
            dbHelper = new DBHelper(SplashscreenActivity.this,"Liona",null,1);
        }
        if (matchCommnucation.lionaWords.isEmpty())
        {
            dbHelper.readLionaDatabaseCommunication();
            System.out.println("Array 값 여부 확인 : 없어서 채움");
            if (dbHelper.readLionaDatabaseCommunicationAllData()<=0)
            {
                //txtSplash.setText("초기 설정 준비 중..");
                setLionaDataIfNoData();
                System.out.println("DB 값 여부 확인 : 없어서 채움");
                //txtSplash.setText("초기 설정 준비 완료");
            }
        }
        if (matchCommnucation.lionaPhoneName.isEmpty())
        {
            //txtSplash.setText("주소록 데이터 새로고침 중..");
            System.out.println("주소록 확인 : 없어서 채움");
            getLeadContacts();
            //txtSplash.setText("주소록 데이터 새로고침 완료");
        }
        else
        {
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    finish();
                    // txtSplash.setText("환영합니다");
                }
            };
            handler.sendEmptyMessageDelayed(0, 500);
        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(0);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
        //mHideHandler.post(mHidePart2Runnable);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
        //mHideHandler.post(mShowPart2Runnable);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
        //mHideHandler.post(mShowPart2Runnable);
    }
    private void getLeadContacts() {
        MatchCommnucation matchCommnucation = new MatchCommnucation();
        ContentResolver cr = getBaseContext().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        int phoneType = pCur.getInt(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.TYPE));
                        String phoneNumber = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        /*
                        switch (phoneType) {
                            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                Log.e(name + "(mobile number)", phoneNumber);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                Log.e(name + "(home number)", phoneNumber);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                Log.e(name + "(work number)", phoneNumber);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                                Log.e(name + "(other number)", phoneNumber);
                                break;
                            default:
                                break;
                        }*/
                        System.out.println(name + " : " + phoneNumber);
                        matchCommnucation.lionaPhoneName.add(name);
                        matchCommnucation.lionaPhoneNumber.add(phoneNumber);

                        System.out.println(matchCommnucation.lionaPhoneName.size());


                    }
                    pCur.close();
                }
            }
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    finish();
                    // txtSplash.setText("환영합니다");
                }
            };
            handler.sendEmptyMessageDelayed(0, 5000);
            //System.out.println(strContactName);
            //System.out.println(strContactNumber);
        }
        else
        {
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    finish();
                }
            };
            handler.sendEmptyMessageDelayed(0, 5000);
        }
    }
    private void setLionaDataIfNoData() {
        ArrayList<String> arrLiona = new ArrayList<String>();

        //인사 등록 시작
        arrLiona.add("안녕");
        arrLiona.add("누구야");
        arrLiona.add("이름이 뭐야");
        arrLiona.add("자기소개");
        arrLiona.add("반가워");

        String type = "일반";
        String detail = "인사";

        for (int i = 0; i < arrLiona.size(); i++) {
            String word = arrLiona.get(i);
            if (dbHelper == null) {
                dbHelper = new DBHelper(SplashscreenActivity.this, "Liona", null, 1);
            }

            LionaCommunication lionaCommunication = new LionaCommunication();
            lionaCommunication.setType(type);
            lionaCommunication.setDetail(detail);
            lionaCommunication.setWord(word);

            dbHelper.addLionaCommunication(lionaCommunication);
            //System.out.println(i);
        }
        //인사 등록 끝
    }
}

package com.lionas.ruwn.fairy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class SplashActivity extends Activity {
    private MatchCommnucation matchCommnucation = new MatchCommnucation();
    private DBHelper dbHelper;
    private TextView textSplash;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        textSplash = (TextView) findViewById(R.id.textSplash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);

        ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.WRITE_CONTACTS, android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, MODE_PRIVATE);

        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS);

        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            // 권한 없음
            Snackbar.make(getWindow().getDecorView().getRootView(), "앱을 사용하기 위해 권한이 필요합니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }else{
            // 권한 있음
            Snackbar.make(getWindow().getDecorView().getRootView(), "권한 확인이 완료되었습니다.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
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
            Snackbar.make(getWindow().getDecorView().getRootView(), "권한 확인이 완료되었습니다.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            setStartApplicationSetting();
        }
    }

    private void setStartApplicationSetting()
    {
        textSplash.setText("초기 설정 준비 중..");
        if (dbHelper == null) {
            dbHelper = new DBHelper(SplashActivity.this,"Liona",null,1);
        }
        if (matchCommnucation.lionaWords.isEmpty())
        {
            dbHelper.readLionaDatabaseCommunication();
            System.out.println("Array 값 여부 확인 : 없어서 채움");
            if (dbHelper.readLionaDatabaseCommunicationAllData()<=0)
            {
                textSplash.setText("초기 설정 준비 중..");
                setLionaDataIfNoData();
                System.out.println("DB 값 여부 확인 : 없어서 채움");
                textSplash.setText("초기 설정 준비 완료");
            }
        }
        if (matchCommnucation.lionaPhoneName.isEmpty())
        {
            textSplash.setText("주소록 데이터 새로고침 중..");
            System.out.println("주소록 확인 : 없어서 채움");
            getLeadContacts();
            textSplash.setText("주소록 데이터 새로고침 완료");
        }
        else
        {
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    finish();
                    textSplash.setText("환영합니다");

                }
            };
            handler.sendEmptyMessageDelayed(0, 3000);
        }
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
                    textSplash.setText("환영합니다");
                }
            };
            handler.sendEmptyMessageDelayed(0, 3000);
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
            handler.sendEmptyMessageDelayed(0, 3000);
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
                dbHelper = new DBHelper(SplashActivity.this, "Liona", null, 1);
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

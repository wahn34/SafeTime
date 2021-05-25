package com.lionas.ruwn.fairy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LionaSetActivity extends AppCompatActivity {
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
    public ArrayList<String> arrLocation = new ArrayList<String>();
    public ArrayList<String> arrMyHome = new ArrayList<String>();
    public ArrayList<String> arrMyList = new ArrayList<String>();
    public ArrayList<String> arrMyHomes = new ArrayList<String>();
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private LocationDBHelper locationDBHelper;
    TextView textView3;
    ProgressDialog loading;
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
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_liona_set);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        TextView txtLog = (TextView)findViewById(R.id.txtLog);
        txtLog.setText(getLionaMyID());
        try{
            ImageView imgProf = (ImageView)findViewById(R.id.imgProf);
            String imgpath = "data/data/com.lionas.ruwn.fairy/files/test.png";
            Bitmap bm = BitmapFactory.decodeFile(imgpath);
            imgProf.setImageBitmap(bm);
        }
        catch(Exception e){
            Snackbar.make(getWindow().getDecorView().getRootView(), "저장된 프로필 이미지가 없습니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        final TextView textView6 = (TextView)findViewById(R.id.textView6);
        Button btnGetHome = (Button)findViewById(R.id.btnGetHome);
        btnGetHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                readOnUserLocationDataPattern();
                for (int i = 0; i < arrLocation.size(); i ++)
                {
                    String str = arrLocation.get(i);
                    String[]array;
                    array = str.split(",");
                    getLocation(Double.parseDouble(array[0]), Double.parseDouble(array[1]));
                    //System.out.println(array[0] + " " + array[1]);
                    //System.out.println(str);
                }

                HashSet hs = new HashSet(arrMyHome);
                ArrayList<String> newArrList = new ArrayList<String>(hs);

                int tmp = 0;
                for (int i = 0; i < newArrList.size(); i ++)
                {
                    arrMyList.add("0");
                    for (int j = 0; j < arrMyHome.size(); j ++)
                    {

                        if (newArrList.get(i).contains(arrMyHome.get(j)))
                        {
                            tmp = Integer.parseInt(arrMyList.get(i));
                            tmp = tmp + 1;
                            arrMyList.set(i,tmp+"");
                        }
                        else
                        {

                        }

                    }
                    tmp = 0;
                    System.out.println(newArrList.get(i));
                }
                int siz=0;
                int shp = 0;
                for (int i = 0; i < arrMyList.size(); i++)
                {
                    if (siz < Integer.parseInt(arrMyList.get(i)))
                    {
                        siz = Integer.parseInt(arrMyList.get(i));
                        shp = i;
                    }
                }

                for (int i = 0; i < newArrList.size(); i++)
                {
                    System.out.println(newArrList.get(i) + " " + arrMyList.get(i));
                }
                textView6.setText(newArrList.get(shp));
                //Log.d("tg", arrMyHome.get(count));
                //textView6.setText(arrMyHome.get(count));
                saveMyHome(newArrList.get(shp));
                //textView6.setText(getLionaMyHome());
            }
        });
        Button btnProf = (Button)findViewById(R.id.btnProf);
        btnProf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), MyProfActivity.class);
                startActivity(intent);
            }
        });
        textView6.setText(getLionaMyHome());
        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toggle();
            }
        });
        final Switch swLock = (Switch) findViewById(R.id.swLock);

        final LionaSetData lionaSetData = new LionaSetData();
        swLock.setChecked(getLionaSettingLock());
        swLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (isChecked)
                {
                    saveOpenLock("0");
                    System.out.println("ON");
                    System.out.println(getLionaSettingLock());
                }
                else
                {
                    saveOpenLock("5");
                    System.out.println("OFF");
                    System.out.println(getLionaSettingLock());
                }
                swLock.setChecked(getLionaSettingLock());
            }
        });
        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        Toast.makeText(getBaseContext(), "resultCode : "+resultCode,Toast.LENGTH_SHORT).show();

        if(requestCode == 100)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap 	= MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    ImageView image = (ImageView)findViewById(R.id.imgProf);

                    //배치해놓은 ImageView에 set
                    image.setImageBitmap(image_bitmap);


                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();


                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    public String getImageNameToUri(Uri data)
    {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);

        return imgName;
    }

    private void saveMyHome(String strNumber){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("hm", strNumber);
        editor.commit();
    }
    private String getLionaMyHome()
    {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String str = pref.getString("hm", "");
        return str;
    }
    private String getLionaMyID()
    {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String str = pref.getString("id", "");
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
    private void saveOpenLock(String strNumber){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("lk", strNumber);
        editor.commit();
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

    public void readOnUserLocationDataPattern() {
        arrLocation.clear();

        if (locationDBHelper == null) {
            locationDBHelper = new LocationDBHelper(this, "Location", null, 1);
        }
        arrLocation = locationDBHelper.readLionaDatabaseLocationAddress();
        //locationDBHelper.readLionaDatabaseLocation();
    }

    public String getLocation(double lat, double lng){
        String str = "";
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                address = geocoder.getFromLocation(lat, lng, 1);
                if (address != null && address.size() > 0) {
                    str = address.get(0).getAddressLine(0).toString();
                }
            }
        } catch (IOException e) {
            Log.e("MainActivity", "주소를 찾지 못하였습니다.");
            e.printStackTrace();
        }
        //System.out.println(str);
        //textview1.setText(str);

        arrMyHome.add(str);
        return str;
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
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}

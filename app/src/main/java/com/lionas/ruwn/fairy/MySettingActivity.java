package com.lionas.ruwn.fairy;

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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class MySettingActivity extends Activity {
    public ArrayList<String> arrLocation = new ArrayList<String>();
    public ArrayList<String> arrMyHome = new ArrayList<String>();
    public ArrayList<String> arrMyList = new ArrayList<String>();
    private LocationDBHelper locationDBHelper;
    TextView textView3;
    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
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
}

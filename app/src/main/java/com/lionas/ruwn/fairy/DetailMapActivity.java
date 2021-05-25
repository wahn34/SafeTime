package com.lionas.ruwn.fairy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

public class DetailMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    Calendar calendar = Calendar.getInstance();
    static final LatLng SEOUL = new LatLng(37.56, 126.97);
    private GoogleMap googleMap;
    public ArrayList<String> arrDate = new ArrayList<String>();
    public ArrayList<String> arrLocation = new ArrayList<String>();
    private LocationDBHelper locationDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_map);

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                googleMap.clear();
                calendar.set(year, monthOfYear, dayOfMonth);
                //Toast.makeText(getApplicationContext(), year + "년" + monthOfYear + "월" + dayOfMonth + "일", Toast.LENGTH_SHORT).show();
                Snackbar.make(getWindow().getDecorView().getRootView(), year + "년" + monthOfYear + "월" + dayOfMonth + "일 선택되었습니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                searchSelectDateOnDatabase(year, monthOfYear, dayOfMonth);
                //MarkerOptions marker = new MarkerOptions().position(SEOUL);
            }
        };

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button btnDateSelect = (Button) findViewById(R.id.btnDateSelect);
        btnDateSelect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new DatePickerDialog(DetailMapActivity.this, dateSetListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    @Override
    public void onMapReady(final GoogleMap map) {
        //readOnUserLocationDataPattern();
        googleMap = map;
        //Marker seoul = googleMap.addMarker(new MarkerOptions().position(SEOUL).title("Seoul"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        readOnUserLocationDataPattern();
    }

    public void readOnUserLocationDataPattern() {

        if (locationDBHelper == null) {
            locationDBHelper = new LocationDBHelper(this, "Location", null, 1);
        }
        arrDate = locationDBHelper.readLionaDatabaseLocationDate();
        arrLocation = locationDBHelper.readLionaDatabaseLocationAddress();
        //locationDBHelper.readLionaDatabaseLocation();
    }

    public String getLocation(double lat, double lng){
        String str = null;
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
        System.out.println(str);
        //textview1.setText(str);
        return str;
    }

    public void searchSelectDateOnDatabase(int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Date dtSelectDate = c.getTime();

        Date dtDate = null;
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        for (int i = 0; i < arrDate.size(); i++) {
            transFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            try {
                dtDate = transFormat.parse(arrDate.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (dtDate.getDate() == dtSelectDate.getDate()) {
                System.out.println(arrLocation.get(i));
                String[] latLng = arrLocation.get(i).split(",");
                double latitude = Double.parseDouble(latLng[0]);
                double longitude = Double.parseDouble(latLng[1]);
                //TMapData tmapdata = new TMapData();

                LatLng location = new LatLng(latitude, longitude);
                Marker mark = googleMap.addMarker(new MarkerOptions().position(location).title(arrDate.get(i)));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                readOnUserLocationDataPattern();
                //getLocation(latitude, longitude);
/*
                String Address = "";
                try
                {
                    Address = tmapdata.convertGpsToAddress(latitude, longitude);
                }
                catch (IOException e)
                {

                }
                catch (SAXException e)
                {

                }
                catch (ParserConfigurationException e)
                {

                }
                System.out.println("///// : " + Address);

            }
        }
        //String from = "2013-04-08 10:10:10";
        //SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date to = transFormat.parse(from);*/
            }
        }


    }
}
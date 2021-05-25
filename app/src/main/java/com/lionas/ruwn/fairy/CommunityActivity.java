package com.lionas.ruwn.fairy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.skp.Tmap.TMapGpsManager;

import android.location.LocationListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class CommunityActivity extends AppCompatActivity implements OnMapReadyCallback, TMapGpsManager.onLocationChangedCallback {

    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "lionaid";
    private static final String TAG_LIONAX = "lionax";
    private static final String TAG_LIONAY = "lionay";
    private static final String TAG_LIONAMSG = "lionamsg";

    String myJSON;
    JSONArray lionaL = null;
    EditText editChatC;
    TextView txtChat;
    private GoogleMap googleMap;
    LocationManager mLocMgr;
    TMapGpsManager gps = null;
    LionaSetData lionaSetData = new LionaSetData();
    Double latX = 0.0;
    Double latY = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        editChatC = (EditText)findViewById(R.id.editChatC);
        Button btnChat = (Button)findViewById(R.id.btnChat);
        txtChat = (TextView) findViewById(R.id.txtChat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                if (editChatC.getText().toString().matches(""))
                {

                }
                else
                {
                    //getMyLocationOnMap();
                    double myArray[]= getMyLocation();
                    insertToDatabase(lionaSetData.getLionaID(),Double.toString(myArray[0]),Double.toString(myArray[1]),editChatC.getText().toString());

                    Snackbar.make(getWindow().getDecorView().getRootView(), "메시지를 전송했습니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    txtChat.setText(lionaSetData.getLionaID() + " : " + editChatC.getText().toString());
                    downKeyboard(getApplicationContext(),editChatC);
                    editChatC.setText("");
                    latX = myArray[0];
                    latY = myArray[1];
                }

            }
        });
        Button btnReload = (Button)findViewById(R.id.btnReload);
        btnReload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                googleMap.clear();
                getData();
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gps = new TMapGpsManager(CommunityActivity.this);
        gps.setMinTime(1);
        gps.setMinDistance(1);
        gps.setProvider(gps.NETWORK_PROVIDER);
        gps.OpenGps();

        MapsInitializer.initialize(this);


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        gps.CloseGps();
    }
    @Override
    public void onLocationChange(Location location) {
        System.out.println("Tmap : onLocationChange " + location.getLatitude() +  " " + location.getLongitude() + " " + location.getSpeed() + " " + location.getAccuracy());
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        latX = location.getLatitude();
        latY = location.getLongitude();
        googleMap.clear();
        getData();
        // 자신의 위치를 DB에 업로드
        // insertToDatabase(lionaSetData.getLionaID(),Double.toString(location.getLatitude()),Double.toString(location.getLongitude()),"");

    }
    @Override
    public void onMapReady(final GoogleMap map) {
        googleMap = map;
        double myArray[]= getMyLocation();
        LatLng latLng = new LatLng(myArray[0], myArray[1]);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }
    public static void downKeyboard(Context context, EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void insertToDatabase(String lionaid, String lionax, String lionay, String lionamsg) {
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CommunityActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    String lionid = (String) params[0];
                    String lionax = (String) params[1];
                    String lionay = (String) params[2];
                    String lionamsg = (String) params[3];

                    String link = "http://scarp.co.kr/Liona/inputmessage.html";
                    String data = URLEncoder.encode("lionaid", "UTF-8") + "=" + URLEncoder.encode(lionid, "UTF-8");
                    data += "&" + URLEncoder.encode("lionax", "UTF-8") + "=" + URLEncoder.encode(lionax, "UTF-8");
                    data += "&" + URLEncoder.encode("lionay", "UTF-8") + "=" + URLEncoder.encode(lionay, "UTF-8");
                    data += "&" + URLEncoder.encode("lionamsg", "UTF-8") + "=" + URLEncoder.encode(lionamsg, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        InsertData task = new InsertData();
        task.execute(lionaid, lionax, lionay, lionamsg);
    }
    public void getData(){
        String url = "http://scarp.co.kr/Liona/getmsg.html";
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result){
                //System.out.println("서버에서 가져옴 : "+result);
                myJSON = result;
                showMyList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    void showMyList()
    {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            lionaL = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<lionaL.length();i++){
                JSONObject c = lionaL.getJSONObject(i);
                String name = c.getString(TAG_ID);
                String locx = c.getString(TAG_LIONAX);
                String locy = c.getString(TAG_LIONAY);
                String lmsg = c.getString(TAG_LIONAMSG);

                //HashMap<String,String> persons = new HashMap<String,String>();

                //persons.put(TAG_ID,id);
                //persons.put(TAG_RESULTS,name);

                //personList.add(persons);
                //System.out.println(persons);
                System.out.println("서버 : " + name);
                System.out.println("서버 : " + locx);
                System.out.println("서버 : " + locy);
                System.out.println("서버 : " + lmsg);
                System.out.println("나는 : " + latX);
                System.out.println("나는 : " + latY);
                if ((latX >= Double.parseDouble(locx)-0.01 && latX <= Double.parseDouble(locx)+0.01) && (latY >= Double.parseDouble(locy)-0.01 && latY <= Double.parseDouble(locy)+0.01))
                {
                    System.out.println("서버 : " + name);
                    System.out.println("서버 : " + locx);
                    System.out.println("서버 : " + locy);
                    System.out.println("서버 : " + lmsg);
                    txtChat.setText(name + " : " + lmsg);

                    if (lionaSetData.getLionaID().matches(name))
                    {

                    }
                    else
                    {
                        Snackbar.make(getWindow().getDecorView().getRootView(), "새로운 메시지가 있습니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        LatLng location = new LatLng(Double.valueOf(locx).doubleValue(), Double.valueOf(locy).doubleValue());
                        Marker mark = googleMap.addMarker(new MarkerOptions().position(location).title(name+" : "+lmsg));
                    }

                }
                //LatLng location = new LatLng(Double.valueOf(locx).doubleValue(), Double.valueOf(locy).doubleValue());
                //Marker mark = googleMap.addMarker(new MarkerOptions().position(location).title(name+" : "+lmsg));
                //LatLng location = new LatLng(Double.valueOf(locx).doubleValue(), Double.valueOf(locy).doubleValue());
                //Marker mark = googleMap.addMarker(new MarkerOptions().position(location).title(name+"\n"+lmsg));
                //googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                //googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));

            }
            /*
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, personList, R.layout.list_item,
                    new String[]{TAG_ID,TAG_NAME,TAG_ADD},
                    new int[]{R.id.id, R.id.name, R.id.address}
            );

            list.setAdapter(adapter);
*/
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }
    double[] getMyLocation()
    {
        double arrDouble[] = {0, 0};
        LionaGPS lionaGPS = new LionaGPS(CommunityActivity.this);
        // GPS 사용유무 가져오기
        if (lionaGPS.isGetLocation()) {

            double latitude = lionaGPS.getLatitude();
            double longitude = lionaGPS.getLongitude();
            arrDouble[0] = latitude;
            arrDouble[1] = longitude;

        } else {
            // GPS 를 사용할수 없으므로
            //lionaGPS.showSettingsAlert();
        }
        return arrDouble;
    }

}

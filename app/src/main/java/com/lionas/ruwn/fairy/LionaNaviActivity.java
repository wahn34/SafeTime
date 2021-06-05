package com.lionas.ruwn.fairy;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skp.Tmap.TMapCircle;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.LogManager;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class LionaNaviActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {
    private TMapView mMapView = null;
    private int m_nCurrentZoomLevel = 0;
    private double m_Latitude = 0;
    private double m_Longitude = 0;
    private boolean m_bShowMapIcon = false;

    private boolean m_bTrafficeMode = false;
    private boolean m_bSightVisible = false;
    private boolean m_bTrackingMode = false;

    private boolean m_bOverlayMode = false;

    TMapGpsManager gps = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_liona_navi);
        mMapView = new TMapView(this);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        mMapView.setSKPMapApiKey("");
        mMapView.setCompassMode(false);
        mMapView.setIconVisibility(true);
        mMapView.setZoomLevel(15);
        mMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        mMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        mMapView.setTrackingMode(true);
        mMapView.setSightVisible(true);

        gps = new TMapGpsManager(LionaNaviActivity.this);
        gps.setMinTime(1);
        gps.setMinDistance(1);
        gps.setProvider(gps.NETWORK_PROVIDER);
        gps.OpenGps();

        relativeLayout.addView(mMapView);
        setContentView(relativeLayout);
        //drawPedestrianPath();
        findMyLocationAddress();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gps.CloseGps();

    }

    public TMapPoint randomTMapPoint() {
        double latitude = ((double) Math.random()) * (37.575113 - 37.483086) + 37.483086;
        double longitude = ((double) Math.random()) * (127.027359 - 126.878357) + 126.878357;

        latitude = Math.min(37.575113, latitude);
        latitude = Math.max(37.483086, latitude);

        longitude = Math.min(127.027359, longitude);
        longitude = Math.max(126.878357, longitude);

        //LogManager.printLog("randomTMapPoint" + latitude + " " + longitude);

        TMapPoint point = new TMapPoint(latitude, longitude);

        return point;
    }

    public void setTrackingMode() {
        m_bTrackingMode = !m_bTrackingMode;
        mMapView.setTrackingMode(m_bTrackingMode);
    }

    public void findMyLocationAddress() {
        LionaGPS lionaGPS = new LionaGPS(LionaNaviActivity.this);
        // GPS 사용유무 가져오기
        if (lionaGPS.isGetLocation()) {

            double latitude = lionaGPS.getLatitude();
            double longitude = lionaGPS.getLongitude();
            //Toast.makeText(getApplicationContext(), "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude, Toast.LENGTH_LONG).show();
            drawPedestrianPathToMyHome(latitude, longitude);

            mMapView.setLocationPoint(longitude, latitude);
            setTrackingMode();
            naviGuide(latitude, longitude);

        } else {
            // GPS 를 사용할수 없으므로
            //lionaGPS.showSettingsAlert();
        }
    }


    public void naviGuide(double latitude, double longitude) {
        TMapPoint point1 = new TMapPoint(latitude, longitude);
        TMapPoint point2 = new  TMapPoint(37.480073, 127.058114);

        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataAll(point1, point2, new TMapData.FindPathDataAllListenerCallback() {
        /*
        tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, point1, point2, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine tMapPolyLine) {

            }
*/
            @Override
            public void onFindPathDataAll(Document doc) {
                //LogManager.printLog("onFindPathDataAll: " + doc);
                System.out.println("경로 찾기 완료 : " +doc);
                //LogManager.printLog("onFindPathDataAll: " + doc);

                try{
                    DocumentBuilderFactory factory  =  DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder    =  factory.newDocumentBuilder();
                    Document document     =  doc;
                    NodeList nodelist     =  document.getElementsByTagName("tmap:totalTime");

                    NodeList nodeLength     =  document.getElementsByTagName("tmap:totalDistance");
                    //태그 (< >)의 이름으로 불러오는 내용
                    //nodelist의 크기를 구하려면 getLength()라는 메소드가 있음
                    Node node       =  nodelist.item(0);//첫번째 element 얻기
                    Node node2       =  nodeLength.item(0);//첫번째 element 얻기
                    Node textNode      =  nodelist.item(0).getChildNodes().item(0);
                    Node textNode2      =  nodeLength.item(0).getChildNodes().item(0);
                    //element의 text 얻기
                    System.out.println("경로찾기!!" + textNode.getNodeValue());
                    //자체 계산법 적용_TEST
                    int i =(int)(Integer.parseInt(textNode2.getNodeValue())/1.38)+1;
                    if (i>60)
                    {
                        Snackbar.make(getWindow().getDecorView().getRootView(), i/60+"분, " + i%60+"초 후 도착, " + "남은 거리 : " + textNode.getNodeValue() + "m", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                    else Snackbar.make(getWindow().getDecorView().getRootView(), i+"초 후 도착, " +"남은 거리 : " + textNode2.getNodeValue() + "m", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    /*
                    if (Integer.parseInt(textNode.getNodeValue())>60)
                    {
                        Snackbar.make(getWindow().getDecorView().getRootView(), Integer.parseInt(textNode.getNodeValue())/60+"분, " + Integer.parseInt(textNode.getNodeValue())%60+"초 후 도착, " + "남은 거리 : " + textNode.getNodeValue() + "m", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                    else Snackbar.make(getWindow().getDecorView().getRootView(), Integer.parseInt(textNode.getNodeValue())+"초 후 도착, " +"남은 거리 : " + textNode2.getNodeValue() + "m", Snackbar.LENGTH_LONG).setAction("Action", null).show();*/
                }catch(Exception e){
                    e.printStackTrace();
                    Snackbar.make(getWindow().getDecorView().getRootView(), "경로안내에 문제가 생겼습니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
    }

    public void drawMapPath() {
        TMapPoint point1 = mMapView.getCenterPoint();
        TMapPoint point2 = randomTMapPoint();

        TMapData tmapdata = new TMapData();

        tmapdata.findPathData(point1, point2, new TMapData.FindPathDataListenerCallback() {

            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                mMapView.addTMapPath(polyLine);
            }
        });
    }

    public void removeMapPath() {
        mMapView.removeTMapPath();
    }

    public void drawCarPath() {
        TMapPoint point1 = mMapView.getCenterPoint();
        TMapPoint point2 = randomTMapPoint();

        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, point1, point2, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                mMapView.addTMapPath(polyLine);
            }
        });
    }

    @Override
    public void onLocationChange(Location location) {
        //LogManager.printLog("onLocationChange " + location.getLatitude() +  " " + location.getLongitude() + " " + location.getSpeed() + " " + location.getAccuracy());

        System.out.println("Tmap : onLocationChange " + location.getLatitude() +  " " + location.getLongitude() + " " + location.getSpeed() + " " + location.getAccuracy());
        mMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        setTrackingMode();
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        //setTrackingMode();
        removeMapPath();
        drawPedestrianPathToMyHome(lat, lon);
        naviGuide(lat, lon);
    }

    public void drawPedestrianPath() {
        TMapPoint point1 = mMapView.getCenterPoint();
        TMapPoint point2 = randomTMapPoint();

        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, point1, point2, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                polyLine.setLineColor(Color.BLUE);
                mMapView.addTMapPath(polyLine);
            }
        });
    }

    public void drawPedestrianPathToMyHome(Double lat, Double lon) {
        TMapPoint point1 = new TMapPoint(lat, lon);
        TMapPoint point2 = new TMapPoint(37.480073, 127.058114);

        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, point1, point2, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                polyLine.setLineColor(Color.BLUE);
                mMapView.addTMapPath(polyLine);
            }
        });
    }
    public void drawBicyclePath() {
        TMapPoint point1 = mMapView.getCenterPoint();
        TMapPoint point2 = randomTMapPoint();

        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataWithType(TMapData.TMapPathType.BICYCLE_PATH, point1, point2, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                mMapView.addTMapPath(polyLine);
            }
        });
    }

    public void captureImage() {
        mMapView.getCaptureImage(20, new TMapView.MapCaptureImageListenerCallback() {

            @Override
            public void onMapCaptureImage(Bitmap bitmap) {

                String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();

                File path = new File(sdcard + File.separator + "image_write");
                if (!path.exists())
                    path.mkdir();

                File fileCacheItem = new File(path.toString() + File.separator + System.currentTimeMillis() + ".png");
                OutputStream out = null;

                try {
                    fileCacheItem.createNewFile();
                    out = new FileOutputStream(fileCacheItem);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

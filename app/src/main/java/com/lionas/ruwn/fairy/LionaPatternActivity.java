package com.lionas.ruwn.fairy;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class LionaPatternActivity extends AppCompatActivity {
    int myArray[]= {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    int myArray2[]= {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    public ArrayList<String> arrPatternTime = new ArrayList<String>();
    public ArrayList<String> arrPatternAction = new ArrayList<String>();
    TextView txtTime;
    TextView txtCondi;
    TextView txtReport;

    private PatternDBHelper patternDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liona_pattern);
        txtTime = (TextView)findViewById(R.id.txtTime);
        txtCondi = (TextView)findViewById(R.id.txtCondi);
        txtReport = (TextView)findViewById(R.id.txtReport);
        readOnUserActionDataPattern();
        //searchSelectDateOnDatabase();
        searchSelectDataOnDatabaseLiona();
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(1, myArray[0]),
                new DataPoint(2, myArray[1]),
                new DataPoint(3, myArray[2]),
                new DataPoint(4, myArray[3]),
                new DataPoint(5, myArray[4]),
                new DataPoint(6, myArray[5]),
                new DataPoint(7, myArray[6]),
                new DataPoint(8, myArray[7]),
                new DataPoint(9, myArray[8]),
                new DataPoint(10, myArray[9]),
                new DataPoint(11, myArray[10]),
                new DataPoint(12, myArray[11]),
                new DataPoint(13, myArray[12]),
                new DataPoint(14, myArray[13]),
                new DataPoint(15, myArray[14]),
                new DataPoint(16, myArray[15]),
                new DataPoint(17, myArray[16]),
                new DataPoint(18, myArray[17]),
                new DataPoint(19, myArray[18]),
                new DataPoint(20, myArray[19]),
                new DataPoint(21, myArray[20]),
                new DataPoint(22, myArray[21]),
                new DataPoint(23, myArray[22]),
                new DataPoint(24, myArray[23])
        });
        graph.addSeries(series);

        GraphView graphView2 = (GraphView) findViewById(R.id.graphView2);
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(1, myArray2[0]),
                new DataPoint(2, myArray2[1]),
                new DataPoint(3, myArray2[2]),
                new DataPoint(4, myArray2[3]),
                new DataPoint(5, myArray2[4]),
                new DataPoint(6, myArray2[5]),
                new DataPoint(7, myArray2[6]),
                new DataPoint(8, myArray2[7]),
                new DataPoint(9, myArray2[8]),
                new DataPoint(10, myArray2[9]),
                new DataPoint(11, myArray2[10]),
                new DataPoint(12, myArray2[11]),
                new DataPoint(13, myArray2[12]),
                new DataPoint(14, myArray2[13]),
                new DataPoint(15, myArray2[14]),
                new DataPoint(16, myArray2[15]),
                new DataPoint(17, myArray2[16]),
                new DataPoint(18, myArray2[17]),
                new DataPoint(19, myArray2[18]),
                new DataPoint(20, myArray2[19]),
                new DataPoint(21, myArray2[20]),
                new DataPoint(22, myArray2[21]),
                new DataPoint(23, myArray2[22]),
                new DataPoint(24, myArray2[23])
        });
        graphView2.addSeries(series2);

        String strz = "";
        int ds = 0;
        for (int i = 0; i < 24; i++)
        {
            ds = ds + myArray[i];
        }
        //ds = ds/24/2;
        System.out.println(ds + " " + ds/24/2);
        Boolean isSleep = false;
        ds = ds/24/2;
        int tmpstart = 0;
        int tmpend = 0;
        for (int i = 0; i < 24; i++)
        {
            if (myArray[i] <= ds)
            {
                isSleep = true;
                strz = strz + "예상 수면 시간 " + i + "시";
            }
            else
                isSleep = false;
        }
        System.out.println(strz);

        report();

    }

    public void report ()
    {
        int max = 0;
        int count = 0;
        int min = 0;
        /*
        for (int i = 0; i < 12; i ++)
        {
            count += myArray[i];
        }
        count = count/12;
        for (int i = 0; i < 12; i ++)
        {
            if (myArray[i]>count) max = i;
        }
        count = 0;
        for (int i = 21; i < 24; i ++)
        {
            count += myArray[i];
        }
        count = count/4;
        for (int i = 21; i < 24; i ++)
        {
            if (myArray[i]>count) min = i;
        }
        count = 0;
        count = max + (24-min);

        for (int i = 0; i < 12; i++)
        txtTime.setText("약 " + count +"시간");
        if (count > 7)
        {
            txtCondi.setText("정상");
            txtCondi.setTextColor(Color.GREEN);
        }
        else if (count > 5)
        {
            txtCondi.setText("조금 피곤");
            txtCondi.setTextColor(Color.BLUE);
        }
        else if (count > 3)
        {
            txtCondi.setText("다소 피곤");
            txtCondi.setTextColor(Color.YELLOW);
        }
        else
        {
            txtCondi.setText("수면 부족");
            txtCondi.setTextColor(Color.RED);
        }

        */
        int rep[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        min = 100;
        max = myArray[1];
        int minCt = 0;
        int maxCt = 0;
        //21, 22, 23, 24, 01, 02, 03, 04, 05, 06, 07, 08, 09, 10
        //1   2   3   4   5   6   7   8   9   10  11  12  13  14
        for (int i = 1; i<10; i++)
        {
            if (min>myArray[i-1])
            {
                min = myArray[i-1];
                minCt = i-1;
            }
            if (max<myArray[i-1])
            {
                max = myArray[i-1];
                maxCt = i-1;
            }
        }
        for (int i = 21; i < 24; i ++)
        {
            if (min>myArray[i-1])
            {
                min = myArray[i-1];
                minCt = i-1;
            }
            if (max<myArray[i-1])
            {
                max = myArray[i-1];
                maxCt = i-1;
            }
        }
        if (maxCt>10)
        {
            for (int i = 1; i<10; i++)
            {
                if (min<myArray[i-1])
                {
                    max = myArray[i-1];
                    maxCt = i-1;
                }
            }
        }
        if (minCt > 20)
        {
            minCt = 24-minCt;
            count = minCt + maxCt;
        }
        else count = maxCt-minCt;


        System.out.println("보고서");
        System.out.println("예상 기상시간 : " + maxCt);
        System.out.println("예상 수면시간 : " + minCt);
        System.out.println("평균 값 : " + count);
        txtTime.setText("약 " + count +"시간");
        txtReport.setText("약 " + minCt+"시 부터 " + maxCt+"시까지");
        if (count > 7)
        {
            txtCondi.setText("정상");
            txtCondi.setTextColor(Color.GREEN);
        }
        else if (count > 5)
        {
            txtCondi.setText("조금 피곤");
            txtCondi.setTextColor(Color.BLUE);
        }
        else if (count > 3)
        {
            txtCondi.setText("다소 피곤");
            txtCondi.setTextColor(Color.YELLOW);
        }
        else
        {
            txtCondi.setText("수면 부족");
            txtCondi.setTextColor(Color.RED);
        }
    }
    public void searchSelectDataOnDatabaseLiona()
    {
        String strText = "";
        for (int i = 0; i < myArray.length; i++)
        {
            myArray[i] = 0;
        }

        //어제
        Calendar calendar1 = new GregorianCalendar();
        calendar1.add(Calendar.DATE, -1);
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String yesterday = dt1.format(calendar1.getTime());
        Date dtYesterday = null;
        int tmp = 0;
        int tmp2 = 0;
        try {
            dtYesterday = dt1.parse(yesterday);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //오늘
        String nowTime = dt1.format(new Date(System.currentTimeMillis()));
        Date dtToday = null;
        try {
            dtToday = dt1.parse(yesterday);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //데이터베이스 날짜
        Date dtDate = null;
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        for (int i = 0; i < arrPatternTime.size(); i++) {
            try {
                dtDate = transFormat.parse(arrPatternTime.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (dtDate.getDate() == dtToday.getDate())
            {
                //오늘 날짜일 경우
                try {
                    DateFormat sdFormat = new SimpleDateFormat("HH");
                    String tempDate = sdFormat.format(dtDate);
                    tmp = Integer.parseInt(tempDate);

                } catch (Exception e) {
                    //e.printStackTrace();
                    tmp = 25;
                }
                if (tmp <= 9) myArray[tmp] = myArray[tmp] + 1;
            }

            if (dtDate.getDate() == dtYesterday.getDate())
            {
                //어제 날짜일 경우
                try {
                    DateFormat sdFormat = new SimpleDateFormat("HH");
                    String tempDate = sdFormat.format(dtDate);
                    tmp = Integer.parseInt(tempDate);

                } catch (Exception e) {
                    //e.printStackTrace();
                    tmp = 25;
                }
                if (tmp > 9) myArray[tmp] = myArray[tmp] + 1;
            }
            //전체 시간
            try {
                DateFormat sdFormat = new SimpleDateFormat("HH");
                String tempDate = sdFormat.format(dtDate);
                tmp2 = Integer.parseInt(tempDate);

            } catch (Exception e) {
                //e.printStackTrace();
                tmp2 = 25;
            }
            myArray2[tmp2] = myArray2[tmp2] + 1;
        }
        for (int i = 0; i < 24; i ++)
        {
            System.out.println(i + "시 사용 빈도 : " + myArray[i]);
            strText = String.format(strText + i + "시 사용 빈도 : " + myArray[i] + "\n");
        }
        //txtPatternView.setText(strText);
    }
    public void searchSelectDateOnDatabase()
    {
        for (int i = 0; i < myArray.length; i++)
        {
            myArray[i] = 0;
        }
        String strText = "";
        Date dtDate = null;
        Date todDate = null;
        int tmp = 0;
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String nowTime = sdfNow.format(new Date(System.currentTimeMillis()));

        //어제
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.add(cal.DATE, -1);
        String yesterday = date.format(cal.getTime());

        //오늘
        nowTime = sdfNow.format(new Date(System.currentTimeMillis()));

        try {
            todDate = transFormat.parse(nowTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        transFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        for (int i = 0; i < arrPatternTime.size(); i++) {

            try {
                dtDate = transFormat.parse(arrPatternTime.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                DateFormat sdFormat = new SimpleDateFormat("HH");
                String tempDate = sdFormat.format(dtDate);
                tmp = Integer.parseInt(tempDate);

            } catch (Exception e) {
                //e.printStackTrace();
                tmp = 25;
            }
            myArray[tmp] = myArray[tmp] + 1;
        }
        for (int i = 0; i < 24; i ++)
        {
            System.out.println(i + "시 사용 빈도 : " + myArray[i]);
            strText = String.format(strText + i + "시 사용 빈도 : " + myArray[i] + "\n");
        }
        //txtPatternView.setText(strText);
    }

    public void test()
    {
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time = sdfNow.format(new Date(System.currentTimeMillis()));
    }

    private void readOnUserActionDataPattern() {
        if (patternDBHelper == null) {
            patternDBHelper = new PatternDBHelper(this, "Pattern", null, 1);
        }
        arrPatternTime = patternDBHelper.readLionaDatabasePatternTime();
        arrPatternAction = patternDBHelper.readLionaDatabasePatternAction();
    }
}

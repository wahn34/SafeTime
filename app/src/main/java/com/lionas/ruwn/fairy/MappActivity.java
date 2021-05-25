package com.lionas.ruwn.fairy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import java.util.Date;
import java.util.*;
import android.util.*;
import android.view.*;
import com.lionas.ruwn.fairy.DBHelper;


public class MappActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapp);
        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "MyMaps.db",null,1);

        final TextView tResult = (TextView) findViewById(R.id.txtResult);

        final EditText eTitle = (EditText) findViewById(R.id.txtTitle);
        final EditText eMemo = (EditText) findViewById(R.id.txtMemo);
        final EditText eMapsx = (EditText) findViewById(R.id.txtMapx);
        final EditText eMapsy = (EditText) findViewById(R.id.txtMapy);

        // 날짜는 현재 날짜로 고정
        // 현재 시간 구하기
        //long now = System.currentTimeMillis();
        //Date date = new Date(now);
        // 출력될 포맷 설정
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        //eTitle.setText(simpleDateFormat.format(date));



    }
}

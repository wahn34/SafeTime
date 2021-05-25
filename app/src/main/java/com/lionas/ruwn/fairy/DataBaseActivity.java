package com.lionas.ruwn.fairy;

import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class DataBaseActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);

        final ListView lstLiona = (ListView) findViewById(R.id.lstLiona);
        Button btnSave1 =  (Button) findViewById(R.id.btnSave1);
        btnSave1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                final EditText etDBType = new EditText(DataBaseActivity.this);
                etDBType.setHint("DB명을 입력");
                AlertDialog.Builder dialog = new AlertDialog.Builder(DataBaseActivity.this);
                dialog.setTitle("Database 이름을 입력하세요")
                        .setMessage("Database  이름을 입력하세요")
                        .setView(etDBType)


                        .setPositiveButton("생성", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (etDBType.getText().toString().length() > 0) {
                                    dbHelper = new DBHelper(
                                            DataBaseActivity.this,
                                            etDBType.getText().toString(), null, 1);
                                    dbHelper.testDB();
                                }
                            }
                        })
                        .setNeutralButton("취소",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();
            }
        });

        Button loadData =  (Button) findViewById(R.id.loadData);
        loadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                lstLiona.setVisibility(View.VISIBLE);
                if (dbHelper == null) {
                    dbHelper = new DBHelper(DataBaseActivity.this,"Liona",null,1);
                }

                List lionaCommunication = dbHelper.getAllLionaData();
                lstLiona.setAdapter(new LionaComAdapter(lionaCommunication, DataBaseActivity.this));

            }
        });

        Button createData =  (Button) findViewById(R.id.createData);
        createData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout layout = new LinearLayout(DataBaseActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText etType = new EditText(DataBaseActivity.this);
                etType.setHint("타입을 입력해주세요.");

                final EditText etDetail = new EditText(DataBaseActivity.this);
                etDetail.setHint("디테일을 입력해주세요");

                final EditText etWord = new EditText(DataBaseActivity.this);
                etWord.setHint("단어를 입력해주세요요.");

                layout.addView(etType);
                layout.addView(etDetail);
                layout.addView(etWord);

                AlertDialog.Builder dialog = new AlertDialog.Builder(DataBaseActivity.this);
                dialog.setTitle("정보를 입력하세요")
                        .setView(layout)
                        .setPositiveButton("등록", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String type = etType.getText().toString();
                                String detail = etDetail.getText().toString();
                                String word = etWord.getText().toString();

                                if (dbHelper == null) {
                                    dbHelper = new DBHelper(DataBaseActivity.this, "Liona", null, 1);
                                }

                                LionaCommunication lionaCommunication = new LionaCommunication();
                                lionaCommunication.setType(type);
                                lionaCommunication.setDetail(detail);
                                lionaCommunication.setWord(word);

                                dbHelper.addLionaCommunication(lionaCommunication);
                            }
                        })
                        .setNeutralButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();
           }
        });
    }
}

package com.lionas.ruwn.fairy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG_RESULTS="result";
    private static final String TAG_NAME = "name";
    private static final String TAG_PASS = "pass";
    String myJSON;
    JSONArray lionaL = null;
    EditText editLionaId;
    EditText editLionaPw;
    int chk = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editLionaId = (EditText) findViewById(R.id.editLionaId);
        editLionaPw = (EditText) findViewById(R.id.editLionaPw);
        Button btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
                startActivity(intent);
            }
        });
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                getData();
            }
        });
    }
    public void getData(){
        String url = "http://scarp.co.kr/Liona/getlog.html";
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
        chk = 0;
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            lionaL = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<lionaL.length();i++){
                JSONObject c = lionaL.getJSONObject(i);
                String name = c.getString(TAG_NAME);
                String pass = c.getString(TAG_PASS);

                //HashMap<String,String> persons = new HashMap<String,String>();

                //persons.put(TAG_ID,id);
                //persons.put(TAG_RESULTS,name);

                //personList.add(persons);
                //System.out.println(persons);

                System.out.println("서버 : " + name);
                if (editLionaId.getText().toString().matches(name) && editLionaPw.getText().toString().matches(pass))
                    chk = 1;
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
        if (chk != 0) {
            Snackbar.make(getWindow().getDecorView().getRootView(), "로그인 완료", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            saveMyID(editLionaId.getText().toString());
            saveMyPW(editLionaPw.getText().toString());
            finish();
        }
        else
            Snackbar.make(getWindow().getDecorView().getRootView(), "아이디와 비밀번호를 확인해주세요", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }
    private void saveMyID(String strID){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("id", strID);
        editor.commit();
    }
    private void saveMyPW(String strPW){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("pw", strPW);
        editor.commit();
    }
}

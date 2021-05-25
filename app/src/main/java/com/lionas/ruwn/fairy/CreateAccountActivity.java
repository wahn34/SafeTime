package com.lionas.ruwn.fairy;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

public class CreateAccountActivity extends AppCompatActivity {
    private static final String TAG_RESULTS="result";
    private static final String TAG_NAME = "request";
    String myJSON;
    JSONArray lionaL = null;
    int chk = 0;
    EditText editCID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        final EditText editCBirth = (EditText) findViewById(R.id.editCBirth);
        editCID = (EditText) findViewById(R.id.editCID);
        final EditText editCIntro = (EditText) findViewById(R.id.editCIntro);
        final EditText editCName = (EditText) findViewById(R.id.editCName);
        final EditText editCPW1 = (EditText) findViewById(R.id.editCPW1);
        final EditText editCPW2 = (EditText) findViewById(R.id.editCPW2);
        final RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);


        Button btnChkAcc = (Button) findViewById(R.id.btnChkAcc);
        btnChkAcc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                getData();
                if (chk!=0)
                {
                    editCID.setText("");
                    Snackbar.make(getWindow().getDecorView().getRootView(), "이미 있는 ID입니다. 다른 ID를 입력해주세요.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else
                    Snackbar.make(getWindow().getDecorView().getRootView(), "사용 가능한 ID입니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        Button btnCreateAcc = (Button) findViewById(R.id.btnCreateAcc);
        btnCreateAcc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                getData();
                if (chk!=0)
                {
                    editCID.setText("");
                    Snackbar.make(getWindow().getDecorView().getRootView(), "이미 있는 ID입니다. 다른 ID를 입력해주세요.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else
                {
                    if (editCPW1.getText().toString().matches(editCPW2.getText().toString()))
                    {
                        RadioButton rd = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
                        String str_Qtype = rd.getText().toString();
                        insertToDatabase("0", editCID.getText().toString(), editCPW1.getText().toString(), editCName.getText().toString(), editCBirth.getText().toString(), str_Qtype, editCIntro.getText().toString());
                    }
                    else
                    {
                        editCPW1.setText("");
                        editCPW2.setText("");
                        Snackbar.make(getWindow().getDecorView().getRootView(), "비밀번호가 일치하지 않습니다..", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
            }
        });
    }

    private void insertToDatabase(String lionaSet, String lionaId, String lionaPw, String lionaName, String lionaBirth, String lionaGender, String lionaIntro) {

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CreateAccountActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                Snackbar.make(getWindow().getDecorView().getRootView(), "회원가입이 완료되었습니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                finish();
            }


            @Override
            protected String doInBackground(String... params) {

                try {
                    String lionaSet = (String) params[0];
                    String lionaId = (String) params[1];
                    String lionaPw = (String) params[2];
                    String lionaName = (String) params[3];
                    String lionaBirth = (String) params[4];
                    String lionaGender = (String) params[5];
                    String lionaIntro = (String) params[6];

                    String link = "http://scarp.co.kr/Liona/inputid.html";
                    String data = URLEncoder.encode("lionaset", "UTF-8") + "=" + URLEncoder.encode(lionaSet, "UTF-8");
                    data += "&" + URLEncoder.encode("lionaid", "UTF-8") + "=" + URLEncoder.encode(lionaId, "UTF-8");
                    data += "&" + URLEncoder.encode("lionapw", "UTF-8") + "=" + URLEncoder.encode(lionaPw, "UTF-8");
                    data += "&" + URLEncoder.encode("lionaname", "UTF-8") + "=" + URLEncoder.encode(lionaName, "UTF-8");
                    data += "&" + URLEncoder.encode("lionabirth", "UTF-8") + "=" + URLEncoder.encode(lionaBirth, "UTF-8");
                    data += "&" + URLEncoder.encode("lionagender", "UTF-8") + "=" + URLEncoder.encode(lionaGender, "UTF-8");
                    data += "&" + URLEncoder.encode("lionaintro", "UTF-8") + "=" + URLEncoder.encode(lionaIntro, "UTF-8");

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
        task.execute(lionaSet, lionaId, lionaPw, lionaName, lionaBirth, lionaGender, lionaIntro);
    }

    public void getData(){
        String url = "http://scarp.co.kr/Liona/getid.html";
        class GetDataJSON extends AsyncTask<String, Void, String>{

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
                System.out.println("서버에서 가져옴 : "+result);
                myJSON = result;
                showMyList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    void showMyList()
    {
        chk=0;
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            lionaL = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<lionaL.length();i++){
                JSONObject c = lionaL.getJSONObject(i);
                String name = c.getString(TAG_NAME);

                //HashMap<String,String> persons = new HashMap<String,String>();

                //persons.put(TAG_ID,id);
                //persons.put(TAG_RESULTS,name);

                //personList.add(persons);
                //System.out.println(persons);

                System.out.println("서버 : " + name);
                if (editCID.getText().toString().matches(name))
                {
                    chk = 1;
                }
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
}
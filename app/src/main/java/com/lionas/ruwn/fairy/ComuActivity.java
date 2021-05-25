package com.lionas.ruwn.fairy;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.widget.Toast;
import android.view.View;
public class ComuActivity extends AppCompatActivity implements OnInitListener{
    View view;
    private TextToSpeech myTTS;
    MatchCommnucation matchCommnucation = new MatchCommnucation();
    private DBHelper dbHelper;

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText eCom;

    private boolean side = false;

    SpeechRecognizer recognizer;
    String logOutput;
    Boolean isSpeach = false;
    String result = "";
    String s = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comu);
        Intent i = getIntent();

        listView = (ListView) findViewById(R.id.listMessage);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage);
        listView.setAdapter(chatArrayAdapter);
        init();
        eCom = (EditText) findViewById(R.id.eCom);

        myTTS = new TextToSpeech(this, this);

        final Button insert = (Button) findViewById(R.id.btnSend);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s = "";
                result = "";
                if (eCom.getText().toString().isEmpty())
                {
                    if (isSpeach)
                    {
                        insert.setBackgroundResource(R.drawable.btn_record1);
                        stopRecognizer();
                    }
                    else
                    {
                        insert.setBackgroundResource(R.drawable.btn_record2);
                        startRecognizer();
                    }
                }
                else
                {
                    s = eCom.getText().toString();
                    sendChatMessage();
                    chkUserMesasgeOnLionaCommunication();
                    eCom.setText("");
                }
            }
        });
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
        insert.setBackgroundResource(R.drawable.btn_message);
    }

    private void insertToDatabase(String name, String address) {

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ComuActivity.this, "Please Wait", null, true, true);
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
                    String name = (String) params[0];
                    String address = (String) params[1];

                    String link = "http://scarp.co.kr/Liona/inputai.html";
                    String data = URLEncoder.encode("lionaid ", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("lionarequest", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8");
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
        task.execute(name, address);
    }
    private void setLionaDataIfNoData(){
        ArrayList<String> arrLiona = new ArrayList<String>();

        //인사 등록 시작
        arrLiona.add("안녕");
        arrLiona.add("누구야");
        arrLiona.add("이름이 뭐야");
        arrLiona.add("자기소개");
        arrLiona.add("반가워");

        String type = "일반";
        String detail = "인사";

        for (int i = 0; i < arrLiona.size(); i++) {
            String word = arrLiona.get(i);
            if (dbHelper == null) {
                dbHelper = new DBHelper(ComuActivity.this, "Liona", null, 1);
            }

            LionaCommunication lionaCommunication = new LionaCommunication();
            lionaCommunication.setType(type);
            lionaCommunication.setDetail(detail);
            lionaCommunication.setWord(word);

            dbHelper.addLionaCommunication(lionaCommunication);
            //System.out.println(i);
        }
        //인사 등록 끝
    }

    private void chkUserMesasgeOnLionaCommunication()
    {
        String[]array;
        array = s.split(" ");
        if(matchCommnucation.ifTryLiona == 0)
        {
            result = matchCommnucation.syncSentence(array);
            if (dbHelper == null) {
                dbHelper = new DBHelper(ComuActivity.this,"Liona",null,1);
            }
            if (matchCommnucation.lionaWords.isEmpty())
            {
                dbHelper.readLionaDatabaseCommunication();
                System.out.println("Array 값 여부 확인 : 없어서 채움");
                if (dbHelper.readLionaDatabaseCommunicationAllData()<=0)
                {
                    setLionaDataIfNoData();
                    System.out.println("DB 값 여부 확인 : 없어서 채움");
                }
            }
            if (matchCommnucation.lionaPhoneName.isEmpty())
            {
                System.out.println("주소록 확인 : 없어서 채움");
                getLeadContacts();
            }
        }
        else if(matchCommnucation.ifTryLiona == 1)
        {
            result = matchCommnucation.ifTrySendMessage(array);
        }
        else if(matchCommnucation.ifTryLiona == 2)
        {
            result = matchCommnucation.ifTryCallPhone(array);
        }
        System.out.println(result);
        myTTS.speak(result, TextToSpeech.QUEUE_FLUSH, null);
        receiveChatMessage(result);
    }
    /*
    private void receiveMessageByUser(ArrayList<String> arrStrMessage)
    {
        if (arrStrMessage != null) {
            //System.out.println("결과물: " + arrStrMessage.size());
            //for (int i = 0; i < arrStrMessage.size(); i++) {
            //    String aItem = arrStrMessage.get(i);
            //    System.out.println("이건뭐야" + i + " : " + aItem);
            //}
        }
        else arrStrMessage.set(0,"음성 인식 결과 없음");
        sendChatMessage(arrStrMessage.get(0));
    }*/
    private boolean receiveChatMessage(String strText){
        chatArrayAdapter.add(new ChatMessage(side, strText));
        side = !side;
        return true;
    }

    private boolean sendChatMessage(){
        chatArrayAdapter.add(new ChatMessage(side, eCom.getText().toString()));
        //eCom.setText("");
        side = !side;
        insertToDatabase("",eCom.getText().toString());
        return true;
    }
    private boolean sendChatMessage(String strMessage){
        chatArrayAdapter.add(new ChatMessage(side, strMessage));
        //eCom.setText("");
        side = !side;
        insertToDatabase("",strMessage);
        return true;
    }


    private void getLeadContacts() {
        MatchCommnucation matchCommnucation = new MatchCommnucation();
        ContentResolver cr = getBaseContext().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        int phoneType = pCur.getInt(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.TYPE));
                        String phoneNumber = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        System.out.println(name + " : " + phoneNumber);
                        matchCommnucation.lionaPhoneName.add(name);
                        matchCommnucation.lionaPhoneNumber.add(phoneNumber);

                        System.out.println(matchCommnucation.lionaPhoneName.size());


                    }
                    pCur.close();
                }
            }
        }
    }

    private void init() {
        recognizer = SpeechRecognizer.createSpeechRecognizer(this);
        recognizer.setRecognitionListener(new VoiceRecognitionListener());

    }

    private void startRecognizer() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                "org.androidtown.voice.recognizer");
        recognizer.startListening(intent);
        isSpeach = true;

    }

    public void onInit(int status) {

    }
    private void stopRecognizer() {
        recognizer.stopListening();
        isSpeach = false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        myTTS.shutdown();
        recognizer.cancel();
        recognizer.destroy();
    }
    public static void dumpArray(String[] array) {
        for (int i = 0; i < array.length; i++)
            System.out.format("array[%d] = %s%n", i, array[i]);
    }


    class VoiceRecognitionListener implements RecognitionListener {

        @Override
        public void onBeginningOfSpeech() {
            //Toast.makeText(getApplicationContext(), "인식 전.", Toast.LENGTH_SHORT).show();
            Snackbar.make(getWindow().getDecorView().getRootView(), "음성 인식 준비가 되었습니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {
            //Toast.makeText(getApplicationContext(), "인식 완료", Toast.LENGTH_SHORT).show();
            Snackbar.make(getWindow().getDecorView().getRootView(), "음성 인식이 완료되었습니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

        @Override
        public void onError(int error) {
            //Toast.makeText(getApplicationContext(), "오류 발생", Toast.LENGTH_SHORT).show();
            Snackbar.make(getWindow().getDecorView().getRootView(), "음성 인식 도중 오류가 발생하였습니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onReadyForSpeech(Bundle params) {
            //Toast.makeText(getApplicationContext(), "준비완료.", Toast.LENGTH_SHORT).show();
            Snackbar.make(getWindow().getDecorView().getRootView(), "음성 인식 준비가 되었습니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> outStringList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (outStringList != null) {
                //println("결과물: " + outStringList.size());
                println(outStringList.get(0));
                //for (int i = 0; i < outStringList.size(); i++) {
                //    String aItem = outStringList.get(i);
                //    println("음성 결과물" + i + " : " + aItem);
                //}
            }
        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

    }

    private void println(String msg) {
        //logOutput = msg + "\n";
        System.out.println(logOutput);
        sendChatMessage(msg);
        s = msg;
        chkUserMesasgeOnLionaCommunication();
        //receiveMessageByUser(outStringList);
    }

}

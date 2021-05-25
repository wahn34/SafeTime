package com.lionas.ruwn.fairy;

/**
 * Created by ruwn on 2017-02-24.
 */
import android.database.Cursor;
import android.database.sqlite.*;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;
//type detail word
//liona_type, liona_detail, liona_word

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    MatchCommnucation matchCommnucation = new MatchCommnucation();

    public DBHelper(Context context, String liona_type, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, liona_type, factory, version);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE LIONA_TABLE (");
        sb.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" TYPE TEXT, ");
        sb.append(" DETAIL TEXT, ");
        sb.append(" WORD TEXT ) ");

        db.execSQL(sb.toString());

        Toast.makeText(context, "Table 생성완료", Toast.LENGTH_SHORT).show();
    }
    // 버전이 올라가서 Table 구조가 변경되었을 시
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Toast.makeText(context, "버전이 올라갔습니다.", Toast.LENGTH_SHORT).show();
    }

    public void readAndWriteLionaDatabaseOnArray()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT _ID, TYPE, DETAIL, WORD FROM LIONA_TABLE");

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), null);

        List liona = new ArrayList();
        LionaCommunication lionaCommunication = null;

        while (cursor.moveToNext()) {
            lionaCommunication = new LionaCommunication();
            lionaCommunication.set_id(cursor.getInt(0));
            lionaCommunication.setType(cursor.getString(1));
            lionaCommunication.setDetail(cursor.getString(2));
            lionaCommunication.setWord(cursor.getString(3));
        }
            matchCommnucation.lionaType.add(lionaCommunication.getType());
            matchCommnucation.lionaDetail.add(lionaCommunication.getDetail());
            matchCommnucation.lionaWords.add(lionaCommunication.getWords());
    }
    
    public void testDB() {
        SQLiteDatabase db  = getReadableDatabase();
    }

    public void addAutoLionaDatabase(LionaCommunication lionaCommunication)
    {
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO LIONA_TABLE ( ");
        sb.append(" TYPE, DETAIL, WORD ) ");
        sb.append(" VALUES ( ?, ?, ? ) ");

        db.execSQL(sb.toString(),
                new Object[]{
                        lionaCommunication.getType(),
                        lionaCommunication.getDetail(),
                        lionaCommunication.getWords()});
        Toast.makeText(context, "Insert 완료", Toast.LENGTH_SHORT).show();
    }

    public void addLionaCommunication(LionaCommunication lionaCommunication) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO LIONA_TABLE ( ");
        sb.append(" TYPE, DETAIL, WORD ) ");
        sb.append(" VALUES ( ?, ?, ? ) ");

        db.execSQL(sb.toString(),
                new Object[]{
                        lionaCommunication.getType(),
                        lionaCommunication.getDetail(),
                        lionaCommunication.getWords()});
        Toast.makeText(context, "Insert 완료", Toast.LENGTH_SHORT).show();
    }

    public List getAllLionaData() {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT _ID, TYPE, DETAIL, WORD FROM LIONA_TABLE");

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), null);

        List liona = new ArrayList();
        LionaCommunication lionaCommunication = null;

        while (cursor.moveToNext()) {
            lionaCommunication = new LionaCommunication();
            lionaCommunication.set_id(cursor.getInt(0));
            lionaCommunication.setType(cursor.getString(1));
            lionaCommunication.setDetail(cursor.getString(2));
            lionaCommunication.setWord(cursor.getString(3));

            liona.add(lionaCommunication);

        }
        return liona;
    }

    public ArrayList<String> readLionaDatabaseCommunication() {
        String str1;
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + "LIONA_TABLE";// + " WHERE " + "DETAIL";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<String> strLionaWords = new ArrayList<String>();
        LionaCommunication lionaCommunication = null;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                lionaCommunication = new LionaCommunication();
                lionaCommunication.set_id(cursor.getInt(0));
                lionaCommunication.setType(cursor.getString(1));
                lionaCommunication.setDetail(cursor.getString(2));
                lionaCommunication.setWord(cursor.getString(3));
                // Adding contact to list
                strLionaWords.add(lionaCommunication.getWords());
                //System.out.println("Check! " + lionaCommunication.getWords());
                //System.out.print("ID " + lionaCommunication.get_id());
                //System.out.print("TYPE " + lionaCommunication.getType());
                //System.out.print("DETAIL " + lionaCommunication.getDetail());
                //System.out.println("WORDS " + lionaCommunication.getWords());
                matchCommnucation.lionaType.add(lionaCommunication.getType());
                matchCommnucation.lionaDetail.add(lionaCommunication.getDetail());
                matchCommnucation.lionaWords.add(lionaCommunication.getWords());

            } while (cursor.moveToNext());
        }
        return strLionaWords;
    }
    public int readLionaDatabaseCommunicationAllData() {
        int x = 0;
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + "LIONA_TABLE";// + " WHERE " + "DETAIL";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<String> strLionaDetail = new ArrayList<String>();
        LionaCommunication lionaCommunication = null;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                lionaCommunication = new LionaCommunication();
                lionaCommunication.set_id(cursor.getInt(0));
                lionaCommunication.setType(cursor.getString(1));
                lionaCommunication.setDetail(cursor.getString(2));
                lionaCommunication.setWord(cursor.getString(3));

                x+=1;
            } while (cursor.moveToNext());
        }
        return x;
    }
}





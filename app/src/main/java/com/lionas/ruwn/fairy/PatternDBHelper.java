package com.lionas.ruwn.fairy;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruwn on 2017-03-09.
 */

public class PatternDBHelper extends SQLiteOpenHelper {

    private Context context;
    LionaPattern lionaPattern = new LionaPattern();

    public PatternDBHelper(Context context, String liona_date, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, liona_date, factory, version);
        this.context = context;
    }

    public ArrayList<String> readLionaDatabasePatternTime() {
        String str1;
        ArrayList<String> arrPatternTime = new ArrayList<String>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + "PATTERN_TABLE";// + " WHERE " + "DETAIL";
        Cursor cursor = db.rawQuery(selectQuery, null);
        LionaPattern lionaPattern = null;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                lionaPattern = new LionaPattern();
                lionaPattern.set_id(cursor.getInt(0));
                lionaPattern.setDate(cursor.getString(1));
                lionaPattern.setDo(cursor.getString(2));
                lionaPattern.setLocation(cursor.getString(3));
                //System.out.print("ID " + lionaPattern.get_id());
                //System.out.print(" DATE " + lionaPattern.getDate());
                //System.out.print(" DO " + lionaPattern.getDo());
                //System.out.println(" LOCATION " + lionaPattern.getLocation());
                arrPatternTime.add(lionaPattern.getDate());

            } while (cursor.moveToNext());
        }
        return arrPatternTime;
    }
    public ArrayList<String> readLionaDatabasePatternAction() {
        String str1;
        ArrayList<String> arrPatternAction = new ArrayList<String>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + "PATTERN_TABLE";// + " WHERE " + "DETAIL";
        Cursor cursor = db.rawQuery(selectQuery, null);
        LionaPattern lionaPattern = null;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                lionaPattern = new LionaPattern();
                lionaPattern.set_id(cursor.getInt(0));
                lionaPattern.setDate(cursor.getString(1));
                lionaPattern.setDo(cursor.getString(2));
                lionaPattern.setLocation(cursor.getString(3));
                //System.out.print("ID " + lionaPattern.get_id());
                //System.out.print(" DATE " + lionaPattern.getDate());
                //System.out.print(" DO " + lionaPattern.getDo());
                //System.out.println(" LOCATION " + lionaPattern.getLocation());
                arrPatternAction.add(lionaPattern.getDo());

            } while (cursor.moveToNext());
        }
        return arrPatternAction;
    }
    public void readLionaDatabasePattern() {
        String str1;
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + "PATTERN_TABLE";// + " WHERE " + "DETAIL";
        Cursor cursor = db.rawQuery(selectQuery, null);
        LionaPattern lionaPattern = null;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                lionaPattern = new LionaPattern();
                lionaPattern.set_id(cursor.getInt(0));
                lionaPattern.setDate(cursor.getString(1));
                lionaPattern.setDo(cursor.getString(2));
                lionaPattern.setLocation(cursor.getString(3));
                System.out.print("ID " + lionaPattern.get_id());
                System.out.print(" DATE " + lionaPattern.getDate());
                System.out.print(" DO " + lionaPattern.getDo());
                System.out.println(" LOCATION " + lionaPattern.getLocation());

            } while (cursor.moveToNext());
        }
    }

    public void addLionaPatternData(LionaPattern lionaPattern) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO PATTERN_TABLE ( ");
        sb.append(" DATE, DO, LOCATION ) ");
        sb.append(" VALUES ( ?, ?, ? ) ");

        db.execSQL(sb.toString(), new Object[]{
                        lionaPattern.getDate(),
                        lionaPattern.getDo(),
                        lionaPattern.getLocation()});
        //Toast.makeText(context, "Insert 완료", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE PATTERN_TABLE (");
        sb.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" DATE TEXT, ");
        sb.append(" DO TEXT, ");
        sb.append(" LOCATION TEXT ) ");

        db.execSQL(sb.toString());

        //Toast.makeText(context, "Table 생성완료", Toast.LENGTH_SHORT).show();
    }

    // 버전이 올라가서 Table 구조가 변경되었을 시
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Toast.makeText(context, "버전이 올라갔습니다.", Toast.LENGTH_SHORT).show();
    }
    public void testDB() {
        SQLiteDatabase db  = getReadableDatabase();
    }
}
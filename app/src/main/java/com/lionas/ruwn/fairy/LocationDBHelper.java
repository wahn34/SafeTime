package com.lionas.ruwn.fairy;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by ruwn on 2017-03-09.
 */

public class LocationDBHelper  extends SQLiteOpenHelper {

    private Context context;
    LionaLocation lionaLocation = new LionaLocation();

    public LocationDBHelper(Context context, String liona_time, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, liona_time, factory, version);
        this.context = context;
    }

    public ArrayList<String> readLionaDatabaseLocationDate() {
        ArrayList<String> arrDate = new ArrayList<String>();
        String str1;
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + "LOCATION_TABLE";// + " WHERE " + "DETAIL";
        Cursor cursor = db.rawQuery(selectQuery, null);
        LionaLocation lionaLocation = null;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                lionaLocation = new LionaLocation();
                lionaLocation.set_id(cursor.getInt(0));
                lionaLocation.setTime(cursor.getString(1));
                lionaLocation.setAccuracy(cursor.getString(2));
                lionaLocation.setAddress(cursor.getString(3));
                //System.out.print("ID " + lionaLocation.get_id());
                //System.out.print(" TIME " + lionaLocation.getTime());
                //System.out.print(" ACCURACY " + lionaLocation.getAccuracy());
                //System.out.println(" ADDRESS " + lionaLocation.getAddress());
                arrDate.add(lionaLocation.getTime());

            } while (cursor.moveToNext());
        }
        return arrDate;
    }
    public ArrayList<String> readLionaDatabaseLocationAddress() {
        ArrayList<String> arrAddress = new ArrayList<String>();
        String str1;
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + "LOCATION_TABLE";// + " WHERE " + "DETAIL";
        Cursor cursor = db.rawQuery(selectQuery, null);
        LionaLocation lionaLocation = null;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                lionaLocation = new LionaLocation();
                lionaLocation.set_id(cursor.getInt(0));
                lionaLocation.setTime(cursor.getString(1));
                lionaLocation.setAccuracy(cursor.getString(2));
                lionaLocation.setAddress(cursor.getString(3));
                //System.out.print("ID " + lionaLocation.get_id());
                //System.out.print(" TIME " + lionaLocation.getTime());
                //System.out.print(" ACCURACY " + lionaLocation.getAccuracy());
                //System.out.println(" ADDRESS " + lionaLocation.getAddress());
                arrAddress.add(lionaLocation.getAddress());

            } while (cursor.moveToNext());
        }
        return arrAddress;
    }
    public void readLionaDatabaseLocation() {
        String str1;
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + "LOCATION_TABLE";// + " WHERE " + "DETAIL";
        Cursor cursor = db.rawQuery(selectQuery, null);
        LionaLocation lionaLocation = null;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                lionaLocation = new LionaLocation();
                lionaLocation.set_id(cursor.getInt(0));
                lionaLocation.setTime(cursor.getString(1));
                lionaLocation.setAccuracy(cursor.getString(2));
                lionaLocation.setAddress(cursor.getString(3));
                System.out.print("ID " + lionaLocation.get_id());
                System.out.print(" TIME " + lionaLocation.getTime());
                System.out.print(" ACCURACY " + lionaLocation.getAccuracy());
                System.out.println(" ADDRESS " + lionaLocation.getAddress());

            } while (cursor.moveToNext());
        }
    }
    public void addLionaLocationData(LionaLocation lionaLocation) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO LOCATION_TABLE ( ");
        sb.append(" TIME, ACCURACY, ADDRESS ) ");
        sb.append(" VALUES ( ?, ?, ? ) ");

        db.execSQL(sb.toString(), new Object[]{
                lionaLocation.getTime(),
                lionaLocation.getAccuracy(),
                lionaLocation.getAddress()});
        //Toast.makeText(context, "Insert ??????", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE LOCATION_TABLE (");
        sb.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" TIME TEXT, ");
        sb.append(" ACCURACY TEXT, ");
        sb.append(" ADDRESS TEXT ) ");

        db.execSQL(sb.toString());

        //Toast.makeText(context, "Table ????????????", Toast.LENGTH_SHORT).show();
    }

    // ????????? ???????????? Table ????????? ??????????????? ???
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Toast.makeText(context, "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
    }
    public void testDB() {
        SQLiteDatabase db  = getReadableDatabase();
    }
}
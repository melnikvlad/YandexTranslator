package com.example.vlad.mytranslatorwithyandex_v101.DB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Langs.Languages;

import java.util.ArrayList;
import java.util.List;

public class LanguagesSQLite extends SQLiteOpenHelper{
    public final static int DB_VERSION = 2;
    public final static String DB_NAME = "YandexTranslatorDB";
    public final static String TABLE_NAME = "Languages";
    public final static String COL_1 = "ID";
    public final static String COL_2 = "KEY";
    public final static String COL_3 = "VALUE";
    public final static String COL_4 = "DIRECTIONS";

    public LanguagesSQLite(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+TABLE_NAME+
                " ("+COL_1+" INTEGER PRIMARY KEY, " +
                COL_2+" TEXT," +
                COL_3+" TEXT," +
                COL_4+" TEXT"+")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    public void insertData(Languages languages) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

     for (int i = 0;i< languages.getKeys().size();i++){
            contentValues.put(COL_2, languages.getKeys().get(i));
            contentValues.put(COL_3, languages.getValues().get(i));
            contentValues.put(COL_4, languages.getDirs().get(i));
            db.insert(TABLE_NAME,null ,contentValues);
     }
        db.close();
    }

    public Languages getAllData() {
        List<String> dirList = new ArrayList<String>();
        List<String> keyList = new ArrayList<String>();
        List<String> valueList = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {
                keyList.add(cursor.getString(1));
                valueList.add(cursor.getString(2));
                dirList.add(cursor.getString(3));
            } while (cursor.moveToNext());
        }
        Languages languages = new Languages(dirList,keyList,valueList);
        return languages;
    }

    public String getKeyByValue(String value) {
        String key ="";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE VALUE=?", new String[]{value + ""});

        if (cursor.moveToFirst()) {
            do {
                key = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        return key;
    }

    public String getValueByKey(String key) {
        String value ="";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE KEY=?", new String[]{key + ""});

        if (cursor.moveToFirst()) {
            do {
                value = cursor.getString(2);
            } while (cursor.moveToNext());
        }
        return value;
    }

    public List<String> RewriteDirsInValues(List<String> direction){
        String from = "";
        String to = "";
        String space = "                 ";
        List<String> result = new ArrayList<>();
        String[] splited;
        for (int i = 0;i<direction.size();i++){
            splited = direction.get(i).split("-");
            from = splited[0];
            to = splited[1];
            result.add(this.getValueByKey(from)+space+this.getValueByKey(to)) ;
        }
        return result;
    }

    public boolean checkForTranslateDirectionsExists(String direction){
        String value ="";
        boolean exist = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE DIRECTIONS=?", new String[]{direction + ""});

        if (cursor.moveToFirst()) {
            do {
                value = cursor.getString(3);
                exist = true;
            } while (cursor.moveToNext());
        }
        return exist;
    }

    public int getLanguagesCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.execSQL("DELETE FROM "+ TABLE_NAME);
        db.close();
    }
}

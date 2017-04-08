package com.example.vlad.mytranslatorwithyandex_v101.DB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Lookup.Lookup;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Translate.Favourite;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Translate.FavouriteDetail;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Translate.History;
import com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs.Directions;
import com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs.Languages;

import java.util.ArrayList;
import java.util.List;

public class DataBaseSQLite extends SQLiteOpenHelper{
    public final static int DB_VERSION = 12;
    public final static String DB_NAME = "YandexTranslatorDB";

    public final static String LANG_TABLE_NAME = "Languages";
    public final static String DIRS_TRANSLATOR_TABLE_NAME = "Directions";
    public final static String DIRS_DICTIONARY_TABLE_NAME = "Directionsdictionary";
    public final static String HISTORY_TABLE_NAME = "History";
    public final static String FAVOURITE_TABLE_NAME = "Favourite";
    public final static String FAVOURITE_DETAIL_TABLE_NAME = "FavouriteDetail";
    public final static String LOOKUP_TABLE_NAME = "Lookup";

    public final static String ID = "ID";
    public final static String LANG_KEYS = "KEY";
    public final static String LANG_VALUES = "VALUE";

    public final static String DIRS_VALUES = "DIRECTIONS";

    public final static String HISTORY_WORD = "WORD";
    public final static String HISTORY_TRANSLATE = "TRANSLATE";
    public final static String HISTORY_TRANSLATE_LANG = "DIRECTION";

    public final static String LOOKUP_DIR = "DIR";
    public final static String LOOKUP_DEF = "DEF";
    public final static String LOOKUP_POS = "POS";
    public final static String LOOKUP_TOP_ROW = "TOP";
    public final static String LOOKUP_BOT_ROW = "BOT";

    public final static String FAVOURITE_WORD = "WORD";
    public final static String FAVOURITE_TRANSLATE = "TRANSLATE";
    public final static String FAVOURITE_POS = "POS";
    public final static String FAVOURITE_TOP_ROW = "TOP";
    public final static String FAVOURITE_BOT_ROW = "BOT";
    public final static String FAVOURITE_TRANSLATE_DIRECTION = "DIRECTION";

    //=============================================== CREATE =================================================================
    String LanguagesTable = "CREATE TABLE "+ LANG_TABLE_NAME +
            " ("+ ID +" INTEGER PRIMARY KEY, " +
            LANG_KEYS +" TEXT," +
            LANG_VALUES +" TEXT"+")";
    String DirectionsTable = "CREATE TABLE "+ DIRS_TRANSLATOR_TABLE_NAME +
            " ("+ ID +" INTEGER PRIMARY KEY, " +
            DIRS_VALUES +" TEXT"+")";
    String DirectionsDictionaryTable = "CREATE TABLE "+ DIRS_DICTIONARY_TABLE_NAME +
            " ("+ ID +" INTEGER PRIMARY KEY, " +
            DIRS_VALUES +" TEXT"+")";
    String HistoryTable = "CREATE TABLE "+ HISTORY_TABLE_NAME +
            " ("+ ID +" INTEGER PRIMARY KEY, " +
            HISTORY_WORD +" TEXT," +
            HISTORY_TRANSLATE+" TEXT," +
            HISTORY_TRANSLATE_LANG +" TEXT"+")";
    String FavouriteTable = "CREATE TABLE "+ FAVOURITE_TABLE_NAME +
            " ("+ ID +" INTEGER PRIMARY KEY, " +
            FAVOURITE_WORD +" TEXT," +
            FAVOURITE_TRANSLATE+" TEXT," +
            FAVOURITE_TRANSLATE_DIRECTION +" TEXT"+")";
    String FavouriteDetailTable = "CREATE TABLE "+ FAVOURITE_DETAIL_TABLE_NAME +
            " ("+ ID +" INTEGER PRIMARY KEY, " +
            FAVOURITE_WORD +" TEXT," +
            FAVOURITE_TRANSLATE+" TEXT," +
            FAVOURITE_POS+" TEXT," +
            FAVOURITE_TOP_ROW+" TEXT," +
            FAVOURITE_BOT_ROW+" TEXT," +
            FAVOURITE_TRANSLATE_DIRECTION +" TEXT"+")";
    String LookupTable = "CREATE TABLE "+ LOOKUP_TABLE_NAME +
            " ("+ ID +" INTEGER PRIMARY KEY, " +
            LOOKUP_DIR +" TEXT," +
            LOOKUP_DEF +" TEXT," +
            LOOKUP_POS+" TEXT," +
            LOOKUP_TOP_ROW+" TEXT," +
            LOOKUP_BOT_ROW +" TEXT"+")";
    //========================================================================================================================
    public DataBaseSQLite(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LanguagesTable);
        db.execSQL(DirectionsTable);
        db.execSQL(DirectionsDictionaryTable);
        db.execSQL(HistoryTable);
        db.execSQL(FavouriteTable);
        db.execSQL(LookupTable);
        db.execSQL(FavouriteDetailTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ LANG_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ DIRS_TRANSLATOR_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ DIRS_DICTIONARY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ HISTORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ FAVOURITE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ FAVOURITE_DETAIL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ LOOKUP_TABLE_NAME);
        onCreate(db);
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
    //========================================================================================================================
    //=============================================== INSERT =================================================================
    public void insertLanguages(Languages languages) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

     for (int i = 0;i< languages.getKeys().size();i++){
            contentValues.put(LANG_KEYS, languages.getKeys().get(i));
            contentValues.put(LANG_VALUES, languages.getValues().get(i));
            db.insert(LANG_TABLE_NAME,null ,contentValues);
     }
        db.close();
    }

    public void insertDirections(Directions directions) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (int i = 0;i< directions.getDirs().size();i++){
            contentValues.put(DIRS_VALUES, directions.getDirs().get(i));
            db.insert(DIRS_TRANSLATOR_TABLE_NAME,null ,contentValues);
        }
        db.close();
    }
    public void insertDictionaryDirections(Directions directions) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (int i = 0;i< directions.getDirs().size();i++){
            contentValues.put(DIRS_VALUES, directions.getDirs().get(i));
            db.insert(DIRS_DICTIONARY_TABLE_NAME,null ,contentValues);
        }
        db.close();
    }

    public void insertHistory(History history) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(HISTORY_WORD, history.getWord());
        contentValues.put(HISTORY_TRANSLATE, history.getTranslate());
        contentValues.put(HISTORY_TRANSLATE_LANG, history.getDirection());
        db.insert(HISTORY_TABLE_NAME,null ,contentValues);

        db.close();
    }

    public void insertFavourite(Favourite favourite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(FAVOURITE_WORD, favourite.getWord());
        contentValues.put(FAVOURITE_TRANSLATE, favourite.getTranslate());
        contentValues.put(FAVOURITE_TRANSLATE_DIRECTION, favourite.getDirection());
        db.insert(FAVOURITE_TABLE_NAME,null ,contentValues);

        db.close();
    }

    public void insertFavouriteDetail(FavouriteDetail favourite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for(int i = 0;i<favourite.getTop_row().size();i++){
            contentValues.put(FAVOURITE_WORD, favourite.getWord());
            contentValues.put(FAVOURITE_TRANSLATE, favourite.getTranslate());
            contentValues.put(FAVOURITE_POS, favourite.getPos());
            contentValues.put(FAVOURITE_TOP_ROW, favourite.getTop_row().get(i));
            contentValues.put(FAVOURITE_BOT_ROW, favourite.getBot_row().get(i));
            contentValues.put(FAVOURITE_TRANSLATE_DIRECTION, favourite.getDirection());
            db.insert(FAVOURITE_DETAIL_TABLE_NAME,null ,contentValues);
        }
        db.close();
    }

    public void insertLookup(Lookup lookup,String dir) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for(int i = 0;i<lookup.getTop_row().size();i++){
            contentValues.put(LOOKUP_DIR, dir);
            contentValues.put(LOOKUP_DEF, lookup.getDef());
            contentValues.put(LOOKUP_POS, lookup.getPos());
            contentValues.put(LOOKUP_TOP_ROW, lookup.getTop_row().get(i));
            contentValues.put(LOOKUP_BOT_ROW, lookup.getBot_row().get(i));
            db.insert(LOOKUP_TABLE_NAME,null ,contentValues);
        }
        db.close();
    }

    //========================================================================================================================
    //=============================================== GET ====================================================================
    public Languages getKeysAndValuesFromLanguagesTable() {
        List<String> keyList = new ArrayList<String>();
        List<String> valueList = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + LANG_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {
                keyList.add(cursor.getString(1));
                valueList.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        Languages languages = new Languages(keyList,valueList);
        return languages;
    }

    public Directions getDirectionsFromDirectionsTable() {
        List<String> dirList = new ArrayList<String>();
        String selectQuery = "SELECT DIRECTIONS FROM " + DIRS_TRANSLATOR_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {
                dirList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        Directions directions = new Directions(dirList);
        return directions;
    }

    public Directions getDictionaryDirectionsFromDirectionsTable() {
        List<String> dirList = new ArrayList<String>();
        String selectQuery = "SELECT DIRECTIONS FROM " + DIRS_DICTIONARY_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {
                dirList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        Directions directions = new Directions(dirList);
        return directions;
    }

    public List<String> getWordsFromHistoryTable() {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT WORD FROM " + HISTORY_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToLast()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToPrevious());
        }
        return list;
    }

    public List<String> getTranslatesFromHistoryTable() {
        List<String> list = new ArrayList<String>();

        String selectQuery = "SELECT TRANSLATE FROM " + HISTORY_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToLast()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToPrevious());
        }
        return list;
    }

    public String getTransateFromHistoryTable(String word) {
        String trans = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT TRANSLATE FROM " + HISTORY_TABLE_NAME + " WHERE WORD=?", new String[]{word + ""});
        if (cursor.moveToLast()) {
            do {
                trans = cursor.getString(0);
            } while (cursor.moveToPrevious());
        }
        return trans;
    }

    public List<String> getDirsFromHistoryTable() {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT DIRECTION FROM " + HISTORY_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToLast()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToPrevious());
        }
        return list;
    }
    public List<String> getWordsFromFavouriteTable() {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT WORD FROM " + FAVOURITE_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToLast()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToPrevious());
        }
        return list;
    }

    public List<String> getTranslatesFromFavouriteTable() {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT TRANSLATE FROM " + FAVOURITE_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToLast()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToPrevious());
        }
        return list;
    }

    public String getTranslateFromFavouriteTable(String word) {
        String trans = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT TRANSLATE FROM " + FAVOURITE_TABLE_NAME + " WHERE WORD=?", new String[]{word + ""});
        if (cursor.moveToLast()) {
            do {
                trans = cursor.getString(0);
            } while (cursor.moveToPrevious());
        }
        return trans;
    }

    public List<String> getDirsFromFavouriteTable() {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT DIRECTION FROM " + FAVOURITE_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToLast()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToPrevious());
        }
        return list;
    }

    public String getPosFromDetailTable(String word) {
        String pos = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT POS FROM " + FAVOURITE_DETAIL_TABLE_NAME + " WHERE WORD=?", new String[]{word + ""});
        if (cursor.moveToLast()) {
            do {
                pos = cursor.getString(0);
            } while (cursor.moveToPrevious());
        }
        return pos;
    }

    public List<String> getTop_RowFromDetailTable(String word) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT TOP FROM " + FAVOURITE_DETAIL_TABLE_NAME + " WHERE WORD=?", new String[]{word + ""});

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public List<String> getBot_RowFromDetailtable(String word) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT BOT FROM " + FAVOURITE_DETAIL_TABLE_NAME + " WHERE WORD=?", new String[]{word + ""});

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public int ExistInHistoryTable(String word, String dir){
        int count = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + HISTORY_TABLE_NAME + " WHERE WORD=? AND DIRECTION=?", new String[]{word + "",dir + ""});
        if (cursor.moveToFirst()) {
            do {
                count+=1;
            } while (cursor.moveToNext());
        }
        Log.d(Constants.TAG,"EXIST :"+count);
        return count;
    }

    public int ExistInFavouriteTable(String word, String dir){
        int count = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + FAVOURITE_TABLE_NAME + " WHERE WORD=? AND DIRECTION=?", new String[]{word + "",dir + ""});
        if (cursor.moveToFirst()) {
            do {
                count+=1;
            } while (cursor.moveToNext());
        }
        return count;
    }

    public List<String> getTop_Row_by_word(String word){
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + LOOKUP_TABLE_NAME + " WHERE DEF=?", new String[]{word + ""});
        if (cursor.moveToFirst()) {
            do {
               list.add(cursor.getString(3));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public List<String> getBot_Row_by_word(String word){
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + LOOKUP_TABLE_NAME + " WHERE DEF=?", new String[]{word + ""});

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(4));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public String getPosFromLookupTable(String word){
        String pos = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + LOOKUP_TABLE_NAME + " WHERE DEF=?", new String[]{word + ""});

        if (cursor.moveToFirst()) {
            do {
                pos = cursor.getString(2);
            } while (cursor.moveToNext());
        }
        return pos;
    }

    //=======================================================================================================================
    //=============================================== COUNT =================================================================
    public int getLanguagesCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + LANG_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getDictoinaryDirectionsCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + DIRS_DICTIONARY_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    //=======================================================================================================================
    //=================================================OTHER=================================================================
    public String getKeyByValue(String value) { // From English To en
        String key ="";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + LANG_TABLE_NAME + " WHERE VALUE=?", new String[]{value + ""});

        if (cursor.moveToFirst()) {
            do {
                key = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        return key;
    }

    public String getValueByKey(String key) { // From en To English
        String value ="";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + LANG_TABLE_NAME + " WHERE KEY=?", new String[]{key + ""});

        if (cursor.moveToFirst()) {
            do {
                value = cursor.getString(2);
            } while (cursor.moveToNext());
        }
        return value;
    }

    public List<String> RewriteDirsToValuesInDirectionsTable(List<String> direction){ // From en-ru To English Russian
        String from = "";
        String to = "";
        String space = "-";
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

    public boolean TranslateDirectionExistInDirectionsTable(String direction){ // Example en-hi exist in directions or not
        boolean exist = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT DIRECTIONS FROM " + DIRS_TRANSLATOR_TABLE_NAME + " WHERE DIRECTIONS=?", new String[]{direction + ""});

        if (cursor.moveToFirst()) {
            do {
                exist = true;
            } while (cursor.moveToNext());
        }
        return exist;
    }

    public boolean TranslateDirectionExistInDictionaryDirectionsTable(String direction){ // Example en-hi exist in directions or not
        boolean exist = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT DIRECTIONS FROM " + DIRS_DICTIONARY_TABLE_NAME + " WHERE DIRECTIONS=?", new String[]{direction + ""});

        if (cursor.moveToFirst()) {
            do {
                exist = true;
            } while (cursor.moveToNext());
        }
        return exist;
    }

    //========================================================DELETE=========================================================
    //=======================================================================================================================
    public void deleteLanguageAndDirectionTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LANG_TABLE_NAME,null,null);
        db.execSQL("DELETE FROM "+ LANG_TABLE_NAME);
        db.delete(DIRS_TRANSLATOR_TABLE_NAME,null,null);
        db.execSQL("DELETE FROM "+ DIRS_TRANSLATOR_TABLE_NAME);
        db.delete(DIRS_DICTIONARY_TABLE_NAME,null,null);
        db.execSQL("DELETE FROM "+ DIRS_DICTIONARY_TABLE_NAME);
        db.close();
    }
    public void deleteHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(HISTORY_TABLE_NAME,null,null);
        db.execSQL("DELETE FROM "+ HISTORY_TABLE_NAME);
        db.close();
    }

    public void deleteHistoryItem (String word,String translate,String dir) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(HISTORY_TABLE_NAME, HISTORY_WORD+" = '"+word+"' AND "+HISTORY_TRANSLATE+" = '"+translate+"' AND "+HISTORY_TRANSLATE_LANG+" = '"+dir+"'", null);
        db.close();
    }

    public void deleteLookupItem (String word,String dir) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LOOKUP_TABLE_NAME, LOOKUP_DEF+" = '"+word+"' AND "+LOOKUP_DIR+" = '"+dir+"'", null);
        db.close();
    }

}

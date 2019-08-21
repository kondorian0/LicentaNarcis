package com.narcis.neamtiu.licentanarcis.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Licenta_Narcis.db";

    //TABLE names
    public static final String TABLE_TODO_EVENTS = "TODO_EVENTS";
    public static final String TABLE_IMAGE = "IMAGE";
    public static final String TABLE_AUDIO = "AUDIO";
    public static final String TABLE_NOTE = "NOTE";
    public static final String TABLE_EVENT = "EVENT";

    // Common column names
    public static final String KEY_ID = "ID";
    public static final String KEY_EVENT_TYPE = "EVENT_TYPE";
    public static final String KEY_PATH = "FILE_PATH";

    // TODO_EVENTS Table - column names
    public static final String KEY_DATE_FROM = "DATE_FROM";
    public static final String KEY_DATE_TO = "DATE_TO";
    public static final String KEY_TIME_FROM = "TIME_FROM";
    public static final String KEY_TIME_TO = "TIME_TO";

    // NOTE Table - column names
    public static final String KEY_NOTE = "NOTE_TEXT";

    // EVENT Table - column names
    public static final String KEY_TITLE = "TITLE";
    public static final String KEY_DESCRIPTION = "DESCRIPTION";
    public static final String KEY_LOCATION = "LOCATION";


    // Table Create Statements
    // Todo table create statement

    private static final String CREATE_TABLE_TODO_EVENTS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TODO_EVENTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_EVENT_TYPE + " TEXT,"
            + KEY_DATE_FROM + " DATETIME,"
            + KEY_TIME_FROM + " DATETIME" + ")";

    private static final String CREATE_TABLE_IMAGE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_IMAGE + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PATH + " TEXT" + ")";

    private static final String CREATE_TABLE_AUDIO = "CREATE TABLE IF NOT EXISTS "
            + TABLE_AUDIO + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PATH + " TEXT" + ")";

    private static final String CREATE_TABLE_NOTE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NOTE + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NOTE + " TEXT" + ")";

    private static final String CREATE_TABLE_EVENT = "CREATE TABLE IF NOT EXISTS "
            + TABLE_EVENT + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TITLE + " TEXT,"
            + KEY_DESCRIPTION + " TEXT,"
            + KEY_LOCATION + " TEXT" + ")";


    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_TODO_EVENTS);
        db.execSQL(CREATE_TABLE_IMAGE);
        db.execSQL(CREATE_TABLE_AUDIO);
        db.execSQL(CREATE_TABLE_NOTE);
        db.execSQL(CREATE_TABLE_EVENT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
        // create new tables
        onCreate(db);

    }

    // -----------------------ALL EVENTS table methods--------------------------- //

    public long insertDataTodoEvent(String event_Type, String date_from, String time_from) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_EVENT_TYPE, event_Type);
        contentValues.put(KEY_DATE_FROM, date_from);
        contentValues.put(KEY_TIME_FROM, time_from);

        long id = db.insert(TABLE_TODO_EVENTS, null, contentValues);

        return id;
    }

    // -----------------------EVENT table methods--------------------------- //

    public long insertDataEvent(String title, String description, String location){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, title);
        contentValues.put(KEY_DESCRIPTION, description);
        contentValues.put(KEY_LOCATION, location);

        long id = db.insert(TABLE_EVENT, null, contentValues);

        return  id;

    }

    // -----------------------NOTE table methods--------------------------- //

    public long insertDataNote(String note){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NOTE, note);

        long id = db.insert(TABLE_NOTE, null, contentValues);

        return  id;

    }

    // -----------------------AUDIO table methods--------------------------- //

    public long insertDataAudio(String path){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PATH, path);

        long id = db.insert(TABLE_AUDIO, null, contentValues);

        return  id;

    }

    // -----------------------AUDIO table methods--------------------------- //

    public long insertDataImage(String path){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PATH, path);

        long id = db.insert(TABLE_IMAGE, null, contentValues);

        return  id;

    }

}

package com.example.sotrudniki;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Staff_data";
    public static final String TABLE_STAFF = "Staff";
    public static final String TABLE_POSITIONS = "Positions";
    public static final String TABLE_BUSINESS_TRIPS = "Business_trips";
    public static final String TABLE_USERS = "User";

    public static final String KEY_ID_POSITIONS = "_id_positions";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DISCHARGE = "discharge";
    public static final String KEY_SALARY = "salary";

    public static final String KEY_ID_STAFF = "_id_staff";
    public static final String KEY_SURNAME = "surname";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_KOD_POST = "kod_post";

    public static final String KEY_ID_TRIPS = "_id_trips";
    public static final String KEY_KOD_STAFF = "kod_staff";
    public static final String KEY_CITY = "city";
    public static final String KEY_DAILY = "daily";

    public static final String KEY_ID1 = "_id";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_POSITIONS + "(" + KEY_ID_POSITIONS
                + " integer primary key," + KEY_TITLE + " text," + KEY_DISCHARGE + " integer,"+ KEY_SALARY + " integer" +")");

        db.execSQL("create table " + TABLE_STAFF + "(" + KEY_ID_STAFF
                + " integer primary key," + KEY_SURNAME + " text," + KEY_FIRST_NAME + " text,"+ KEY_LAST_NAME + " text," + KEY_KOD_POST + " integer,"+
                " FOREIGN KEY ("+KEY_KOD_POST+") REFERENCES "+TABLE_POSITIONS+"("+KEY_ID_POSITIONS+"))");

        db.execSQL("create table " + TABLE_BUSINESS_TRIPS + "(" + KEY_ID_TRIPS
                + " integer primary key," + KEY_KOD_STAFF + " integer," + KEY_CITY + " text,"+ KEY_DAILY + " integer," +
                " FOREIGN KEY ("+KEY_KOD_STAFF+") REFERENCES "+TABLE_STAFF+"("+KEY_ID_STAFF+"))");

        db.execSQL("create table " + TABLE_USERS + "(" + KEY_ID1
                + " integer primary key," + KEY_LOGIN + " text," + KEY_PASSWORD + " text"+")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_POSITIONS);
        db.execSQL("drop table if exists " + TABLE_STAFF);
        db.execSQL("drop table if exists " + TABLE_BUSINESS_TRIPS);
        db.execSQL("drop table if exists " + TABLE_USERS);
        onCreate(db);

    }
}

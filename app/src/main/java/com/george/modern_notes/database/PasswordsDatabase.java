package com.george.modern_notes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PasswordsDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "passwords.db"; // название бд
    private static final int VERSION = 1; // версия базы данных
    public static final String TABLE = "users"; // название таблицы в бд

    // названия столбцов таблицы
    public static final String ID = "_id";
    public static final String WEB = "name";
    public static final String PASSWORD = "code";
    public static final String NAME_ACCOUNT = "nameAcc";
    public static final String THEME_MODE_PASSWORDS = "them_pas";

    public PasswordsDatabase(Context context) { super(context, DATABASE_NAME, null, VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + WEB + " TEXT, " + PASSWORD + " TEXT, " +
                NAME_ACCOUNT + " TEXT, " + THEME_MODE_PASSWORDS + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

}

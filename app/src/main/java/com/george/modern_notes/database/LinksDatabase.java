package com.george.modern_notes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LinksDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "link.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    public static final String TABLE = "links"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME_LINK = "name";
    public static final String COLUMN_LNK = "link";
    public static final String COLUMN_LINK_NOTE = "link_note";
    public static final String COLUMN_THEME_MODE_LINK = "theme";

    public LinksDatabase(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE links (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME_LINK + " TEXT, " + COLUMN_LNK + " TEXT, "
                + COLUMN_LINK_NOTE + " TEXT, " + COLUMN_THEME_MODE_LINK + " TEXT);");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }

}

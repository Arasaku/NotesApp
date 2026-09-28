package com.example.main.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes_app.db";
    private static final int DATABASE_VERSION = 1;

    // Таблица пользователей
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    // Таблица заметок
    public static final String TABLE_NOTES = "notes";
    public static final String COL_NOTE_ID = "id";
    public static final String COL_NOTE_USER_ID = "user_id";
    public static final String COL_NOTE_TITLE = "title";
    public static final String COL_NOTE_CONTENT = "content";
    public static final String COL_NOTE_CREATED = "created_at";

    private static final String CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_USERNAME + " TEXT UNIQUE NOT NULL, " +
                    COL_PASSWORD + " TEXT NOT NULL);";

    private static final String CREATE_NOTES =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    COL_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_NOTE_USER_ID + " INTEGER NOT NULL, " +
                    COL_NOTE_TITLE + " TEXT NOT NULL, " +
                    COL_NOTE_CONTENT + " TEXT, " +
                    COL_NOTE_CREATED + " INTEGER NOT NULL, " +
                    "FOREIGN KEY(" + COL_NOTE_USER_ID + ") REFERENCES " +
                    TABLE_USERS + "(" + COL_USER_ID + ") ON DELETE CASCADE);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS);
        db.execSQL(CREATE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}

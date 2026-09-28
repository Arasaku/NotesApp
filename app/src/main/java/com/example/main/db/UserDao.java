package com.example.main.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.main.model.User;

public class UserDao {

    private final DatabaseHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long registerUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_USERNAME, username);
        values.put(DatabaseHelper.COL_PASSWORD, password);
        long id = db.insert(DatabaseHelper.TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public User loginUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USERS,
                null,
                DatabaseHelper.COL_USERNAME + "=? AND " + DatabaseHelper.COL_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID));
            user = new User(id, username, password);
        }
        cursor.close();
        db.close();
        return user;
    }
}

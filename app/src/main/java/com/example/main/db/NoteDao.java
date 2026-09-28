package com.example.main.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.main.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteDao {

    private final DatabaseHelper dbHelper;

    public NoteDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_NOTE_USER_ID, note.getUserId());
        values.put(DatabaseHelper.COL_NOTE_TITLE, note.getTitle());
        values.put(DatabaseHelper.COL_NOTE_CONTENT, note.getContent());
        values.put(DatabaseHelper.COL_NOTE_CREATED, note.getCreatedAt());
        long id = db.insert(DatabaseHelper.TABLE_NOTES, null, values);
        db.close();
        return id;
    }

    public List<Note> getNotesByUser(long userId) {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NOTES,
                null,
                DatabaseHelper.COL_NOTE_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null,
                DatabaseHelper.COL_NOTE_CREATED + " DESC");

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NOTE_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NOTE_TITLE));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NOTE_CONTENT));
            long createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NOTE_CREATED));
            notes.add(new Note(id, userId, title, content, createdAt));
        }
        cursor.close();
        db.close();
        return notes;
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_NOTE_TITLE, note.getTitle());
        values.put(DatabaseHelper.COL_NOTE_CONTENT, note.getContent());
        int rows = db.update(
                DatabaseHelper.TABLE_NOTES,
                values,
                DatabaseHelper.COL_NOTE_ID + "=?",
                new String[]{String.valueOf(note.getId())});
        db.close();
        return rows;
    }

    public void deleteNote(long noteId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(
                DatabaseHelper.TABLE_NOTES,
                DatabaseHelper.COL_NOTE_ID + "=?",
                new String[]{String.valueOf(noteId)});
        db.close();
    }
}

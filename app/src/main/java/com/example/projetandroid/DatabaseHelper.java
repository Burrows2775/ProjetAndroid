package com.example.projetandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mathquiz.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE = "scores";
    private static final String COL_NAME = "name";
    private static final String COL_SCORE = "score";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_SCORE + " INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void insertScore(String name, int score) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_SCORE, score);
        db.insert(TABLE, null, cv);
        db.close();
    }

    public List<ScoreEntry> getTop10() {
        List<ScoreEntry> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COL_NAME + ", " + COL_SCORE +
                " FROM " + TABLE +
                " ORDER BY " + COL_SCORE + " DESC LIMIT 10", null);
        while (cursor.moveToNext()) {
            list.add(new ScoreEntry(cursor.getString(0), cursor.getInt(1)));
        }
        cursor.close();
        db.close();
        return list;
    }

    public static class ScoreEntry {
        public final String name;
        public final int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }
}

package com.shawncheng.termtracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shawncheng.termtracker.model.Term;

import java.util.ArrayList;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TermTracker.db";
    private static final int DATABASE_VERSION = 1;

    // Term Table Constants
    private static final String TERM_TABLE_NAME = "term";
    private static final String TERM_COLUMN_ID = "termId";
    private static final String TERM_COLUMN_TITLE = "title";
    private static final String TERM_COLUMN_START = "startDate";
    private static final String TERM_COLUMN_END = "endDate";
    public static final String CREATE_TABLE_TERM = "CREATE TABLE " + TERM_TABLE_NAME + "(" +
            TERM_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TERM_COLUMN_TITLE + " TEXT NOT NULL," +
            TERM_COLUMN_START + " TEXT NOT NULL," +
            TERM_COLUMN_END + " TEXT NOT NULL);";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TERM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TERM_TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<Term> getTerms() {
        ArrayList<Term> termArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TERM_TABLE_NAME, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            int termId = res.getInt(0);
            String termName = res.getString(1);
            String startDate = res.getString(2);
            String endDate = res.getString(3);
            termArrayList.add(new Term(termId, termName, startDate, endDate));
            res.moveToNext();
        }
        res.close();
        return termArrayList;
    }

    public boolean insertTerm(String title, String start, String end) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TERM_COLUMN_TITLE, title);
        cv.put(TERM_COLUMN_START, start);
        cv.put(TERM_COLUMN_END, end);
        db.insert(TERM_TABLE_NAME, null, cv);
        return true;
    }
}

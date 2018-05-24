package com.shawncheng.termtracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.Mentor;
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
    public static final String SQL_GET_TERMS = "SELECT * FROM " + TERM_TABLE_NAME;

    // Course Table Constants
    private static final String COURSE_TABLE_NAME = "course";
    private static final String COURSE_COLUMN_ID = "courseId";
    private static final String COURSE_COLUMN_TITLE = "courseName";
    private static final String COURSE_COLUMN_STATUS = "status";
    private static final String COURSE_COLUMN_START = "startDate";
    private static final String COURSE_COLUMN_END = "endDate";
    private static final String COURSE_COLUMN_TERM_ID = "termId";
    public static final String CREATE_TABLE_COURSE = "CREATE TABLE " + COURSE_TABLE_NAME + "(" +
            COURSE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COURSE_COLUMN_TITLE + " TEXT NOT NULL," +
            COURSE_COLUMN_STATUS + " TEXT NOT NULL," +
            COURSE_COLUMN_START + " TEXT NOT NULL," +
            COURSE_COLUMN_END + " TEXT NOT NULL," +
            COURSE_COLUMN_TERM_ID + " INTEGER NOT NULL," +
            "FOREIGN KEY(" + COURSE_COLUMN_TERM_ID + ") " +
            "REFERENCES " + TERM_TABLE_NAME + "(" + TERM_COLUMN_ID + "));";
    public static final String SQL_GET_COURSES = "SELECT * FROM " + COURSE_TABLE_NAME + " WHERE " + COURSE_COLUMN_TERM_ID + " = ";

    // Mentor Table Constants
    private static final String MENTOR_TABLE_NAME = "mentor";
    private static final String MENTOR_COLUMN_ID = "mentor_id";
    private static final String MENTOR_COLUMN_NAME = "name";
    private static final String MENTOR_COLUMN_PHONE = "phone";
    private static final String MENTOR_COLUMN_EMAIL = "email";
    private static final String MENTOR_COLUMN_COURSE_ID = "course_id";
    public static final String CREATE_MENTOR_TABLE = "CREATE TABLE " + MENTOR_TABLE_NAME + "(" +
            MENTOR_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MENTOR_COLUMN_NAME + " TEXT NOT NULL," +
            MENTOR_COLUMN_PHONE + " TEXT NOT NULL," +
            MENTOR_COLUMN_EMAIL + " TEXT NOT NULL," +
            MENTOR_COLUMN_COURSE_ID + " INTEGER NOT NULL," +
            "FOREIGN KEY(" + MENTOR_COLUMN_COURSE_ID + ") " +
            "REFERENCES " + COURSE_TABLE_NAME + "(" + COURSE_COLUMN_ID + "));";
    public static final String SQL_GET_MENTORS = "SELECT * FROM " + MENTOR_TABLE_NAME + " WHERE " + MENTOR_COLUMN_COURSE_ID + " = ";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TERM);
        db.execSQL(CREATE_TABLE_COURSE);
        db.execSQL(CREATE_MENTOR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TERM_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + COURSE_TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<Term> getTerms() {
        ArrayList<Term> termArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(SQL_GET_TERMS, null);
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

    //TODO remove getTerm if never used.
    public Term getTerm(int termId) {
        Term term;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TERM_TABLE_NAME + " WHERE " + TERM_COLUMN_ID + " = " + termId,
                null
        );
        cursor.moveToFirst();
        term = new Term(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
        );
        cursor.close();
        return term;
    }

    public boolean updateTerm(int id, String title, String start, String end) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TERM_COLUMN_TITLE, title);
        contentValues.put(TERM_COLUMN_START, start);
        contentValues.put(TERM_COLUMN_END, end);
        db.update(
                TERM_TABLE_NAME,
                contentValues,
                TERM_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) }
        );
        return true;
    }

    public boolean insertCourse(String title, String start, String end, String status, int termId, ArrayList<Mentor> mentors) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COURSE_COLUMN_TITLE, title);
        contentValues.put(COURSE_COLUMN_START, start);
        contentValues.put(COURSE_COLUMN_END, end);
        contentValues.put(COURSE_COLUMN_STATUS, status);
        contentValues.put(COURSE_COLUMN_TERM_ID, termId);
        long mCourseId = db.insert(COURSE_TABLE_NAME, null, contentValues);
        for (Mentor mentor : mentors) {
            ContentValues cv1 = new ContentValues();
            cv1.put(MENTOR_COLUMN_NAME, mentor.getName());
            cv1.put(MENTOR_COLUMN_PHONE, mentor.getPhone());
            cv1.put(MENTOR_COLUMN_EMAIL, mentor.getEmail());
            cv1.put(MENTOR_COLUMN_COURSE_ID, mCourseId);
            db.insert(MENTOR_TABLE_NAME, null, cv1);
        }
        return true;
    }

    public ArrayList<Course> getCourses(int termId) {
        ArrayList<Course> courseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_GET_COURSES + termId,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int mId = cursor.getInt(0);
            String mTitle = cursor.getString(1);
            String mStatus = cursor.getString(2);
            String mStart = cursor.getString(3);
            String mEnd = cursor.getString(4);
            int mTermId = cursor.getInt(5);
            courseList.add(new Course(mId, mTitle, mStatus, mStart, mEnd, mTermId));
            cursor.moveToNext();
        }
        cursor.close();
        return courseList;
    }

    public ArrayList<Mentor> getMentors(int courseId) {
        ArrayList<Mentor> mentorList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_GET_MENTORS + courseId,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String mName = cursor.getString(1);
            String mPhone = cursor.getString(2);
            String mEmail = cursor.getString(3);
            mentorList.add(new Mentor(mName, mPhone, mEmail));
            cursor.moveToNext();
        }
        cursor.close();
        return mentorList;
    }
}

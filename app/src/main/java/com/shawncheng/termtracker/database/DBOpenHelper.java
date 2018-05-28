package com.shawncheng.termtracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shawncheng.termtracker.model.Assessment;
import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.GoalDate;
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
    public static final String CREATE_TABLE_MENTOR = "CREATE TABLE " + MENTOR_TABLE_NAME + "(" +
            MENTOR_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MENTOR_COLUMN_NAME + " TEXT NOT NULL," +
            MENTOR_COLUMN_PHONE + " TEXT NOT NULL," +
            MENTOR_COLUMN_EMAIL + " TEXT NOT NULL," +
            MENTOR_COLUMN_COURSE_ID + " INTEGER NOT NULL," +
            "FOREIGN KEY(" + MENTOR_COLUMN_COURSE_ID + ") " +
            "REFERENCES " + COURSE_TABLE_NAME + "(" + COURSE_COLUMN_ID + "));";
    public static final String SQL_GET_MENTORS = "SELECT * FROM " + MENTOR_TABLE_NAME + " WHERE " + MENTOR_COLUMN_COURSE_ID + " = ";

    // Assessment Table Constants
    private static final String ASSESSMENT_TABLE_NAME = "assessment";
    private static final String ASSESSMENT_COLUMN_ID = "assessmentId";
    private static final String ASSESSMENT_COLUMN_TITLE = "title";
    private static final String ASSESSMENT_COLUMN_TYPE = "type";
    private static final String ASSESSMENT_COLUMN_DUE = "dueDate";
    private static final String ASSESSMENT_COLUMN_COURSE_ID = "courseId";
    public static final String CREATE_TABLE_ASSESSMENT = "CREATE TABLE " + ASSESSMENT_TABLE_NAME + "(" +
            ASSESSMENT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ASSESSMENT_COLUMN_TITLE + " TEXT NOT NULL," +
            ASSESSMENT_COLUMN_TYPE + " TEXT NOT NULL," +
            ASSESSMENT_COLUMN_DUE + " TEXT NOT NULL," +
            ASSESSMENT_COLUMN_COURSE_ID + " INTEGER NOT NULL," +
            "FOREIGN KEY(" + ASSESSMENT_COLUMN_COURSE_ID + ") " +
            "REFERENCES " + COURSE_TABLE_NAME + "(" + COURSE_COLUMN_ID + "));";
    public static final String SQL_GET_ASSESSMENTS = "SELECT * FROM " + ASSESSMENT_TABLE_NAME + " WHERE " + ASSESSMENT_COLUMN_COURSE_ID + " = ";

    // Goal Date Table Constants
    private static final String GOAL_DATE_TABLE_NAME = "goal_date";
    private static final String GOAL_DATE_COLUMN_ID = "goal_date_id";
    private static final String GOAL_DATE_COLUMN_DATE = "date";
    private static final String GOAL_DATE_COLUMN_ASSESSMENT_ID = "assessment_id";
    public static final String SQL_GET_GOALS = "SELECT * FROM " + GOAL_DATE_TABLE_NAME + " WHERE " + GOAL_DATE_COLUMN_ASSESSMENT_ID + " = ";
    public static final String CREATE_TABLE_GOAL_DATE = "CREATE TABLE " + GOAL_DATE_TABLE_NAME + "(" +
            GOAL_DATE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            GOAL_DATE_COLUMN_DATE + " TEXT NOT NULL," +
            GOAL_DATE_COLUMN_ASSESSMENT_ID + " INTEGER NOT NULL," +
            "FOREIGN KEY(" + GOAL_DATE_COLUMN_ASSESSMENT_ID + ")" +
            "REFERENCES " + ASSESSMENT_TABLE_NAME + "(" + ASSESSMENT_COLUMN_ID + "));";
    public static final String SQL_GET_GOAL = "SELECT * FROM " + GOAL_DATE_TABLE_NAME + " WHERE " + GOAL_DATE_COLUMN_ASSESSMENT_ID + " = ";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TERM);
        db.execSQL(CREATE_TABLE_COURSE);
        db.execSQL(CREATE_TABLE_MENTOR);
        db.execSQL(CREATE_TABLE_ASSESSMENT);
        db.execSQL(CREATE_TABLE_GOAL_DATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TERM_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + COURSE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MENTOR_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ASSESSMENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GOAL_DATE_TABLE_NAME);
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

    public boolean deleteTerm(int termId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TERM_TABLE_NAME, TERM_COLUMN_ID + " = " + termId, null);
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

    public long insertCourse(String title, String start, String end, String status, int termId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COURSE_COLUMN_TITLE, title);
        contentValues.put(COURSE_COLUMN_START, start);
        contentValues.put(COURSE_COLUMN_END, end);
        contentValues.put(COURSE_COLUMN_STATUS, status);
        contentValues.put(COURSE_COLUMN_TERM_ID, termId);
        long courseId = db.insert(COURSE_TABLE_NAME, null, contentValues);
//        for (Mentor mentor : mentors) {
//            ContentValues cv1 = new ContentValues();
//            cv1.put(MENTOR_COLUMN_NAME, mentor.getName());
//            cv1.put(MENTOR_COLUMN_PHONE, mentor.getPhone());
//            cv1.put(MENTOR_COLUMN_EMAIL, mentor.getEmail());
//            cv1.put(MENTOR_COLUMN_COURSE_ID, mCourseId);
//            db.insert(MENTOR_TABLE_NAME, null, cv1);
//        }
        return courseId;
    }

    public Course getCourse(int courseId) {
        Course course;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(
                "SELECT * FROM " + COURSE_TABLE_NAME + " WHERE " + COURSE_COLUMN_ID + " = " + courseId,
                null
        );
        res.moveToFirst();
        course = new Course(
                res.getInt(0),
                res.getString(1),
                res.getString(2),
                res.getString(3),
                res.getString(4),
                res.getInt(5)
        );
        res.close();
        return course;
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

    public boolean deleteCourse(int courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(COURSE_TABLE_NAME, COURSE_COLUMN_ID + " = " + courseId, null);
        deleteAssessmentFromCourse(courseId);
        //db.delete(ASSESSMENT_TABLE_NAME, ASSESSMENT_COLUMN_COURSE_ID + " = " + courseId, null);
        //TODO If we put note table, then we'll need to delete notes from here too
        return true;
    }

    public boolean updateCourse(int id, String title, String start, String end, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COURSE_COLUMN_TITLE, title);
        contentValues.put(COURSE_COLUMN_START, start);
        contentValues.put(COURSE_COLUMN_END, end);
        contentValues.put(COURSE_COLUMN_STATUS, status);
        db.update(COURSE_TABLE_NAME, contentValues, COURSE_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) });
        return true;
    }

    public long insertMentor(String title, String phone, String email, int courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MENTOR_COLUMN_NAME, title);
        contentValues.put(MENTOR_COLUMN_PHONE, phone);
        contentValues.put(MENTOR_COLUMN_EMAIL, email);
        contentValues.put(MENTOR_COLUMN_COURSE_ID, courseId);
        return db.insert(MENTOR_TABLE_NAME, null, contentValues);
    }

    public boolean updateMentor(int id, String title, String phone, String email, int courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MENTOR_COLUMN_NAME, title);
        contentValues.put(MENTOR_COLUMN_PHONE, phone);
        contentValues.put(MENTOR_COLUMN_EMAIL, email);
        contentValues.put(MENTOR_COLUMN_COURSE_ID, courseId);
        db.update(MENTOR_TABLE_NAME, contentValues, MENTOR_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) });
        return true;
    }

    public ArrayList<Mentor> getMentors(int courseId) {
        ArrayList<Mentor> mentorList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_GET_MENTORS + courseId,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String mName = cursor.getString(1);
            String mPhone = cursor.getString(2);
            String mEmail = cursor.getString(3);
            mentorList.add(new Mentor(id, mName, mPhone, mEmail));
            cursor.moveToNext();
        }
        cursor.close();
        return mentorList;
    }

    public Mentor getMentor(int mentorId) {
        Mentor mentor;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + MENTOR_TABLE_NAME + " WHERE " + MENTOR_COLUMN_ID + " = " + mentorId,
                null
        );
        cursor.moveToFirst();
        mentor = new Mentor(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getInt(4)
        );
        cursor.close();
        return mentor;
    }

    public boolean deleteMentor(int mentorId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MENTOR_TABLE_NAME, MENTOR_COLUMN_ID + " = " + mentorId, null);
        return true;
    }

    public long insertAssessment(String title, String type, String due, int courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ASSESSMENT_COLUMN_TITLE, title);
        contentValues.put(ASSESSMENT_COLUMN_TYPE, type);
        contentValues.put(ASSESSMENT_COLUMN_DUE, due);
        contentValues.put(ASSESSMENT_COLUMN_COURSE_ID, courseId);
        long id = db.insert(ASSESSMENT_TABLE_NAME, null, contentValues);
        return id;
    }

    public boolean updateAssessment(int id, String title, String type, String due, int courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ASSESSMENT_COLUMN_TITLE, title);
        contentValues.put(ASSESSMENT_COLUMN_TYPE, type);
        contentValues.put(ASSESSMENT_COLUMN_DUE, due);
        contentValues.put(ASSESSMENT_COLUMN_COURSE_ID, courseId);
        db.update(ASSESSMENT_TABLE_NAME, contentValues, ASSESSMENT_COLUMN_ID + " = ?", new String[] {Integer.toString(id)});
        return true;
    }

    public boolean deleteAssessment(int assessmentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ASSESSMENT_TABLE_NAME, ASSESSMENT_COLUMN_ID + " = " + assessmentId, null);
        db.delete(GOAL_DATE_TABLE_NAME, GOAL_DATE_COLUMN_ASSESSMENT_ID + " = " + assessmentId, null);
        return true;
    }

    public boolean deleteAssessmentFromCourse(int courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Assessment> assessmentArrayList = getAssessments(courseId);
        db.delete(ASSESSMENT_TABLE_NAME, ASSESSMENT_COLUMN_COURSE_ID + " = " + courseId, null);
        for (Assessment assessment : assessmentArrayList) {
            db.delete(GOAL_DATE_TABLE_NAME, GOAL_DATE_COLUMN_ASSESSMENT_ID + " = " + assessment.getAssessmentId(), null);
        }
        return true;
    }

    public ArrayList<Assessment> getAssessments(int courseId) {
        ArrayList<Assessment> assessmentArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_GET_ASSESSMENTS + courseId,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int mId = cursor.getInt(0);
            String mTitle = cursor.getString(1);
            String mType = cursor.getString(2);
            String mDue = cursor.getString(3);
            int mCourseId = cursor.getInt(4);
            assessmentArrayList.add(new Assessment(mId, mTitle, mType, mDue, mCourseId));
            cursor.moveToNext();
        }
        cursor.close();
        return assessmentArrayList;
    }

    public void insertGoal(String goalDate,  long assessmentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GOAL_DATE_COLUMN_DATE, goalDate);
        contentValues.put(GOAL_DATE_COLUMN_ASSESSMENT_ID, assessmentId);
        db.insert(GOAL_DATE_TABLE_NAME, null, contentValues);
    }

    public void updateGoal(String goalDate,  long assessmentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GOAL_DATE_COLUMN_DATE, goalDate);
        db.update(GOAL_DATE_TABLE_NAME, contentValues, GOAL_DATE_COLUMN_ASSESSMENT_ID + " = ?", new String[] {Long.toString(assessmentId)});
    }

    public GoalDate getGoal(int assessmentId) {
        GoalDate goal;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                SQL_GET_GOAL + assessmentId,
                null
        );
        cursor.moveToFirst();
        goal = new GoalDate(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getInt(2)
        );
        cursor.close();
        return goal;
    }

    public ArrayList<GoalDate> getGoals(int assessmentId) {
        ArrayList<GoalDate> goalDates = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_GET_GOALS + assessmentId, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int goalId = cursor.getInt(0);
            String date = cursor.getString(1);
            int assessId = cursor.getInt(2);
            goalDates.add(new GoalDate(goalId, date, assessId));
            cursor.moveToNext();
        }
        cursor.close();
        return goalDates;
    }

}

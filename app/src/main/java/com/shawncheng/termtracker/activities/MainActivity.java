package com.shawncheng.termtracker.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.shawncheng.termtracker.*;
import com.shawncheng.termtracker.activities.terms_activities.TermsListActivity;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Assessment;
import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.GoalDate;
import com.shawncheng.termtracker.model.Mentor;
import com.shawncheng.termtracker.model.Term;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //
//        //Start Insert Test
        DBOpenHelper mDBHelper = new DBOpenHelper(this);
//        String name = "Term1";
//        String startDate = "2018-01-01";
//        String endDate = "2018-03-01";
//        mDBHelper.insertTerm(name, startDate, endDate);
//        Log.d(TAG, "TEST: Inserted term name: " + name + " startDate: " + startDate + " endDate: " + endDate);
//
//        //Start Retrieve all terms test
//        ArrayList<Term> termsList = mDBHelper.getTerms();
//        Log.d(TAG, "TEST: Retrieving arrayList for terms, first item is " + termsList.get(0).getTermName());
//        Log.d(TAG, "TEST: Term ID of first item is " + termsList.get(0).getTermId());
//
//        // Create mentor and add to list
//        Mentor newMentor = new Mentor("Mentor2", "626-607-3569", "mentor2@gmail.com");
//        ArrayList<Mentor> mentorList = new ArrayList<Mentor>();
//        mentorList.add(newMentor);
//
//        //Insert Sample course
//        mDBHelper.insertCourse("Course2", "2018-02-01", "2018-03-01", "In progress", termsList.get(0).getTermId(), mentorList);
//        Log.d(TAG, "TEST: Inserted course: " + "Course2");
//
//        //Getting courses
//        ArrayList<Course> courseList = mDBHelper.getCourses(termsList.get(0).getTermId());
//        Log.d(TAG, "TEST: retrieved course list, second item is: " + courseList.get(1).getTitle());

        // Insert assessments
//        GoalDate goalDate1 = new GoalDate("GoalDate Description", "2018-06-01");
//        ArrayList<GoalDate> goalDateArrayList = new ArrayList<>();
//        goalDateArrayList.add(goalDate1);
//
//        mDBHelper.insertAssessment("Assessment1", "Objective", "2018-07-01", 1);
//        Log.d(TAG, "TEST: Starting Testing");
//        ArrayList<Assessment> assessmentArrayList = mDBHelper.getAssessments(1);
//        Log.d(TAG, "TEST: First assessment in list: " + assessmentArrayList.get(0).getTitle());
//        mDBHelper.insertGoal("2018-02-01", 1);
        GoalDate goal = mDBHelper.getGoal(1);
        Log.d(TAG, "TEST: Goal date retrieved was: " + goal.getDate());

        // Remove assessment
        //mDBHelper.deleteAssessment(assessmentArrayList.get(0).getAssessmentId());



    }

    public void handleViewTerms(View view) {
        Intent intent = new Intent(this, TermsListActivity.class);
        startActivity(intent);
    }
}
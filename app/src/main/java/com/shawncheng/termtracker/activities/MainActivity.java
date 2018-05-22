package com.shawncheng.termtracker.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.shawncheng.termtracker.*;
import com.shawncheng.termtracker.activities.terms_activities.TermsListActivity;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Term;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start Insert Test
//        DBOpenHelper mDBHelper = new DBOpenHelper(this);
//        String name = "Term2";
//        String startDate = "2018-06-01";
//        String endDate = "2018-09-01";
//        mDBHelper.insertTerm(name, startDate, endDate);
//        Log.d(TAG, "testInsertTerm: Inserted term name: " + name + " startDate: " + startDate + " endDate: " + endDate);
//        //End Insert Test

        //Start Retrieve all terms test
//        ArrayList<Term> termsList = mDBHelper.getTerms();
//        Log.d(TAG, "Retrieving arrayList for terms, second item is " + termsList.get(1).getTermName());

    }

    public void handleViewTerms(View view) {
        Intent intent = new Intent(this, TermsListActivity.class);
        startActivity(intent);
    }
}

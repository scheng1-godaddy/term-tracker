package com.shawncheng.termtracker.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.activities.terms_activities.TermsListActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void handleViewTerms(View view) {
        Intent intent = new Intent(this, TermsListActivity.class);
        startActivity(intent);
    }
}

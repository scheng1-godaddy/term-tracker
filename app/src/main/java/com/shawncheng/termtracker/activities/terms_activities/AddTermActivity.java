package com.shawncheng.termtracker.activities.terms_activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shawncheng.termtracker.R;

public class AddTermActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        setTitle("Add Term");
    }
}
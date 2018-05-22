package com.shawncheng.termtracker.activities.terms_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.shawncheng.termtracker.R;

public class TermAddActivity extends AppCompatActivity {

    private String termType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        resolveTitle();
    }

    private void resolveTitle() {
        Intent intent = getIntent();
        this.termType = intent.getStringExtra("type");
        Log.d("TermAddActivity", "onCreate: Type is: " + termType);
        if (termType.equals("add")) {
            setTitle("Add Term");
        } else {
            setTitle("Modify Term");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term_addmodify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cancel:
                // Switch activity to add term activity
                if (termType.equals("add")) {
                    Intent intent = new Intent(this, TermsListActivity.class);
                    startActivity(intent);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

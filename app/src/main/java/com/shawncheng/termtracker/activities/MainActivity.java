package com.shawncheng.termtracker.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.shawncheng.termtracker.*;
import com.shawncheng.termtracker.activities.terms_activities.TermAddActivity;
import com.shawncheng.termtracker.activities.terms_activities.TermDetailActivity;
import com.shawncheng.termtracker.adapters.TermsListAdapter;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Term;
import static com.shawncheng.termtracker.util.IntentConstants.*;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TermsListActivity";
    private DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_list);
        setTitle("Terms Listing");
        populateTermListView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        populateTermListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                // Switch activity to add term activity
                Intent intent = new Intent(this, TermAddActivity.class);
                intent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_ADD);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Populate the listView
    private void populateTermListView() {
        Log.d(TAG, "populateListView: Retreiving terms to populate ListView");
        this.dbOpenHelper = new DBOpenHelper(this);
        final ArrayList<Term> termsList = dbOpenHelper.getTerms();
        ListAdapter termsListAdapter = new TermsListAdapter(this, termsList);
        ListView listView = findViewById(R.id.lv_terms);
        listView.setAdapter(termsListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Term termItem = (Term) parent.getAdapter().getItem(position);
                changeActivity(termItem);
            }
        });
    }

    private void changeActivity(Term term) {
        Intent intent = new Intent(this, TermDetailActivity.class);
        intent.putExtra(INTENT_TAG_TERM, term);
        startActivity(intent);
    }
}
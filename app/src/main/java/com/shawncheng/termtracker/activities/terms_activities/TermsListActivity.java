package com.shawncheng.termtracker.activities.terms_activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import com.shawncheng.termtracker.adapters.TermsListAdapter;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Term;

import java.util.ArrayList;


public class TermsListActivity extends AppCompatActivity {

    private static final String TAG = "TermsListActivity";
    private DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_list);
        setTitle("Terms Listing");
        populateTermListView();
    }

    /**
     * Create menu (only add button)
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term_list, menu);
        return true;
    }

    /**
     * Menu logic
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                // Switch activity to add term activity
                Intent intent = new Intent(this, TermAddActivity.class);
                intent.putExtra("type", "add");
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
        ListView termsListView = findViewById(R.id.lv_terms);
        termsListView.setAdapter(termsListAdapter);
        termsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Term term = (Term) parent.getAdapter().getItem(position);
                switchDetailActivity(term);
            }
        });
    }

    private void switchDetailActivity(Term term) {
        Intent intent = new Intent(this, TermDetailActivity.class);
        intent.putExtra("termName", term.getTermName());
        startActivity(intent);
    }
}

package com.shawncheng.termtracker.activities.terms_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Term;

public class TermDetailActivity extends AppCompatActivity {

    private static final String TAG = "TermDetailActivity";
    Term activeTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        // Get info on the term
        Intent intent = getIntent();
        this.activeTerm = (Term) intent.getSerializableExtra("term");
        Log.d(TAG, "onCreate: Active term is: " + activeTerm.getTermName());

        // Display term info
        setInput();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_edit_term:
                Intent intent = new Intent(this, TermAddActivity.class);
                intent.putExtra("type", "modify");
                intent.putExtra("term", activeTerm);
                startActivity(intent);
                break;
            case R.id.menu_delete_term:
                deleteTerm();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteTerm() {
        //TODO create delete term after creating courses, you need to check if term has any courses before deleting
    }

    private void setInput() {
        TextView termName = findViewById(R.id.term_name);
        TextView startDate = findViewById(R.id.term_start_date);
        TextView endDate = findViewById(R.id.term_end_date);
        termName.setText(activeTerm.getTermName());
        startDate.setText(activeTerm.getStartDate());
        endDate.setText(activeTerm.getEndDate());
    }
}

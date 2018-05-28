package com.shawncheng.termtracker.activities.terms_activities;

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
import android.widget.TextView;
import android.widget.Toast;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.activities.course_activities.CourseAddActivity;
import com.shawncheng.termtracker.activities.course_activities.CourseDetailActivity;
import com.shawncheng.termtracker.adapters.CourseListAdapter;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.Term;
import static com.shawncheng.termtracker.util.IntentConstants.*;

import java.util.ArrayList;

public class TermDetailActivity extends AppCompatActivity {

    private static final String TAG = "TermDetailActivity";
    private Term activeTerm;
    private DBOpenHelper dbOpenHelper;
    private ArrayList<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        setTitle("Term Details");

        // Get info on the term
        Intent intent = getIntent();
        this.activeTerm = (Term) intent.getSerializableExtra(INTENT_TAG_TERM);
        Log.d(TAG, "onCreate: Active term is: " + activeTerm.getTermName());

        // Display term info
        setInput();

        // Display courses for the term
        dbOpenHelper = new DBOpenHelper(this);
        setCourseListView();
    }


    @Override
    protected void onRestart() {
        super.onResume();
        setCourseListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit_term:
                Intent intent = new Intent(this, TermAddActivity.class);
                intent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_MODIFY);
                intent.putExtra(INTENT_TAG_TERM, activeTerm);
                startActivity(intent);
                break;
            case R.id.menu_delete_term:
                deleteTerm();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteTerm() {
        if (isCourseListEmpty()) {
            if (dbOpenHelper.deleteTerm(activeTerm.getTermId())) {
                Toast.makeText(getBaseContext(), "Term successfully removed", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getBaseContext(), "Term could not be removed", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getBaseContext(), "Removal failed: must remove courses first", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isCourseListEmpty() {
        ArrayList<Course> courseList = dbOpenHelper.getCourses(activeTerm.getTermId());
        return courseList.isEmpty();
    }

    private void setInput() {
        TextView termName = findViewById(R.id.term_name);
        TextView startDate = findViewById(R.id.term_start_date);
        TextView endDate = findViewById(R.id.term_end_date);
        termName.setText(activeTerm.getTermName());
        startDate.setText(activeTerm.getStartDate());
        endDate.setText(activeTerm.getEndDate());
    }

    private void setCourseListView() {
        this.courseList = dbOpenHelper.getCourses(this.activeTerm.getTermId());
        Log.d(TAG, "course list retrieved, the length is: " + courseList.size());
        ListAdapter listAdapter = new CourseListAdapter(this, courseList);
        ListView courseListView = findViewById(R.id.courses_list_view);
        courseListView.setAdapter(listAdapter);
        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course = (Course) parent.getAdapter().getItem(position);
                switchActivity(CourseDetailActivity.class, course);
            }
        });
    }

    private void switchActivity(Class<?> detailClass, Object obj) {
        Intent intent = new Intent(this, detailClass);
        if (detailClass.equals(CourseDetailActivity.class)) {
            Course course = (Course) obj;
            intent.putExtra(INTENT_TAG_COURSE, course);
        }
        if (detailClass.equals(CourseAddActivity.class)) {
            intent.putExtra(INTENT_TAG_TERM, activeTerm);
            intent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_ADD);
        }
        startActivity(intent);
    }

    public void addCourseButtonHandler(android.view.View myview) {
        switchActivity(CourseAddActivity.class, null);
    }

}

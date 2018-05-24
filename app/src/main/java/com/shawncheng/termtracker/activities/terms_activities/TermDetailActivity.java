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

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.activities.course_activities.CourseDetailActivity;
import com.shawncheng.termtracker.adapters.CourseListAdapter;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.Term;

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

        // Get info on the term
        Intent intent = getIntent();
        this.activeTerm = (Term) intent.getSerializableExtra("term");
        Log.d(TAG, "onCreate: Active term is: " + activeTerm.getTermName());

        // Display term info
        setInput();

        // Display courses for the term
        dbOpenHelper = new DBOpenHelper(this);
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
                switchActivity(course);
            }
        });
    }

    private void switchActivity(Course course) {
        //TODO finish switchactivity after finishing class for course details
        Intent intent = new Intent(this, CourseDetailActivity.class);
        intent.putExtra("course", course);
        startActivity(intent);
    }

}

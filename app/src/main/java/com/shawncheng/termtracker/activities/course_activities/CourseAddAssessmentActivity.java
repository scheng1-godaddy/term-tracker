package com.shawncheng.termtracker.activities.course_activities;

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

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.activities.assessment_activities.AssessmentAddActivity;
import com.shawncheng.termtracker.activities.assessment_activities.AssessmentDetailActivity;
import com.shawncheng.termtracker.activities.terms_activities.TermDetailActivity;
import com.shawncheng.termtracker.adapters.AssessmentListAdapter;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Assessment;
import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.Term;
import com.shawncheng.termtracker.util.Util;

import static com.shawncheng.termtracker.util.TermTrackerConstants.*;

import java.util.ArrayList;

public class CourseAddAssessmentActivity extends AppCompatActivity {

    private static final String TAG = "CourseAddAssessment";
    private Intent intent;
    private DBOpenHelper dbOpenHelper;
    private Course activeCourse;
    private ArrayList<Assessment> assessmentArrayList;
    private Term activeTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_add_assessment);
        intent = getIntent();
        activeCourse = (Course) intent.getSerializableExtra(INTENT_TAG_COURSE);
        activeTerm = (Term) intent.getSerializableExtra(INTENT_TAG_TERM);
        Log.d(TAG, "[Intent Tracking] intent name is: " + activeCourse.getTitle() +  ", Intent ID is: " + activeCourse.getCourseId());
        setTitle(activeCourse.getTitle());
        dbOpenHelper = new DBOpenHelper(this);
        setAssessmentListView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setAssessmentListView();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_add_assessment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_course_add_assessment_finish:
                changeActivity(TermDetailActivity.class, null);
                break;
        }
        return true;
    }


    private void setAssessmentListView() {
        assessmentArrayList = dbOpenHelper.getAssessments(activeCourse.getCourseId());
        ListView listView = findViewById(R.id.list_view_course_add_assessments);
        ListAdapter listAdapter = new AssessmentListAdapter(this, assessmentArrayList);
        listView.setAdapter(listAdapter);
        Util.setListViewHeightBasedOnChildren(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Assessment assessment = (Assessment) parent.getAdapter().getItem(position);
                changeActivity(AssessmentDetailActivity.class, assessment);
            }
        });
    }

    private void changeActivity(Class<?> activityClass, Object obj) {
        Intent intent = new Intent(this, activityClass);
        if (activityClass.equals(AssessmentDetailActivity.class)) {
            Assessment assessment = (Assessment) obj;
            intent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_VIEW);
            intent.putExtra(INTENT_TAG_ASSESSMENT, assessment);
        }
        if (activityClass.equals(AssessmentAddActivity.class)) {
            intent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_ADD);
            intent.putExtra(INTENT_TAG_COURSE, activeCourse);
        }
        if (activityClass.equals(TermDetailActivity.class)) {
            intent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_ADD);
            intent.putExtra(INTENT_TAG_TERM, activeTerm);
        }
        startActivity(intent);
    }

    public void addAssessmentButtonHandler(View view) {
        changeActivity(AssessmentAddActivity.class, null);
    }
}

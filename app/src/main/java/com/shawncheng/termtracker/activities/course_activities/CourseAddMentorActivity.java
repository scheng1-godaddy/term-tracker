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
import com.shawncheng.termtracker.activities.mentor_activities.MentorAddActivity;
import com.shawncheng.termtracker.activities.mentor_activities.MentorDetailActivity;
import com.shawncheng.termtracker.adapters.MentorsListAdapter;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.Mentor;
import com.shawncheng.termtracker.model.Term;

import static com.shawncheng.termtracker.util.IntentConstants.*;

import java.util.ArrayList;

public class CourseAddMentorActivity extends AppCompatActivity {


    private static final String TAG = "CourseAddMentorActivity";
    private Intent intent;
    private DBOpenHelper dbOpenHelper;
    private Course activeCourse;
    private Term activeTerm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_add_mentor);
        intent = getIntent();
        activeCourse = (Course) intent.getSerializableExtra(INTENT_TAG_COURSE);
        activeTerm = (Term) intent.getSerializableExtra(INTENT_TAG_TERM);
        Log.d(TAG, "[Intent Tracking] intent name is: " + activeCourse.getTitle() +  ", Intent ID is: " + activeCourse.getCourseId());

        setTitle(activeCourse.getTitle());

        dbOpenHelper = new DBOpenHelper(this);

        setMentorListView();
    }

    //TODO Have to put a menu here for next... we just want to skip to the next screen... I don't think we need to put a cancel button.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_add_mentor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_course_add_mentor_next:
                changeActivity(CourseAddAssessmentActivity.class, null);
                break;
        }
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setMentorListView();
    }

    private void setMentorListView() {
        ArrayList<Mentor> mentorArrayList = dbOpenHelper.getMentors(activeCourse.getCourseId());
        ListView listView = findViewById(R.id.list_view_course_add_mentors);
        ListAdapter listAdapter = new MentorsListAdapter(this, mentorArrayList);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mentor mentor = (Mentor) parent.getAdapter().getItem(position);
                changeActivity(MentorDetailActivity.class, mentor);
            }
        });
    }

    public void addMentorButtonHandler(View view) {
        changeActivity(MentorAddActivity.class, null);
    }

    private void changeActivity(Class<?> activityClass, Object obj) {
        Intent intent = new Intent(this, activityClass);
        if (activityClass.equals(MentorDetailActivity.class)) {
            Mentor mentor = (Mentor) obj;
            intent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_VIEW);
            intent.putExtra(INTENT_TAG_MENTOR, mentor);
        }
        if (activityClass.equals(MentorAddActivity.class)) {
            intent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_ADD);
            intent.putExtra(INTENT_TAG_COURSEID, activeCourse.getCourseId());
            intent.putExtra(INTENT_TAG_ORIGIN, intent.getStringExtra(INTENT_TAG_ORIGIN));
        }
        if (activityClass.equals(CourseAddAssessmentActivity.class)) {
            Course course = (Course) obj;
            intent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_ADD);
            intent.putExtra(INTENT_TAG_COURSE, activeCourse);
            intent.putExtra(INTENT_TAG_TERM, activeTerm);
            intent.putExtra(INTENT_TAG_ORIGIN, intent.getStringExtra(INTENT_TAG_ORIGIN));
        }
        startActivity(intent);
    }

}

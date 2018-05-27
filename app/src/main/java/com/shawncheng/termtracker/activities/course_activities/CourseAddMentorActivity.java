package com.shawncheng.termtracker.activities.course_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;

public class CourseAddMentorActivity extends AppCompatActivity {

    private Intent intent;
    private DBOpenHelper dbOpenHelper;
    private Course activeCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_add_mentor);
        intent = getIntent();
        activeCourse = (Course) intent.getSerializableExtra("course");

        setTitle(activeCourse.getTitle());

        dbOpenHelper = new DBOpenHelper(this);

        setMentorListView();
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
            intent.putExtra("type", "view");
            intent.putExtra("mentor", mentor);
        }
        if (activityClass.equals(MentorAddActivity.class)) {
            intent.putExtra("type", "add");
            intent.putExtra("courseId", activeCourse.getCourseId());
        }
        startActivity(intent);
    }

}

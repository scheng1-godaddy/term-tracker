package com.shawncheng.termtracker.activities.course_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.activities.assessment_activities.AssessmentDetailActivity;
import com.shawncheng.termtracker.activities.mentor_activities.MentorAddActivity;
import com.shawncheng.termtracker.adapters.AssessmentListAdapter;
import com.shawncheng.termtracker.adapters.MentorsListAdapter;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Assessment;
import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.Mentor;

import java.util.ArrayList;

public class CourseDetailActivity extends AppCompatActivity {

    private Course activeCourse;
    private DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        setTitle("Course Details");

        Intent intent = getIntent();
        activeCourse = (Course) intent.getSerializableExtra("course");
        dbOpenHelper = new DBOpenHelper(this);

        setInputs();
        setAssessmentListView();
        setMentorListView();
    }



    //TODO implement menu for course detail (I'm assuming you need to put edit and delete.

    private void setInputs() {
        TextView title = findViewById(R.id.course_detail_title);
        TextView startDate = findViewById(R.id.course_detail_start_value);
        TextView endDate = findViewById(R.id.course_detail_end_date_value);
        TextView status = findViewById(R.id.course_detail_status_value);
        title.setText(activeCourse.getTitle());
        startDate.setText(activeCourse.getStartDate());
        endDate.setText(activeCourse.getEndDate());
        status.setText(activeCourse.getStatus());
    }

    private void setAssessmentListView() {
        ArrayList<Assessment> assessmentArrayList = dbOpenHelper.getAssessments(activeCourse.getCourseId());
        ListView listView = findViewById(R.id.list_view_assessments);
        ListAdapter listAdapter = new AssessmentListAdapter(this, assessmentArrayList);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Assessment assessment = (Assessment) parent.getAdapter().getItem(position);
                switchDetailActivity(AssessmentDetailActivity.class, assessment);
            }
        });
    }

    private void setMentorListView() {
        ArrayList<Mentor> mentorArrayList = dbOpenHelper.getMentors(activeCourse.getCourseId());
        ListView listView = findViewById(R.id.list_view_mentors);
        ListAdapter listAdapter = new MentorsListAdapter(this, mentorArrayList);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mentor mentor = (Mentor) parent.getAdapter().getItem(position);
                switchDetailActivity(MentorAddActivity.class, mentor);
            }
        });
    }

    private void switchDetailActivity(Class<?> detailClass, Object obj) {
        Intent intent = new Intent(this, detailClass);
        if (detailClass.equals(AssessmentDetailActivity.class)) {
            Assessment assessment = (Assessment) obj;
            intent.putExtra("assessment", assessment);
        } else if (detailClass.equals(MentorAddActivity.class)) {
            Mentor mentor = (Mentor) obj;
            intent.putExtra("mentor", mentor);
            intent.putExtra("type", "view");
        }
        startActivity(intent);
    }

}

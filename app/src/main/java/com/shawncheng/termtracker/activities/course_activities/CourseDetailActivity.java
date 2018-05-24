package com.shawncheng.termtracker.activities.course_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.model.Course;

public class CourseDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        setTitle("Course Details");

        Intent intent = getIntent();
        Course course = (Course) intent.getSerializableExtra("course");

        TextView title = findViewById(R.id.course_detail_title);
        title.setText(course.getTitle());
    }
}

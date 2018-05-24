package com.shawncheng.termtracker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.model.Course;

import java.util.ArrayList;

public class CourseListAdapter extends ArrayAdapter<Course> {

    public CourseListAdapter(@NonNull Context context, ArrayList<Course> courseList) {
        super(context, R.layout.viewlist_course_row, courseList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.viewlist_course_row, parent, false);
            Course courseItem = getItem(position);
            TextView courseNameField = convertView.findViewById(R.id.course_row_name);
            courseNameField.setText(courseItem.getTitle());
        }
        return convertView;
    }
}

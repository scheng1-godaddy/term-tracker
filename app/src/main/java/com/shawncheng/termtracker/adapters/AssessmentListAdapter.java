package com.shawncheng.termtracker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.model.Assessment;
import com.shawncheng.termtracker.model.Course;

import java.util.ArrayList;


public class AssessmentListAdapter extends ArrayAdapter<Assessment> {

    private static final String TAG = "AssessmentListAdapter";

    public AssessmentListAdapter(@NonNull Context context, ArrayList<Assessment> assessmentList) {
        super(context, R.layout.viewlist_assessment_row, assessmentList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.viewlist_assessment_row, parent, false);
            Assessment assessmentItem = getItem(position);
            Log.d(TAG, "getView: received the following assessmentItem: " + assessmentItem.getTitle());
            TextView assessmentNameField = convertView.findViewById(R.id.assessment_list_name);
            assessmentNameField.setText(assessmentItem.getTitle());
        }
        return convertView;
    }
}

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
import com.shawncheng.termtracker.model.GoalDate;

import java.util.ArrayList;
import java.util.List;

public class GoalListAdapter extends ArrayAdapter<GoalDate> {
    //TODO Might need to remove goal list adapter

    public GoalListAdapter(@NonNull Context context, @NonNull ArrayList<GoalDate> goalDateArrayList) {
        super(context, R.layout.viewlist_goal_row, goalDateArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.viewlist_goal_row, parent, false);
            GoalDate goalDateItem = getItem(position);
            TextView goalDate = convertView.findViewById(R.id.goal_list_date);
            goalDate.setText(goalDateItem.getDate());
        }
        return convertView;
    }

}
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
import com.shawncheng.termtracker.model.Mentor;

import java.util.ArrayList;
import java.util.List;

public class MentorsListAdapter extends ArrayAdapter<Mentor> {
    public MentorsListAdapter(@NonNull Context context, @NonNull ArrayList<Mentor> objects) {
        super(context, R.layout.viewlist_mentor_row, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.viewlist_mentor_row, parent, false);
            Mentor mentor = getItem(position);
            TextView name = convertView.findViewById(R.id.mentor_list_name);
            name.setText(mentor.getName());
        }
        return convertView;
    }
}

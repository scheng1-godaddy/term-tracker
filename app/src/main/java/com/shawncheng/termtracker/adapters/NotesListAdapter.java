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
import com.shawncheng.termtracker.model.Note;

import java.util.ArrayList;

public class NotesListAdapter extends ArrayAdapter<Note> {
    public NotesListAdapter(@NonNull Context context, @NonNull ArrayList<Note> objects) {
        super(context, R.layout.viewlist_note_row, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.viewlist_note_row, parent, false);
            Note note = getItem(position);
            TextView noteField = convertView.findViewById(R.id.note_list_text);
            noteField.setText(note.getNote());
        }
        return convertView;
    }
}
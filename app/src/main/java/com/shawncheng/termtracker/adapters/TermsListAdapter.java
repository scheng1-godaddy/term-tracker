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
import com.shawncheng.termtracker.model.Term;

import java.util.ArrayList;

public class TermsListAdapter extends ArrayAdapter<Term> {

    public TermsListAdapter(@NonNull Context context, ArrayList<Term> termsList) {
        super(context, R.layout.viewlist_term_row, termsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.viewlist_term_row, parent, false);
            Term termItem = getItem(position);
            TextView termTextView = convertView.findViewById(R.id.term_row_name);
            TextView termStartDateView = convertView.findViewById(R.id.term_row_start_date);
            TextView termEndDateView = convertView.findViewById(R.id.term_row_end_date);
            termTextView.setText(termItem.getTermName());
            termStartDateView.setText(termItem.getStartDate());
            termEndDateView.setText(termItem.getEndDate());
        }
        return convertView;
    }
}

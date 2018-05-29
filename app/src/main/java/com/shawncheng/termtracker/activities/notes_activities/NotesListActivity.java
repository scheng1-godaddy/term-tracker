package com.shawncheng.termtracker.activities.notes_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.adapters.NotesListAdapter;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.Note;
import com.shawncheng.termtracker.util.Util;
import static com.shawncheng.termtracker.util.IntentConstants.*;

import java.util.ArrayList;

public class NotesListActivity extends AppCompatActivity {

    private static final String TAG = "NotesListActivity";
    private Course activeCourse;
    private Intent intent;
    private DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        dbOpenHelper = new DBOpenHelper(this);
        intent = getIntent();
        activeCourse = (Course) intent.getSerializableExtra(INTENT_TAG_COURSE);

        setTitle("Notes for " + activeCourse.getTitle());

        populateNotesListView();
    }

    //TODO create onRestart and repopulate list

    private void populateNotesListView() {
        Log.d(TAG, "populateListView: Retreiving terms to populate ListView");
        this.dbOpenHelper = new DBOpenHelper(this);
        final ArrayList<Note> notesList = dbOpenHelper.getNotes(activeCourse.getCourseId());
        ListAdapter notesListAdapter = new NotesListAdapter(this, notesList);
        ListView listView = findViewById(R.id.lv_terms);
        listView.setAdapter(notesListAdapter);
        Util.setListViewHeightBasedOnChildren(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = (Note) parent.getAdapter().getItem(position);
                Intent newIntent = new Intent(getBaseContext(), NoteDetailActivity.class);
                newIntent.putExtra(INTENT_TAG_COURSE, activeCourse);
                newIntent.putExtra(INTENT_TAG_NOTE, note);
                startActivity(newIntent);
            }
        });
    }
}

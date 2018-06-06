package com.shawncheng.termtracker.activities.notes_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.Note;

import static com.shawncheng.termtracker.util.TermTrackerConstants.*;


public class NoteAddActivity extends AppCompatActivity {

    private Course activeCourse;
    private EditText noteInput;
    private DBOpenHelper dbOpenHelper;
    private Note activeNote;
    private boolean isAdd;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);
        dbOpenHelper = new DBOpenHelper(this);
        intent = getIntent();
        activeCourse = (Course) intent.getSerializableExtra(INTENT_TAG_COURSE);
        noteInput = findViewById(R.id.note_add_input);
        resolveType();
    }

    private void resolveType() {
        if (intent.getStringExtra(INTENT_TAG_TYPE).equals(INTENT_VALUE_ADD)) {
            this.isAdd = true;
        }
        if (isAdd) {
            setTitle("Add note for " + activeCourse.getTitle());
        } else {
            setTitle("Modify note for " + activeCourse.getTitle());
            activeNote = (Note) intent.getSerializableExtra(INTENT_TAG_NOTE);
            setInputs();
        }
    }

    private void setInputs() {
        this.noteInput.setText(activeNote.getNote());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_note_add_save:
                if (isAdd) {
                    if (insertNote()) {
                        finish();
                    }
                } else {
                    if (updateNote()) {
                        finish();
                    }
                }
                break;
            case R.id.menu_note_add_cancel:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean updateNote() {
        if (!(noteInput.getText().toString().trim().isEmpty())) {
            int retVal = dbOpenHelper.updateNote(activeNote.getNoteId(), noteInput.getText().toString());
            if (retVal != 0) {
                Toast.makeText(getBaseContext(), "Note updated", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(getBaseContext(), "Note could not be updated", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(getBaseContext(), "No notes to update", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean insertNote() {
        if (!(noteInput.getText().toString().trim().isEmpty())) {
            int id = (int) dbOpenHelper.insertNote(activeCourse.getCourseId(), noteInput.getText().toString());
            if (id != -1) {
                Toast.makeText(getBaseContext(), "Note added", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(getBaseContext(), "Note could not be added", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(getBaseContext(), "No notes to add", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}

package com.shawncheng.termtracker.activities.mentor_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.Mentor;

import static com.shawncheng.termtracker.util.TermTrackerConstants.*;

import java.util.ArrayList;

public class MentorAddActivity extends AppCompatActivity {

    private static final String TAG = "MentorAddActivity";

    private EditText nameInput;
    private EditText phoneInput;
    private EditText emailInput;
    private String mentorType;
    private Mentor activeMentor;
    private DBOpenHelper dbOpenHelper;
    private Mentor newMentor;
    private Course activeCourse;
    private ArrayList<Mentor> mentorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_add);
        dbOpenHelper = new DBOpenHelper(this);

        nameInput = findViewById(R.id.mentor_add_name);
        phoneInput = findViewById(R.id.mentor_add_phone_value);
        emailInput = findViewById(R.id.mentor_add_email_value);

        Intent intent = getIntent();
        mentorType = intent.getStringExtra(INTENT_TAG_TYPE);
        if (mentorType.equals(INTENT_VALUE_MODIFY)) {
            this.activeMentor = (Mentor) intent.getSerializableExtra(INTENT_TAG_MENTOR);
            setInputs(activeMentor);
            setTitle("Edit Mentor");
        } else {
            setTitle("Add Mentor");
        }
        activeCourse = (Course) intent.getSerializableExtra(INTENT_TAG_COURSE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mentor_addmodify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_mentor_addmodify_cancel:
                finish();
                break;
            case R.id.menu_mentor_addmodify_save:
                saveMentor();
        }
        return true;
    }

    private void setInputs(Mentor activeMentor) {
        nameInput.setText(activeMentor.getName());
        phoneInput.setText(activeMentor.getPhone());
        emailInput.setText(activeMentor.getEmail());
    }

    public void saveMentor() {
        if (validateInput()) {
            if (this.mentorType.equals(INTENT_VALUE_ADD)) {
                long mentorId = this.dbOpenHelper.insertMentor(newMentor.getName(), newMentor.getPhone(), newMentor.getEmail(), activeCourse.getCourseId());
                if (mentorId != -1) {
                    Toast.makeText(getBaseContext(), "Mentor successfully added", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Failed to add mentor", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (this.dbOpenHelper.updateMentor(activeMentor.getId(), newMentor.getName(), newMentor.getPhone(), newMentor.getEmail(), newMentor.getCourseId())) {
                    Toast.makeText(getBaseContext(), "Mentor successfully updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Failed to update mentor", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean validateInput() {
        this.newMentor = new Mentor();
        Log.d(TAG, "validateInput: Validating input for " + this.nameInput.getText().toString());
        if(this.nameInput.getText().toString().trim().isEmpty()) {
            Toast.makeText(getBaseContext(), "Please enter a name for mentor", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            newMentor.setName(this.nameInput.getText().toString());
        }
        if(this.phoneInput.getText().toString().trim().isEmpty()) {
            Toast.makeText(getBaseContext(), "Please enter a phone number", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            newMentor.setPhone(this.phoneInput.getText().toString());
        }
        if(this.emailInput.getText().toString().trim().isEmpty()) {
            Toast.makeText(getBaseContext(), "Please enter an email address", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            newMentor.setEmail(this.emailInput.getText().toString());
            newMentor.setCourseId(activeCourse.getCourseId());
        }
        return true;
    }

}

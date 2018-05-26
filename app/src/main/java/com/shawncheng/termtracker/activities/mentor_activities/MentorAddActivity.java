package com.shawncheng.termtracker.activities.mentor_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.model.Mentor;

public class MentorAddActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText phoneInput;
    private EditText emailInput;
    Mentor activeMentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_add);


        Intent intent = getIntent();
        if (intent.getStringExtra("type").equals("edit")) {
            this.activeMentor = (Mentor) intent.getSerializableExtra("mentor");
            setInputs(activeMentor);
            setTitle("Edit Mentor");
        } else {
            setTitle("Add Mentor");
        }
    }

    private void setInputs(Mentor activeMentor) {
        nameInput = findViewById(R.id.mentor_add_name);
        phoneInput = findViewById(R.id.mentor_add_phone_value);
        emailInput = findViewById(R.id.mentor_add_email_value);
        nameInput.setText(activeMentor.getName());
        phoneInput.setText(activeMentor.getPhone());
        emailInput.setText(activeMentor.getEmail());
    }
}

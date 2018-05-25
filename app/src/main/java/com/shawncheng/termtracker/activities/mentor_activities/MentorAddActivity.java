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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_add);
        setTitle("Mentor Details");

        Intent intent = getIntent();

        if (intent.getStringExtra("type").equals("view")) {
            Mentor activeMentor = (Mentor) intent.getSerializableExtra("mentor");
            setInputs(activeMentor);
        }
    }

    private void setInputs(Mentor activeMentor) {
        nameInput = findViewById(R.id.mentor_name);
        phoneInput = findViewById(R.id.mentor_phone_value);
        emailInput = findViewById(R.id.mentor_email_value);
        nameInput.setText(activeMentor.getName());
        phoneInput.setText(activeMentor.getPhone());
        emailInput.setText(activeMentor.getEmail());
    }
}

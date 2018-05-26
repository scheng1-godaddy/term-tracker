package com.shawncheng.termtracker.activities.mentor_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.model.Mentor;

public class MentorDetailActivity extends AppCompatActivity {

    private TextView nameInput;
    private TextView phoneInput;
    private TextView emailInput;
    private Intent intent;
    private Mentor activeMentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_detail);

        this.intent = getIntent();
        this.activeMentor = (Mentor) intent.getSerializableExtra("mentor");

        setInputs(activeMentor);

    }

    private void setInputs(Mentor activeMentor) {
        nameInput = findViewById(R.id.mentor_detail_name);
        phoneInput = findViewById(R.id.mentor_detail_phone_value);
        emailInput = findViewById(R.id.mentor_detail_email_value);
        nameInput.setText(activeMentor.getName());
        phoneInput.setText(activeMentor.getPhone());
        emailInput.setText(activeMentor.getEmail());
    }
}

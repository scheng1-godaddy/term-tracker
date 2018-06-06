package com.shawncheng.termtracker.activities.mentor_activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.Mentor;

import static com.shawncheng.termtracker.util.TermTrackerConstants.*;

public class MentorDetailActivity extends AppCompatActivity {

    private TextView nameInput;
    private TextView phoneInput;
    private TextView emailInput;
    private Intent intent;
    private Mentor activeMentor;
    private Course activeCourse;
    private DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_detail);

        this.intent = getIntent();
        this.activeMentor = (Mentor) intent.getSerializableExtra(INTENT_TAG_MENTOR);
        this.activeCourse = (Course) intent.getSerializableExtra(INTENT_TAG_COURSE);

        dbOpenHelper = new DBOpenHelper(this);
        setInputs();
        addDeleteButton();
    }

    private void setInputs() {
        nameInput = findViewById(R.id.mentor_detail_name);
        phoneInput = findViewById(R.id.mentor_detail_phone_value);
        emailInput = findViewById(R.id.mentor_detail_email_value);
        nameInput.setText(activeMentor.getName());
        phoneInput.setText(activeMentor.getPhone());
        emailInput.setText(activeMentor.getEmail());
    }

    private void addDeleteButton() {
        RelativeLayout rlayout = findViewById(R.id.mentor_detail_relativelayout);
        Button dbutton = new Button(this);
        dbutton.setText("Delete Mentor");
        dbutton.setTextColor(Color.WHITE);
        dbutton.setBackgroundColor(Color.RED);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.mentor_detail_email_row);
        dbutton.setLayoutParams(params);
        dbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAlertDialog();
            }
        });
        if (rlayout != null) {
            rlayout.addView(dbutton);
        }
    }

    private void displayAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Do you wish to delete Mentor?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteHandler();
                    }
                }).setNegativeButton(android.R.string.no, null).show();
    }

    private void deleteHandler() {
        if (dbOpenHelper.deleteMentor(activeMentor.getId())) {
            Toast.makeText(getBaseContext(), "Mentor successfully deleted", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Mentor could not be removed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        activeMentor = dbOpenHelper.getMentor(activeMentor.getId());
        setInputs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mentor_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_mentor_detail_edit:
                Intent newIntent = new Intent(this, MentorAddActivity.class);
                newIntent.putExtra(INTENT_TAG_COURSE, activeCourse);
                newIntent.putExtra(INTENT_TAG_MENTOR, activeMentor);
                newIntent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_MODIFY);
                startActivity(newIntent);
                break;
            case R.id.menu_mentor_detail_close:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

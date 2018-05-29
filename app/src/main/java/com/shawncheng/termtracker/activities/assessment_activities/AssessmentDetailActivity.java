package com.shawncheng.termtracker.activities.assessment_activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.activities.mentor_activities.MentorAddActivity;
import com.shawncheng.termtracker.adapters.GoalListAdapter;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Assessment;
import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.GoalDate;

import static com.shawncheng.termtracker.util.IntentConstants.*;

import java.util.ArrayList;

public class AssessmentDetailActivity extends AppCompatActivity {

    private static final String TAG = "AssessmentDetail";
    private Assessment activeAssessment;
    private Course activeCourse;
    private DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        setTitle("Assessment Details");

        Intent intent = getIntent();
        this.activeAssessment = (Assessment) intent.getSerializableExtra(INTENT_TAG_ASSESSMENT);
        this.activeCourse = (Course) intent.getSerializableExtra(INTENT_TAG_COURSE);
        Log.d(TAG, "[INTENT TRACKING] activeCourse id and name: " + activeCourse.getCourseId() + " " + activeCourse.getTitle() +
                " Assessment id and name: " + activeAssessment.getAssessmentId() + " " + activeAssessment.getTitle());
        dbOpenHelper = new DBOpenHelper(this);

        setInputs();

        addDeleteButton();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        activeAssessment = dbOpenHelper.getAssessment(activeAssessment.getAssessmentId());
        setInputs();
    }

    private GoalDate retrieveGoal() {
        GoalDate goalDate = dbOpenHelper.getGoal(activeAssessment.getAssessmentId());
        if (goalDate != null && !(goalDate.getDate().trim().isEmpty())) {
            Log.d(TAG, "Retrieved the following goal date: " + goalDate.getDate());
            return goalDate;
        } else {
            Log.d(TAG, "Goal date not found in the database");
            return null;
        }
    }

    private void setInputs() {
        TextView title = findViewById(R.id.assessment_detail_title);
        TextView type = findViewById(R.id.assessment_type_value);
        TextView dueDate = findViewById(R.id.assessment_due_date_value);
        TextView goalDate = findViewById(R.id.assessment_goal_date_value);
        title.setText(activeAssessment.getTitle());
        type.setText(activeAssessment.getType());
        dueDate.setText(activeAssessment.getDueDate());
        GoalDate goalDateValue;
        try {
            goalDateValue = retrieveGoal();
            if (goalDateValue != null && !(goalDateValue.getDate().trim().isEmpty())) {
                goalDate.setText(goalDateValue.getDate());
            }
        } catch (CursorIndexOutOfBoundsException e) {
            Log.d(TAG, "Could not find a goal date for assessment: " + activeAssessment.getTitle());
        }
    }

    private void addDeleteButton() {
        RelativeLayout rlayout = findViewById(R.id.assessment_detail_relativelayout);
        Button dbutton = new Button(this);
        dbutton.setText("Delete Assessment");
        dbutton.setTextColor(Color.WHITE);
        dbutton.setBackgroundColor(Color.RED);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.assessment_goal_date_row);
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
                .setMessage("Do you wish to delete Assessment?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteHandler();
                    }
                }).setNegativeButton(android.R.string.no, null).show();
    }

    private void deleteHandler() {
        if (dbOpenHelper.deleteAssessment(activeAssessment.getAssessmentId())) {
            Toast.makeText(getBaseContext(), "Assessment successfully deleted", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Assessment could not be removed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assessment_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_assessment_detail_edit:
                Intent newIntent = new Intent(this, AssessmentAddActivity.class);
                newIntent.putExtra(INTENT_TAG_ASSESSMENT, activeAssessment);
                newIntent.putExtra(INTENT_TAG_COURSE, activeCourse);
                newIntent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_MODIFY);
                startActivity(newIntent);
                break;
            case R.id.menu_assessment_detail_close:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

package com.shawncheng.termtracker.activities.assessment_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Assessment;
import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.GoalDate;
import com.shawncheng.termtracker.model.Term;
import com.shawncheng.termtracker.util.Util;
import static com.shawncheng.termtracker.util.IntentConstants.*;

public class AssessmentAddActivity extends AppCompatActivity {

    private static final String TAG = "AssessmentAddActivity";
    private Intent intent;
    private boolean isAdd;
    private Course activeCourse;
    private EditText assessmentNameInput;
    private EditText dueDateInput;
    private EditText goalDateInput;
    private Spinner assessmentType;
    private Assessment activeAssessment;
    private Assessment newAssessment;
    private DBOpenHelper dbOpenHelper;
    private GoalDate goalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_add);
        populateTypeSpinner();
        this.intent = getIntent();
        resolveType();
        goalDate = null;
        this.assessmentNameInput = findViewById(R.id.assessment_add_name);
        this.dueDateInput = findViewById(R.id.assessment_add_due_date_value);
        this.goalDateInput = findViewById(R.id.assessment_add_goal_date_value);
        this.assessmentType = findViewById(R.id.assessment_add_type_spinner);
        dbOpenHelper = new DBOpenHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assessment_addmodify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_assessment_addmodify_cancel:
                finish();
                break;
            case R.id.menu_assessment_addmodify_save:
                saveAssessment();
        }
        return true;
    }

    //TODO populate inputs for edit

    private void populateTypeSpinner() {
        Spinner statusDropDown = findViewById(R.id.assessment_add_type_spinner);
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this, R.array.assessment_type_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusDropDown.setAdapter(statusAdapter);
    }

    private void resolveType() {
        if (this.intent.getStringExtra(INTENT_TAG_TYPE).equals(INTENT_VALUE_ADD)) {
            this.isAdd = true;
        }
        this.activeCourse = (Course) intent.getSerializableExtra(INTENT_TAG_COURSE);
        Log.d(TAG, "resolveType - active course is: " + activeCourse.getTitle());
        if (isAdd) {
            setTitle("Add Assessment");

        } else {
            setTitle("Modify Assessment");
        }
    }

    public void saveAssessment() {
        if (validateInput()) {
            if (isAdd) {
                long assessmentId = this.dbOpenHelper.insertAssessment(newAssessment.getTitle(), newAssessment.getType(), newAssessment.getDueDate(), activeCourse.getCourseId());
                if (assessmentId != -1) {
                    Toast.makeText(getBaseContext(), "Assessment successfully added", Toast.LENGTH_SHORT).show();

                    // If there's a goal value, insert it
                    if (goalDate != null && !(goalDate.getDate().trim().isEmpty())) {
                        this.dbOpenHelper.insertGoal(goalDate.getDate(), assessmentId);
                    }
                    finish();
//                    Intent newIntent = new Intent(this, CourseAddMentorActivity.class);
//                    newIntent.putExtra("course", newCourse);
//                    startActivity(newIntent);
                } else {
                    Toast.makeText(getBaseContext(), "Failed to add assessment", Toast.LENGTH_SHORT).show();
                }
            } else {
                newAssessment.setAssessmentId(activeAssessment.getAssessmentId());
                if (this.dbOpenHelper.updateAssessment(activeAssessment.getAssessmentId(), newAssessment.getTitle(), newAssessment.getType(), newAssessment.getDueDate(), activeAssessment.getCourseId())) {
                    Toast.makeText(getBaseContext(), "Assessment successfully updated", Toast.LENGTH_SHORT).show();
                    // Update the goal value if present
                    if (goalDate != null && !(goalDate.getDate().trim().isEmpty())) {
                        this.dbOpenHelper.updateGoal(goalDate.getDate(), activeAssessment.getAssessmentId());
                    }
                    finish();
//                    Intent newIntent = new Intent(this, CourseAddMentorActivity.class);
//                    newIntent.putExtra("course", newCourse);
//                    startActivity(newIntent);
                } else {
                    Toast.makeText(getBaseContext(), "Failed to update assessment", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean validateInput() {
        this.newAssessment = new Assessment();
        Log.d(TAG, "validateInput: Validating input for " + this.assessmentNameInput.getText().toString());
        if(this.assessmentNameInput.getText().toString().trim().isEmpty()) {
            Toast.makeText(getBaseContext(), "Please enter an assessment name", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            newAssessment.setTitle(this.assessmentNameInput.getText().toString());
        }
        if(!(Util.checkDate(this.dueDateInput.getText().toString()))) {
            Toast.makeText(getBaseContext(), "Please enter a valid due date, format: yyyy-mm-dd", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            newAssessment.setDueDate(this.dueDateInput.getText().toString());
        }
        if (!this.goalDateInput.getText().toString().trim().isEmpty()) {
            if (!(Util.checkDate(this.goalDateInput.getText().toString()))) {
                Toast.makeText(getBaseContext(), "Please enter a valid goal date, format: yyyy-mm-dd", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                this.goalDate = new GoalDate(this.goalDateInput.getText().toString());
            }
        }
        if (this.assessmentType != null && this.assessmentType.getSelectedItem() != null) {
            newAssessment.setType(this.assessmentType.getSelectedItem().toString());
        } else {
            Toast.makeText(getBaseContext(), "Please select an assessment type!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}

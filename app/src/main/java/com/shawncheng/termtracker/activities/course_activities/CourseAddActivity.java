package com.shawncheng.termtracker.activities.course_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.Term;
import com.shawncheng.termtracker.util.Util;
import static com.shawncheng.termtracker.util.TermTrackerConstants.*;

import java.util.Arrays;
import java.util.List;

public class CourseAddActivity extends AppCompatActivity {

    private static final String TAG = "CourseAddActivity";


    private Course activeCourse;
    private Button saveButton;
    private Term activeTerm;
    private Course newCourse;
    private EditText courseNameInput;
    private EditText startDateInput;
    private EditText endDateInput;
    private Spinner status;
    private String courseType;
    private Intent intent;
    private DBOpenHelper dbOpenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate called");

        setContentView(R.layout.activity_course_add);

        dbOpenHelper = new DBOpenHelper(this);

        this.courseNameInput = findViewById(R.id.course_add_title);
        this.startDateInput = findViewById(R.id.course_add_start_value);
        this.endDateInput = findViewById(R.id.course_add_end_date_value);
        this.status = findViewById(R.id.course_add_status_spinner);
        this.saveButton = findViewById(R.id.course_add_next_button);

        // Populate the drop down menu
        populateStatusSpinner();

        this.intent = getIntent();
        resolveType();
    }

    private void resolveType() {
        this.courseType = this.intent.getStringExtra(INTENT_TAG_TYPE);
        if (this.courseType.equals(INTENT_VALUE_ADD)) {
            Log.d(TAG, "resolveType: Intent was for a course ADD");
            setTitle("Add Course");
            this.activeTerm = (Term) this.intent.getSerializableExtra(INTENT_TAG_TERM);
            Log.d(TAG, "Adding course for term: " + activeTerm.getTermName());
        } else {
            Log.d(TAG, "resolveType: Intent was for a course MODIFY");
            setTitle("Modify Course");
            this.activeCourse = (Course) this.intent.getSerializableExtra("course");
            this.saveButton.setText("Save");
            setInputs();
        }
    }

    private void setInputs() {
        this.courseNameInput.setText(activeCourse.getTitle());
        this.startDateInput.setText(activeCourse.getStartDate());
        this.endDateInput.setText(activeCourse.getEndDate());
        String[] statusArray = getResources().getStringArray(R.array.course_status_array);
        List<String> statusArrayList = Arrays.asList(statusArray);
        Log.d(TAG, "[setInputs] Status: " + activeCourse.getStatus() + " has an index of: " + statusArrayList.indexOf(activeCourse.getStatus()) + " in ArrayList: " + statusArrayList);
        this.status.setSelection(statusArrayList.indexOf(activeCourse.getStatus()));
    }

    private void populateStatusSpinner() {
        Spinner statusDropDown = findViewById(R.id.course_add_status_spinner);
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this, R.array.course_status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusDropDown.setAdapter(statusAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_addmodify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_course_addmodify_cancel:
                finish();
                break;
        }
        return true;
    }

    public void saveCourse() {
        if (validateInput()) {
            if (this.courseType.equals("add")) {
                int courseId = (int) this.dbOpenHelper.insertCourse(newCourse.getTitle(), newCourse.getStartDate(), newCourse.getEndDate(), newCourse.getStatus(), activeTerm.getTermId());
                if (courseId != -1) {
                    Toast.makeText(getBaseContext(), "Course successfully added", Toast.LENGTH_SHORT).show();
                    newCourse.setCourseId(courseId);
                    Intent newIntent = new Intent(this, CourseAddMentorActivity.class);
                    newIntent.putExtra(INTENT_TAG_COURSE, newCourse);
                    newIntent.putExtra(INTENT_TAG_TERM, activeTerm);
                    newIntent.putExtra(INTENT_TAG_ORIGIN, INTENT_VALUE_ORIGIN_COURSEADD);
                    Log.d(TAG, "[Intent Tracking] intent name is: " + newCourse.getTitle() +  ", Intent ID is: " + newCourse.getCourseId());
                    startActivity(newIntent);
                } else {
                    Toast.makeText(getBaseContext(), "Failed to add course", Toast.LENGTH_SHORT).show();
                }
            } else {
                newCourse.setCourseId(activeCourse.getCourseId());
                if (this.dbOpenHelper.updateCourse(activeCourse.getCourseId(), newCourse.getTitle(), newCourse.getStartDate(), newCourse.getEndDate(), newCourse.getStatus())) {
                    Toast.makeText(getBaseContext(), "Course successfully updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Failed to update course", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean validateInput() {
        this.newCourse = new Course();
        Log.d(TAG, "validateInput: Validating input for " + this.courseNameInput.getText().toString());
        if(this.courseNameInput.getText().toString().trim().isEmpty()) {
            Toast.makeText(getBaseContext(), "Please enter a course name", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            newCourse.setTitle(this.courseNameInput.getText().toString());
        }
        if(!(Util.checkDate(this.startDateInput.getText().toString()))) {
            Toast.makeText(getBaseContext(), "Please enter a valid start date, format: yyyy-mm-dd", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            newCourse.setStartDate(this.startDateInput.getText().toString());
        }
        if(!(Util.checkDate(this.endDateInput.getText().toString()))) {
            Toast.makeText(getBaseContext(), "Please enter a valid end date", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            newCourse.setEndDate(this.endDateInput.getText().toString());
        }
        if (this.status != null && this.status.getSelectedItem() != null) {
            newCourse.setStatus(this.status.getSelectedItem().toString());
        } else {
            Toast.makeText(getBaseContext(), "Please select a status", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void nextButtonHandler(View view) {
        saveCourse();
    }

}

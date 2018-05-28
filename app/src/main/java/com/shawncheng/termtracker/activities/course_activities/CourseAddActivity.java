package com.shawncheng.termtracker.activities.course_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.activities.assessment_activities.AssessmentAddActivity;
import com.shawncheng.termtracker.activities.assessment_activities.AssessmentDetailActivity;
import com.shawncheng.termtracker.activities.mentor_activities.MentorAddActivity;
import com.shawncheng.termtracker.activities.mentor_activities.MentorDetailActivity;
import com.shawncheng.termtracker.adapters.AssessmentListAdapter;
import com.shawncheng.termtracker.adapters.MentorsListAdapter;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Assessment;
import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.Mentor;
import com.shawncheng.termtracker.model.Term;
import com.shawncheng.termtracker.util.Util;
import static com.shawncheng.termtracker.util.IntentConstants.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourseAddActivity extends AppCompatActivity {

    private static final String TAG = "CourseAddActivity";


    private Course activeCourse;
    private Term activeTerm;
    private Course newCourse;
    private EditText courseNameInput;
    private EditText startDateInput;
    private EditText endDateInput;
    private Spinner status;
    private String courseType;
    private Intent intent;
    private DBOpenHelper dbOpenHelper;
//    private ArrayList<Mentor> mentorArrayList;
//    private ArrayList<Assessment> assessmentArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate called");

        setContentView(R.layout.activity_course_add);

        dbOpenHelper = new DBOpenHelper(this);
//        mentorArrayList = new ArrayList<>();
//        assessmentArrayList = new ArrayList<>();

        this.courseNameInput = findViewById(R.id.course_add_title);
        this.startDateInput = findViewById(R.id.course_add_start_value);
        this.endDateInput = findViewById(R.id.course_add_end_date_value);
        this.status = findViewById(R.id.course_add_status_spinner);

        // Populate the drop down menu
        populateStatusSpinner();

        this.intent = getIntent();
        resolveType();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onPostResume called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart called");
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
//            case R.id.menu_course_addmodify_save:
//                saveCourse();
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
                    Intent newIntent = new Intent(this, CourseAddMentorActivity.class);
                    //TODO Origin here for coming from detail screen?
                    newIntent.putExtra("course", newCourse);
                    startActivity(newIntent);
                    //finish();
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

//    public void addAssessmentHandler(View view) {
//        //TODO add handler for adding assessment. Need to implement the actual add assessment activity first
//        changeActivity(AssessmentAddActivity.class, activeCourse);
//    }
//
//    public void addMentorHandler(View view) {
//        changeActivity(MentorAddActivity.class, activeCourse);
//    }
//
//    private void changeActivity(Class<?> activityClass, Course course) {
//        Intent intent = new Intent(this, activityClass);
//        intent.putExtra("type", "add");
//        intent.putExtra("course", course);
//        startActivity(intent);
//    }

//    private void setMentorListView() {
//        if (this.courseType.equals("edit")) {
//            this.mentorArrayList = dbOpenHelper.getMentors(activeCourse.getCourseId());
//        }
//        ListView listView = findViewById(R.id.list_view_course_add_mentors);
//        ListAdapter listAdapter = new MentorsListAdapter(this, mentorArrayList);
//        listView.setAdapter(listAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Mentor mentor = (Mentor) parent.getAdapter().getItem(position);
//                switchAddActivity(MentorDetailActivity.class, mentor, mentorArrayList);
//            }
//        });
//    }
//
//    private void switchAddActivity(Class<?> detailClass, Object obj, ArrayList<?> arrayList) {
//        Intent intent = new Intent(this, detailClass);
//        if (detailClass.equals(AssessmentDetailActivity.class)) {
//            Assessment assessment = (Assessment) obj;
//            intent.putExtra("assessment", assessment);
//        } else if (detailClass.equals(MentorDetailActivity.class)) {
//            Mentor mentor = (Mentor) obj;
//            intent.putExtra("mentor", mentor);
//            intent.putExtra("type", "addcourse");
//            intent.putExtra("list", arrayList);
//        }
//        startActivity(intent);
//    }
//
//    private void setAssessmentListView() {
//        if (this.courseType.equals("edit")) {
//            assessmentArrayList = dbOpenHelper.getAssessments(activeCourse.getCourseId());
//        }
//        ListView listView = findViewById(R.id.list_view_assessments);
//        ListAdapter listAdapter = new AssessmentListAdapter(this, assessmentArrayList);
//        listView.setAdapter(listAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Assessment assessment = (Assessment) parent.getAdapter().getItem(position);
//                switchAddActivity(AssessmentDetailActivity.class, assessment, assessmentArrayList);
//            }
//        });
//    }
}

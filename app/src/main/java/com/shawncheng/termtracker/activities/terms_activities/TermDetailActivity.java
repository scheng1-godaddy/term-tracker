package com.shawncheng.termtracker.activities.terms_activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.shawncheng.termtracker.activities.course_activities.CourseAddActivity;
import com.shawncheng.termtracker.activities.course_activities.CourseDetailActivity;
import com.shawncheng.termtracker.adapters.CourseListAdapter;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.Term;
import com.shawncheng.termtracker.util.Util;

import static com.shawncheng.termtracker.util.IntentConstants.*;

import java.util.ArrayList;

public class TermDetailActivity extends AppCompatActivity {

    private static final String TAG = "TermDetailActivity";
    private Term activeTerm;
    private DBOpenHelper dbOpenHelper;
    private ArrayList<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        setTitle("Term Details");

        // Get info on the term
        Intent intent = getIntent();
        this.activeTerm = (Term) intent.getSerializableExtra(INTENT_TAG_TERM);
        Log.d(TAG, "onCreate: Active term is: " + activeTerm.getTermName());



        // Display courses for the term
        dbOpenHelper = new DBOpenHelper(this);

        // Display term info
        setInput();

        setCourseListView();
        addDeleteButton();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        activeTerm = dbOpenHelper.getTerm(activeTerm.getTermId());
        setInput();
        setCourseListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_term_detail_edit:
                Intent intent = new Intent(this, TermAddActivity.class);
                intent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_MODIFY);
                intent.putExtra(INTENT_TAG_TERM, activeTerm);
                startActivity(intent);
                break;
            case R.id.menu_term_detail_close:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteTerm() {
        if (isCourseListEmpty()) {
            if (dbOpenHelper.deleteTerm(activeTerm.getTermId())) {
                Toast.makeText(getBaseContext(), "Term successfully removed", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getBaseContext(), "Term could not be removed", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getBaseContext(), "Removal failed: must remove courses first", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isCourseListEmpty() {
        ArrayList<Course> courseList = dbOpenHelper.getCourses(activeTerm.getTermId());
        return courseList.isEmpty();
    }

    private void setInput() {
        TextView termName = findViewById(R.id.term_name);
        TextView startDate = findViewById(R.id.term_start_date);
        TextView endDate = findViewById(R.id.term_end_date);
        termName.setText(activeTerm.getTermName());
        startDate.setText(activeTerm.getStartDate());
        endDate.setText(activeTerm.getEndDate());
    }

    private void setCourseListView() {
        this.courseList = dbOpenHelper.getCourses(this.activeTerm.getTermId());
        Log.d(TAG, "course list retrieved, the length is: " + courseList.size());
        ListAdapter listAdapter = new CourseListAdapter(this, courseList);
        ListView listView = findViewById(R.id.courses_list_view);
        listView.setAdapter(listAdapter);
        Util.setListViewHeightBasedOnChildren(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course = (Course) parent.getAdapter().getItem(position);
                changeActivity(CourseDetailActivity.class, course);
            }
        });
    }

    private void changeActivity(Class<?> detailClass, Object obj) {
        Intent intent = new Intent(this, detailClass);
        if (detailClass.equals(CourseDetailActivity.class)) {
            Course course = (Course) obj;
            intent.putExtra(INTENT_TAG_COURSE, course);
        }
        if (detailClass.equals(CourseAddActivity.class)) {
            intent.putExtra(INTENT_TAG_TERM, activeTerm);
            intent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_ADD);
        }
        startActivity(intent);
    }

    public void addCourseButtonHandler(android.view.View myview) {
        changeActivity(CourseAddActivity.class, null);
    }

    private void addDeleteButton() {
        RelativeLayout rlayout = findViewById(R.id.term_detail_relativelayout);
        Button dbutton = new Button(this);
        dbutton.setText("Delete Term");
        dbutton.setTextColor(Color.WHITE);
        dbutton.setBackgroundColor(Color.RED);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.courses_list_view);
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
                .setMessage("Do you wish to delete Term?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTerm();
                    }
                }).setNegativeButton(android.R.string.no, null).show();
    }

}

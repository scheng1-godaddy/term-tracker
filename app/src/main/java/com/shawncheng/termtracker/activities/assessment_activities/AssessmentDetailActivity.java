package com.shawncheng.termtracker.activities.assessment_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.adapters.GoalListAdapter;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Assessment;
import com.shawncheng.termtracker.model.GoalDate;

import java.util.ArrayList;

public class AssessmentDetailActivity extends AppCompatActivity {

    private Assessment activeAssessment;
    private DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        setTitle("Assessment Details");

        Intent intent = getIntent();
        this.activeAssessment = (Assessment) intent.getSerializableExtra("assessment");
        dbOpenHelper = new DBOpenHelper(this);

        setInputs();

        setGoalListView();
    }

    private void setGoalListView() {
        ArrayList<GoalDate> goalDateArrayList = dbOpenHelper.getGoals(activeAssessment.getAssessmentId());
        ListView listView = findViewById(R.id.assessment_goal_date_list_view);
        ListAdapter listAdapter = new GoalListAdapter(this, goalDateArrayList);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoalDate goalDate = (GoalDate) parent.getAdapter().getItem(position);
                switchDetailActivity(goalDate);
            }
        });
    }

    private void switchDetailActivity(GoalDate goalDate) {
        //TODO switch activity for goal details
    }

    private void setInputs() {
        TextView title = findViewById(R.id.assessment_detail_title);
        TextView type = findViewById(R.id.assessment_type_value);
        TextView dueDate = findViewById(R.id.assessment_due_date_value);
        title.setText(activeAssessment.getTitle());
        type.setText(activeAssessment.getType());
        dueDate.setText(activeAssessment.getDueDate());
    }
}

package com.shawncheng.termtracker.activities.mentor_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.Mentor;
import static com.shawncheng.termtracker.util.IntentConstants.*;

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
            case R.id.menu_mentor_detail_close:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setInputs() {
        nameInput = findViewById(R.id.mentor_detail_name);
        phoneInput = findViewById(R.id.mentor_detail_phone_value);
        emailInput = findViewById(R.id.mentor_detail_email_value);
        nameInput.setText(activeMentor.getName());
        phoneInput.setText(activeMentor.getPhone());
        emailInput.setText(activeMentor.getEmail());
    }

    public void editButtonHandler(View view) {
        Intent intent = new Intent(this, MentorAddActivity.class);
        intent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_MODIFY);
        intent.putExtra(INTENT_TAG_MENTOR, activeMentor);
        intent.putExtra(INTENT_TAG_COURSE, activeCourse);
        startActivity(intent);
    }

    public void deleteButtonHandler(View view) {
        //TODO We should check for confirmation before deletes
        if (dbOpenHelper.deleteMentor(activeMentor.getId())) {
            Toast.makeText(getBaseContext(), "Mentor successfully deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "Mentor could not be deleted", Toast.LENGTH_SHORT).show();
        }
    }
}

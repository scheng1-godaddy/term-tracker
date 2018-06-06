package com.shawncheng.termtracker.activities.assessment_activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Assessment;
import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.GoalDate;
import com.shawncheng.termtracker.util.NotificationPublisher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.shawncheng.termtracker.util.TermTrackerConstants.*;

public class AssessmentDetailActivity extends AppCompatActivity {

    private static final String TAG = "AssessmentDetail";
    private Assessment activeAssessment;
    private Course activeCourse;
    private DBOpenHelper dbOpenHelper;
    private GoalDate goalDateValue;

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

        setAlarmSwitch();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        activeAssessment = dbOpenHelper.getAssessment(activeAssessment.getAssessmentId());
        setAlarmSwitch();
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
        try {
            goalDateValue = retrieveGoal();
            if (goalDateValue != null && !(goalDateValue.getDate().trim().isEmpty())) {
                goalDate.setText(goalDateValue.getDate());
            }
        } catch (CursorIndexOutOfBoundsException e) {
            goalDateValue = null;
            Log.d(TAG, "Could not find a goal date for assessment: " + activeAssessment.getTitle());
        }
    }

    private void setAlarmSwitch() {
        String notificationMsg = "Assessment " + activeAssessment.getTitle() + " goal date reminder";
        final Switch goalDateSwitch = findViewById(R.id.assessment_goal_date_switch);
        if (checkAlarm(getNotification(notificationMsg, true), true)) {
            goalDateSwitch.setChecked(true);
        }
        goalDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if (goalDateValue != null) {
                        String notificationMsg = "Assessment " + activeAssessment.getTitle() + " goal date reminder";
                        scheduleNotification(getNotification(notificationMsg, true), true);
                        Toast.makeText(getBaseContext(), "Goal Date Reminder On", Toast.LENGTH_SHORT).show();
                        checkAlarm(getNotification(notificationMsg, true), true);
                    } else {
                        Toast.makeText(getBaseContext(), "No goal date set!", Toast.LENGTH_SHORT).show();
                        goalDateSwitch.setChecked(false);
                    }

                } else {
                    String notificationMsg = "Assessment " + activeAssessment.getTitle() + " goal date reminder";
                    cancelNotification(getNotification(notificationMsg, true), true);
                    Toast.makeText(getBaseContext(), "Goal Date Reminder Off", Toast.LENGTH_SHORT).show();
                    checkAlarm(getNotification(notificationMsg, true), true);
                }
            }
        });
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

    private void scheduleNotification(Notification notificationMsg, boolean start) {
        PendingIntent pendingIntent = getPendingIntent(notificationMsg, start);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        try {

            Date startDate = getDateFormat(start);
            alarmManager.set(AlarmManager.RTC_WAKEUP, startDate.getTime(), pendingIntent);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void cancelNotification(Notification notificationMsg, boolean start) {
        PendingIntent pendingIntent = getPendingIntent(notificationMsg, start);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
    }

    private boolean checkAlarm(Notification notificationMsg, boolean start) {
        Intent notificationIntent = new Intent(this.getApplicationContext(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, getNotificationId(start));
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notificationMsg);

        boolean alarmUp = (PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmUp) {
            Log.d(TAG, "[checkAlarm] The alarm is already active");
            return true;
        } else {
            Log.d(TAG, "[checkAlarm] The alarm is not active");
            return false;
        }
    }

    private Date getDateFormat(boolean start) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Log.d(TAG, "Simple date format is: " + dateFormat.toString());
        if (start) {
            return dateFormat.parse(goalDateValue.getDate());
        } else {
            return dateFormat.parse(goalDateValue.getDate());
        }

    }

    private int getNotificationId(boolean isStart) {
        if (isStart) {
            return Integer.parseInt(activeCourse.getCourseId() + "1");
        } else {
            return Integer.parseInt(activeCourse.getCourseId() + "2");
        }
    }

    private PendingIntent getPendingIntent(Notification notificationMsg, boolean start) {
        Intent notificationIntent = new Intent(this.getApplicationContext(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, getNotificationId(start));
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notificationMsg);
        return PendingIntent.getBroadcast(this, 3, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Notification getNotification(String content, boolean start) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Term Tracker Alert");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        return builder.build();
    }

}

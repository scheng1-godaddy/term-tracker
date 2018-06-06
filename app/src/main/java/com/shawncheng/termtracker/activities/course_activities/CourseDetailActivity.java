package com.shawncheng.termtracker.activities.course_activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.activities.assessment_activities.AssessmentAddActivity;
import com.shawncheng.termtracker.activities.assessment_activities.AssessmentDetailActivity;
import com.shawncheng.termtracker.activities.mentor_activities.MentorAddActivity;
import com.shawncheng.termtracker.activities.mentor_activities.MentorDetailActivity;
import com.shawncheng.termtracker.activities.notes_activities.NoteAddActivity;
import com.shawncheng.termtracker.activities.notes_activities.NoteDetailActivity;
import com.shawncheng.termtracker.adapters.AssessmentListAdapter;
import com.shawncheng.termtracker.adapters.MentorsListAdapter;
import com.shawncheng.termtracker.adapters.NotesListAdapter;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Assessment;
import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.Mentor;
import com.shawncheng.termtracker.model.Note;
import com.shawncheng.termtracker.util.NotificationPublisher;
import com.shawncheng.termtracker.util.Util;

import static com.shawncheng.termtracker.util.TermTrackerConstants.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CourseDetailActivity extends AppCompatActivity {

    private static final String TAG = "CourseDetailActivity";
    private Course activeCourse;
    private DBOpenHelper dbOpenHelper;
    private Switch startDateSwitch;
    private Switch endDateSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        setTitle("Course Details");

        Intent intent = getIntent();
        activeCourse = (Course) intent.getSerializableExtra("course");
        dbOpenHelper = new DBOpenHelper(this);

        setAlarmSwitches();


        setInputs();
        setAssessmentListView();
        setMentorListView();
        setNotesListView();
        addDeleteButton();
    }

    private void setAlarmSwitches() {
        startDateSwitch = findViewById(R.id.course_detail_start_alarm_switch);
        endDateSwitch = findViewById(R.id.course_detail_end_alarm_switch);
        String notificationStartMsg = "Course " + activeCourse.getTitle() + " is starting today";
        String notificationEndMsg = "Course " + activeCourse.getTitle() + " is ending today";

        if (checkAlarm(getNotification(notificationStartMsg, true), true)) {
            startDateSwitch.setChecked(true);
        }

        if (checkAlarm(getNotification(notificationEndMsg, false), false)) {
            endDateSwitch.setChecked(true);
        }

        startDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String notificationMsg = "Course " + activeCourse.getTitle() + " is starting today";
                    scheduleNotification(getNotification(notificationMsg, true), true);
                    Toast.makeText(getBaseContext(), "Start Date Alarm On", Toast.LENGTH_SHORT).show();
                    checkAlarm(getNotification(notificationMsg, true), true);
                } else {
                    String notificationMsg = "Course " + activeCourse.getTitle() + " is starting today";
                    cancelNotification(getNotification(notificationMsg, true), true);
                    Toast.makeText(getBaseContext(), "Start Date Alarm Off", Toast.LENGTH_SHORT).show();
                    checkAlarm(getNotification(notificationMsg, true), true);
                }
            }
        });

        endDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String notificationMsg = "Course " + activeCourse.getTitle() + " is ending today";
                    scheduleNotification(getNotification(notificationMsg, false), false);
                    Toast.makeText(getBaseContext(), "End Date Alarm On", Toast.LENGTH_SHORT).show();
                    checkAlarm(getNotification(notificationMsg, false), false);
                } else {
                    String notificationMsg = "Course " + activeCourse.getTitle() + " is ending today";
                    cancelNotification(getNotification(notificationMsg, false), false);
                    Toast.makeText(getBaseContext(), "End Date Alarm Off", Toast.LENGTH_SHORT).show();
                    checkAlarm(getNotification(notificationMsg, false), false);
                }
            }
        });
    }

    private void scheduleNotification(Notification notificationMsg, boolean start) {
        PendingIntent pendingIntent = getPendingIntent(notificationMsg, start);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        try {

            Date startDate = getDateFormat(start);
            alarmManager.set(AlarmManager.RTC_WAKEUP, startDate.getTime(), pendingIntent);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void cancelNotification(Notification notificationMsg, boolean start) {
        PendingIntent pendingIntent = getPendingIntent(notificationMsg, start);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
    }

    private boolean checkAlarm(Notification notificationMsg, boolean start) {
        Intent notificationIntent = new Intent(this.getApplicationContext(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, getNotificationId(start));
        Log.d(TAG, "[checkAlarm] Notification ID is: " + getNotificationId(start) + " Course ID is: " + activeCourse.getCourseId());
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notificationMsg);
        int requestCode = 0;
        if (start) {
            requestCode = 1;
        }

        boolean alarmUp = (PendingIntent.getBroadcast(this, requestCode, notificationIntent, PendingIntent.FLAG_NO_CREATE) != null);

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
            return dateFormat.parse(activeCourse.getStartDate());
        } else {
            return dateFormat.parse(activeCourse.getEndDate());
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
        int requestCode = 0;
        if (start) {
            requestCode = 1;
        }
        return PendingIntent.getBroadcast(this, requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Notification getNotification(String content, boolean start) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Term Tracker Alert");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        return builder.build();
    }


    private void setInputs() {
        TextView title = findViewById(R.id.course_detail_title);
        TextView startDate = findViewById(R.id.course_detail_start_value);
        TextView endDate = findViewById(R.id.course_detail_end_date_value);
        TextView status = findViewById(R.id.course_detail_status_value);
        title.setText(activeCourse.getTitle());
        startDate.setText(activeCourse.getStartDate());
        endDate.setText(activeCourse.getEndDate());
        status.setText(activeCourse.getStatus());
    }

    private void setAssessmentListView() {
        ArrayList<Assessment> assessmentArrayList = dbOpenHelper.getAssessments(activeCourse.getCourseId());
        ListView listView = findViewById(R.id.list_view_assessments);
        ListAdapter listAdapter = new AssessmentListAdapter(this, assessmentArrayList);
        listView.setAdapter(listAdapter);
        Util.setListViewHeightBasedOnChildren(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Assessment assessment = (Assessment) parent.getAdapter().getItem(position);
                changeActivity(AssessmentDetailActivity.class, assessment);
            }
        });
    }

    private void setMentorListView() {
        ArrayList<Mentor> mentorArrayList = dbOpenHelper.getMentors(activeCourse.getCourseId());
        ListView listView = findViewById(R.id.list_view_mentors);
        ListAdapter listAdapter = new MentorsListAdapter(this, mentorArrayList);
        listView.setAdapter(listAdapter);
        Util.setListViewHeightBasedOnChildren(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mentor mentor = (Mentor) parent.getAdapter().getItem(position);
                changeActivity(MentorDetailActivity.class, mentor);
            }
        });
    }

    private void addDeleteButton() {
        RelativeLayout rlayout = findViewById(R.id.course_detail_relativelayout);
        Button dbutton = new Button(this);
        dbutton.setText("Delete Course");
        dbutton.setTextColor(Color.WHITE);
        dbutton.setBackgroundColor(Color.RED);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.list_view_notes);
        params.setMargins(0, 20, 0, 0);
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

    private void changeActivity(Class<?> detailClass, Object obj) {
        Intent intent = new Intent(this, detailClass);
        if (detailClass.equals(AssessmentDetailActivity.class)) {
            Assessment assessment = (Assessment) obj;
            intent.putExtra(INTENT_TAG_ASSESSMENT, assessment);
            intent.putExtra(INTENT_TAG_COURSE, activeCourse);
            Log.d(TAG, "[INTENT TRACKING] activeCourse id and name: " + activeCourse.getCourseId() + " " + activeCourse.getTitle() +
                    " Assessment id and name: " + assessment.getAssessmentId() + " " + assessment.getTitle());
        } else if (detailClass.equals(MentorDetailActivity.class)) {
            Mentor mentor = (Mentor) obj;
            intent.putExtra(INTENT_TAG_MENTOR, mentor);
            intent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_VIEW);
            intent.putExtra(INTENT_TAG_COURSE, activeCourse);
        } else if (detailClass.equals(MentorAddActivity.class)) {
            intent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_ADD);
            intent.putExtra(INTENT_TAG_COURSE, activeCourse);
        } else if (detailClass.equals(AssessmentAddActivity.class)) {
            intent.putExtra(INTENT_TAG_COURSE, activeCourse);
            intent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_ADD);
        } else if (detailClass.equals(NoteAddActivity.class)) {
            intent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_ADD);
            intent.putExtra(INTENT_TAG_COURSE, activeCourse);
        } else if (detailClass.equals(NoteDetailActivity.class)) {
            Note note = (Note) obj;
            intent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_MODIFY);
            intent.putExtra(INTENT_TAG_NOTE, note);
            intent.putExtra(INTENT_TAG_COURSE, activeCourse);
        }
        startActivity(intent);
    }

    private void displayAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Do you wish to delete Course?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCourse();
                    }
                }).setNegativeButton(android.R.string.no, null).show();
    }

    private void deleteCourse() {
        if (dbOpenHelper.deleteCourse(activeCourse.getCourseId())) {
            Toast.makeText(getBaseContext(), "Course successfully removed", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Course could not be removed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        activeCourse = dbOpenHelper.getCourse(activeCourse.getCourseId());
        setInputs();
        setAssessmentListView();
        setMentorListView();
        setNotesListView();
        addDeleteButton();
        setAlarmSwitches();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit_course:
                Intent intent = new Intent(this, CourseAddActivity.class);
                intent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_MODIFY);
                intent.putExtra(INTENT_TAG_COURSE, activeCourse);
                startActivity(intent);
                break;
            case R.id.menu_close_course:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addMentorHandler(View view) {
        changeActivity(MentorAddActivity.class, null);
    }

    public void addAssessmentHandler(View view) {
        changeActivity(AssessmentAddActivity.class, null);
    }

    private void setNotesListView() {
        this.dbOpenHelper = new DBOpenHelper(this);
        final ArrayList<Note> notesList = dbOpenHelper.getNotes(activeCourse.getCourseId());
        ListAdapter notesListAdapter = new NotesListAdapter(this, notesList);
        ListView listView = findViewById(R.id.list_view_notes);
        listView.setAdapter(notesListAdapter);
        Util.setListViewHeightBasedOnChildren(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = (Note) parent.getAdapter().getItem(position);
                changeActivity(NoteDetailActivity.class, note);
            }
        });
    }

    public void addNoteHandler(View view) {
        changeActivity(NoteAddActivity.class, null);
    }
}

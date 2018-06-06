package com.shawncheng.termtracker.activities.notes_activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Course;
import com.shawncheng.termtracker.model.Note;

import static com.shawncheng.termtracker.util.TermTrackerConstants.*;

public class NoteDetailActivity extends AppCompatActivity {

    private Course activeCourse;
    private TextView noteField;
    private Note activeNote;
    private Intent intent;
    private DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        dbOpenHelper = new DBOpenHelper(this);

        intent = getIntent();
        activeCourse = (Course) intent.getSerializableExtra(INTENT_TAG_COURSE);
        activeNote = (Note) intent.getSerializableExtra(INTENT_TAG_NOTE);
        noteField = findViewById(R.id.note_text_display);
        setInputs();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        activeNote = dbOpenHelper.getNote(activeNote.getNoteId());
        setInputs();
    }

    private void setInputs() {
        setTitle("Note for " + activeCourse.getTitle());
        noteField.setText(activeNote.getNote());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_note_detail_edit:
                Intent newIntent = new Intent(this, NoteAddActivity.class);
                newIntent.putExtra(INTENT_TAG_COURSE, activeCourse);
                newIntent.putExtra(INTENT_TAG_NOTE, activeNote);
                newIntent.putExtra(INTENT_TAG_TYPE, INTENT_VALUE_MODIFY);
                startActivity(newIntent);
                break;
            case R.id.menu_note_detail_close:
                finish();
                break;
            case R.id.menu_note_detail_share:
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String sharePreference = prefs.getString(SHARE_SETTING_KEY, null);

                if (sharePreference.equals(SHARE_VALUE_EMAIL)) {
                    sendEmail();
                } else {
                    sendSms();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Note for course " + activeCourse.getTitle());
        i.putExtra(Intent.EXTRA_TEXT   , activeNote.getNote());
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getBaseContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSms() {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("sms:"));
        smsIntent.putExtra("sms_body", activeNote.getNote());
        startActivity(smsIntent);
    }
}

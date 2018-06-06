package com.shawncheng.termtracker.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import static com.shawncheng.termtracker.util.TermTrackerConstants.*;
import com.shawncheng.termtracker.R;

public class SettingsActivity extends AppCompatActivity {



    private RadioGroup radioGroup;
    private RadioButton rbSms;
    private RadioButton rbEmail;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setRadioButtons();
        
        setPreferences();
        
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setPreferences();
    }

    private void setPreferences() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String sharePreference = prefs.getString(SHARE_SETTING_KEY, "EMAIL");

        if (sharePreference.equals(SHARE_VALUE_EMAIL)) {
            rbEmail.setChecked(true);
        } else {
            rbSms.setChecked(true);
        }
    }

    private void setRadioButtons() {
        radioGroup = findViewById(R.id.share_settings_radio_group);
        rbSms = findViewById(R.id.share_settings_radio_sms);
        rbEmail = findViewById(R.id.share_settings_radio_email);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.share_settings_radio_sms) {
                    setPreferences(SHARE_VALUE_SMS);
                    Toast.makeText(getBaseContext(), "Share settings set to SMS", Toast.LENGTH_SHORT).show();
                }
                if (checkedId == R.id.share_settings_radio_email) {
                    setPreferences(SHARE_VALUE_EMAIL);
                    Toast.makeText(getBaseContext(), "Share settings set to Email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setPreferences(String prefSetting) {
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(SHARE_SETTING_KEY, prefSetting);
        editor.apply();
    }
}

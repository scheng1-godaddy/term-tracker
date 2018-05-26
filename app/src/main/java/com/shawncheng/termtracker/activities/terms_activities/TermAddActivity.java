package com.shawncheng.termtracker.activities.terms_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.shawncheng.termtracker.R;
import com.shawncheng.termtracker.database.DBOpenHelper;
import com.shawncheng.termtracker.model.Term;
import com.shawncheng.termtracker.util.Util;

public class TermAddActivity extends AppCompatActivity {

    private static final String TAG = "TermAddActivity";
    private String termType;
    private EditText termNameInput;
    private EditText startDateInput;
    private EditText endDateInput;
    private Intent intent;
    private Term existingTerm;
    private Term newTerm;
    private DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_add);
        this.dbOpenHelper = new DBOpenHelper(this);
        this.termNameInput = findViewById(R.id.termTitleInput);
        this.startDateInput = findViewById(R.id.termStartInput);
        this.endDateInput = findViewById(R.id.termEndInput);
        this.intent = getIntent();
        resolveType();
    }

    private void resolveType() {
        this.termType = this.intent.getStringExtra("type");
        Log.d("TermAddActivity", "onCreate: Type is: " + termType);
        if (termType.equals("add")) {
            setTitle("Add Term");
        } else {
            this.existingTerm = (Term) intent.getSerializableExtra("term");
            setTitle("Modify Term");
            setInputs(this.intent);
        }
    }

    private void setInputs(Intent intent) {
        termNameInput.setText(existingTerm.getTermName());
        startDateInput.setText(existingTerm.getStartDate());
        endDateInput.setText(existingTerm.getEndDate());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term_addmodify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_term_addmodify_cancel:
                finish();
                break;
            case R.id.menu_term_addmodify_save:
                saveButtonHandler();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveButtonHandler() {
        if (validateInput()) {
            if (this.termType.equals("add")) {
                if (this.dbOpenHelper.insertTerm(newTerm.getTermName(), newTerm.getStartDate(), newTerm.getEndDate())) {
                    Toast.makeText(getBaseContext(), "Term successfully added", Toast.LENGTH_SHORT).show();
                    switchActivity();
                } else {
                    Toast.makeText(getBaseContext(), "Failed to add term", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (this.dbOpenHelper.updateTerm(existingTerm.getTermId(), newTerm.getTermName(), newTerm.getStartDate(), newTerm.getEndDate())) {
                    Toast.makeText(getBaseContext(), "Term successfully updated", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "Failed to update term", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void switchActivity() {
        Intent intent = new Intent(this, TermsListActivity.class);
        startActivity(intent);
    }

    private boolean validateInput() {
        this.newTerm = new Term();
        Log.d(TAG, "validateInput: Validating input for " + termNameInput.getText().toString());
        if(this.termNameInput.getText().toString().trim().isEmpty()) {
            Toast.makeText(getBaseContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            newTerm.setTermName(this.termNameInput.getText().toString());
        }
        if(!(Util.checkDate(this.startDateInput.getText().toString()))) {
            Toast.makeText(getBaseContext(), "Please enter a valid start date", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            newTerm.setStartDate(this.startDateInput.getText().toString());
        }
        if(!(Util.checkDate(this.endDateInput.getText().toString()))) {
            Toast.makeText(getBaseContext(), "Please enter a valid end date", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            newTerm.setEndDate(this.endDateInput.getText().toString());
        }
        return true;
    }



}

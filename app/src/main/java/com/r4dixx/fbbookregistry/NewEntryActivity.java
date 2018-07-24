package com.r4dixx.fbbookregistry;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.r4dixx.fbbookregistry.database.Contract.BookEntry;
import com.r4dixx.fbbookregistry.database.DbHelper;

public class NewEntryActivity extends AppCompatActivity {

    private EditText mTitleET;
    private EditText mAuthorET;
    private EditText mPublisherET;
    private EditText mYearET;
    private Spinner mSubjectSpin;


    private int mSubject = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        Toolbar toolbar = findViewById(R.id.toolbar_new_entry);
        setSupportActionBar(toolbar);

        mTitleET = findViewById(R.id.edit_book_title);
        mAuthorET = findViewById(R.id.edit_book_author);
        mPublisherET = findViewById(R.id.edit_book_publisher);
        mYearET = findViewById(R.id.edit_book_year);
        mSubjectSpin = findViewById(R.id.spinner_subject);

        setSpinner();
    }

    private void setSpinner() {

        ArrayAdapter subjectSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_subjects, android.R.layout.simple_spinner_item);
        subjectSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mSubjectSpin.setAdapter(subjectSpinnerAdapter);

        mSubjectSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);

                // TODO this could be a looot cleaner. With a switch maybe or even better a loop
                if (!TextUtils.isEmpty(selected)) {
                    if (selected.equals(getString(R.string.subject_alchemy))) {
                        mSubject = BookEntry.SUBJECT_ALCHEMY;
                    } else if (selected.equals(getString(R.string.subject_ancient_runes))) {
                        mSubject = BookEntry.SUBJECT_ANCIENT_RUNES;
                    } else if (selected.equals(getString(R.string.subject_ancient_studies))) {
                        mSubject = BookEntry.SUBJECT_ANCIENT_STUDIES;
                    } else if (selected.equals(getString(R.string.subject_apparition))) {
                        mSubject = BookEntry.SUBJECT_APPARITION;
                    } else if (selected.equals(getString(R.string.subject_arithmancy))) {
                        mSubject = BookEntry.SUBJECT_ARITHMANCY;
                    } else if (selected.equals(getString(R.string.subject_art))) {
                        mSubject = BookEntry.SUBJECT_ART;
                    } else if (selected.equals(getString(R.string.subject_astronomy))) {
                        mSubject = BookEntry.SUBJECT_ASTRONOMY;
                    } else if (selected.equals(getString(R.string.subject_charms))) {
                        mSubject = BookEntry.SUBJECT_CHARMS;
                    } else if (selected.equals(getString(R.string.subject_creatures))) {
                        mSubject = BookEntry.SUBJECT_CREATURES;
                    } else if (selected.equals(getString(R.string.subject_dark_arts))) {
                        mSubject = BookEntry.SUBJECT_DARK_ARTS;
                    } else if (selected.equals(getString(R.string.subject_divination))) {
                        mSubject = BookEntry.SUBJECT_DIVINATION;
                    } else if (selected.equals(getString(R.string.subject_flying))) {
                        mSubject = BookEntry.SUBJECT_FLYING;
                    } else if (selected.equals(getString(R.string.subject_herbology))) {
                        mSubject = BookEntry.SUBJECT_HERBOLOGY;
                    } else if (selected.equals(getString(R.string.subject_magic_history))) {
                        mSubject = BookEntry.SUBJECT_MAGIC_HISTORY;
                    } else if (selected.equals(getString(R.string.subject_magical_theory))) {
                        mSubject = BookEntry.SUBJECT_MAGICAL_THEORY;
                    } else if (selected.equals(getString(R.string.subject_muggles_studies))) {
                        mSubject = BookEntry.SUBJECT_MUGGLES_STUDY;
                    } else if (selected.equals(getString(R.string.subject_muggle_art))) {
                        mSubject = BookEntry.SUBJECT_MUGGLE_ART;
                    } else if (selected.equals(getString(R.string.subject_music))) {
                        mSubject = BookEntry.SUBJECT_MUSIC;
                    } else if (selected.equals(getString(R.string.subject_potions))) {
                        mSubject = BookEntry.SUBJECT_POTIONS;
                    } else if (selected.equals(getString(R.string.subject_transfiguration))) {
                        mSubject = BookEntry.SUBJECT_TRANFIGURATION;
                    } else if (selected.equals(getString(R.string.subject_xylomancy))) {
                        mSubject = BookEntry.SUBJECT_XYLOMANCY;
                    } else {
                        mSubject = BookEntry.SUBJECT_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSubject = 0;
            }
        });
    }

    private void newEntry() {
        DbHelper mDbHelper = new DbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String titleString = mTitleET.getText().toString().trim();
        String authorString = mAuthorET.getText().toString().trim();
        String publisherString = mPublisherET.getText().toString().trim();
        String yearString = mYearET.getText().toString().trim();
        int year = Integer.parseInt(yearString);

        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_TITLE, titleString);
        values.put(BookEntry.COLUMN_AUTHOR, authorString);
        values.put(BookEntry.COLUMN_PUBLISHER, publisherString);
        values.put(BookEntry.COLUMN_YEAR, year);
        values.put(BookEntry.COLUMN_YEAR, mSubject);

        long newRowId = db.insert(BookEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Cannot save book. Something wrong happened", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                newEntry();
                finish();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

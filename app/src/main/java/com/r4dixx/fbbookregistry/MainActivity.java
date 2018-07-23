package com.r4dixx.fbbookregistry;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.r4dixx.fbbookregistry.database.Contract.BookEntry;
import com.r4dixx.fbbookregistry.database.DbHelper;

public class MainActivity extends AppCompatActivity {

    private DbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.r4dixx.fbbookregistry.R.layout.activity_main);
        Toolbar toolbar = findViewById(com.r4dixx.fbbookregistry.R.id.toolbar_main);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(com.r4dixx.fbbookregistry.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewEntryActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new DbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDbInfo();
    }

    private void displayDbInfo() {
        // Accesses the database
        DbHelper mDbHelper = new DbHelper(this);
        // Opens the database (or create it if non existent)
        // and reads from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // "SELECT * FROM books"
        String[] proj = {
                BookEntry._ID,
                BookEntry.COLUMN_TITLE,
                BookEntry.COLUMN_AUTHOR,
                BookEntry.COLUMN_EDITION,
                BookEntry.COLUMN_YEAR,
                BookEntry.COLUMN_SUBJECT
        };
        // Cursor contains all rows and order them by COLUMN_TITLE
        Cursor cursor = db.query(
                BookEntry.TABLE_NAME,
                proj,
                null,
                null,
                null,
                null,
                BookEntry.COLUMN_TITLE);

        TextView tv = findViewById(R.id.text_view_books);

        // TODO: Modify heavily after being sure the database queries behave correctly in MainActivity. This is just for test and debugging purposes
        // Displays the header created in Contract.BookEntry
        try {
            tv.setText("The current db table contains: "
                    + cursor.getCount()
                    + " entries."
                    + "\n\n");

            tv.append("The header is currently the following:\n"
                    + BookEntry._ID + " | "
                    + BookEntry.COLUMN_TITLE + " | "
                    + BookEntry.COLUMN_AUTHOR + " | "
                    + BookEntry.COLUMN_EDITION + " | "
                    + BookEntry.COLUMN_YEAR + " | "
                    + BookEntry.COLUMN_SUBJECT
                    + "\n\n");

            // Executing this outside the while loop as it more efficient this way performance-wise. Otherwise getColumnIndex() would be called for each row)
            int idIndex = cursor.getColumnIndex((BookEntry._ID));
            int titleIndex = cursor.getColumnIndex((BookEntry.COLUMN_TITLE));
            int authorIndex = cursor.getColumnIndex((BookEntry.COLUMN_AUTHOR));
            int editionIndex = cursor.getColumnIndex((BookEntry.COLUMN_EDITION));
            int yearIndex = cursor.getColumnIndex((BookEntry.COLUMN_YEAR));
            int subjectIndex = cursor.getColumnIndex((BookEntry.COLUMN_SUBJECT));

            while (cursor.moveToNext()) {

                int currentId = cursor.getInt((idIndex));
                String currentTitle = cursor.getString(titleIndex);
                String currentAuthor = cursor.getString(authorIndex);
                String currentEdition = cursor.getString(editionIndex);
                int currentYear = cursor.getInt(yearIndex);
                int currentSubject = cursor.getInt(subjectIndex);

                tv.append((currentId + "|"
                        + currentTitle + "|"
                        + currentAuthor + "|"
                        + currentEdition + "|"
                        + currentYear + "|"
                        + currentSubject));
            }
        } finally {
            cursor.close();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.r4dixx.fbbookregistry.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case com.r4dixx.fbbookregistry.R.id.action_insert_dummy_data:
                // TODO insert and display data
                return true;
            case com.r4dixx.fbbookregistry.R.id.action_delete_data:
                // TODO reset/delete database and display it
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

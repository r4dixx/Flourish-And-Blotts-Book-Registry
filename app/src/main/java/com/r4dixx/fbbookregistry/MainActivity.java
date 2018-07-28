package com.r4dixx.fbbookregistry;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.r4dixx.fbbookregistry.database.BookContract.BookEntry;
import com.r4dixx.fbbookregistry.database.BookDbHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.r4dixx.fbbookregistry.R.layout.activity_main);
        Toolbar toolbar = findViewById(com.r4dixx.fbbookregistry.R.id.toolbar_main);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_bug_report_white_24dp));
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(com.r4dixx.fbbookregistry.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewEntryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDbInfo();
    }

    private void displayDbInfo() {

        // "SELECT * FROM books"
        String[] proj = {
                BookEntry._ID,
                BookEntry.COLUMN_TITLE,
                BookEntry.COLUMN_AUTHOR,
                BookEntry.COLUMN_PUBLISHER,
                BookEntry.COLUMN_YEAR,
                BookEntry.COLUMN_SUBJECT,
                BookEntry.COLUMN_PRICE,
                BookEntry.COLUMN_QUANTITY,
                BookEntry.COLUMN_SUPPLIER,
                BookEntry.COLUMN_SUPPLIER_PHONE
        };

        // Cursor contains all rows and order them by COLUMN_TITLE
        Cursor curs = getContentResolver().query(
                BookEntry.URI_FINAL,
                proj,
                null,
                null,
                BookEntry.COLUMN_TITLE,
                null);

        ListView lv = findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        lv.setEmptyView(emptyView);
        BookCursorAdapter adpt = new BookCursorAdapter(this, curs);
        lv.setAdapter(adpt);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adptV, View v, int pos, long id) {
                Intent intent = new Intent(MainActivity.this, NewEntryActivity.class);
                Uri currentPetUri = ContentUris.withAppendedId(BookEntry.URI_FINAL, id);
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });

    }

    private void newEntry() {

        ContentValues vals = new ContentValues();

        vals.put(BookEntry.COLUMN_TITLE, "Fantastic Beasts and Where to Find Them");
        vals.put(BookEntry.COLUMN_AUTHOR, "Newton Scamander");
        vals.put(BookEntry.COLUMN_PUBLISHER, "Obscurus Books");
        vals.put(BookEntry.COLUMN_YEAR, "1927");
        vals.put(BookEntry.COLUMN_SUBJECT, BookEntry.SUBJECT_CREATURES);
        vals.put(BookEntry.COLUMN_PRICE, "2");
        vals.put(BookEntry.COLUMN_QUANTITY, BookEntry.QUANTITY_DEFAULT);
        vals.put(BookEntry.COLUMN_SUPPLIER, "Albus Dumbledore");
        vals.put(BookEntry.COLUMN_SUPPLIER_PHONE, "605-475-6961");

        getContentResolver().insert(BookEntry.URI_FINAL, vals);
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
                newEntry();
                displayDbInfo();
                return true;
            case com.r4dixx.fbbookregistry.R.id.action_delete_data:
                // TODO this feature is now broken (it wasn't implemented correctly anyway
                this.deleteDatabase(BookDbHelper.DB_NAME);
                new BookDbHelper(this);
                displayDbInfo();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

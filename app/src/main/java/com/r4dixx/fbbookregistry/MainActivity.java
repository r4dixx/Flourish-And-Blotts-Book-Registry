package com.r4dixx.fbbookregistry;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.r4dixx.fbbookregistry.database.BookContract.BookEntry;
import com.r4dixx.fbbookregistry.database.BookDbHelper;

public class MainActivity extends AppCompatActivity {

    private BookDbHelper mBookDbHelper;

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

        mBookDbHelper = new BookDbHelper(this);
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
        Cursor cursor = getContentResolver().query(
                BookEntry.URI_FINAL,
                proj,
                null,
                null,
                BookEntry.COLUMN_TITLE,
                null);

        TextView tv = findViewById(R.id.text_view_books);

        // TODO: Modify heavily after being sure the database queries behave correctly in MainActivity. This is just for test and debugging purposes
        // Displays the header created in BookContract.BookEntry
        try {
            tv.setText("The current db table contains: "
                    + cursor.getCount()
                    + " entries."
                    + "\n\n");

            tv.append("The header is currently the following:\n"
                    + BookEntry._ID + " | "
                    + BookEntry.COLUMN_TITLE + " | "
                    + BookEntry.COLUMN_AUTHOR + " | "
                    + BookEntry.COLUMN_PUBLISHER + " | "
                    + BookEntry.COLUMN_YEAR + " | "
                    + BookEntry.COLUMN_SUBJECT + " | "
                    + BookEntry.COLUMN_PRICE + " | "
                    + BookEntry.COLUMN_QUANTITY + " | "
                    + BookEntry.COLUMN_SUPPLIER + " | "
                    + BookEntry.COLUMN_SUPPLIER_PHONE
                    + "\n");

            // Executing this outside the while loop as it more efficient this way performance-wise. Otherwise getColumnIndex() would be called for each row)
            int idIndex = cursor.getColumnIndex((BookEntry._ID));
            int titleIndex = cursor.getColumnIndex((BookEntry.COLUMN_TITLE));
            int authorIndex = cursor.getColumnIndex((BookEntry.COLUMN_AUTHOR));
            int publisherIndex = cursor.getColumnIndex((BookEntry.COLUMN_PUBLISHER));
            int yearIndex = cursor.getColumnIndex((BookEntry.COLUMN_YEAR));
            int subjectIndex = cursor.getColumnIndex((BookEntry.COLUMN_SUBJECT));
            int priceIndex = cursor.getColumnIndex((BookEntry.COLUMN_PRICE));
            int quantityIndex = cursor.getColumnIndex((BookEntry.COLUMN_QUANTITY));
            int supplierIndex = cursor.getColumnIndex((BookEntry.COLUMN_SUPPLIER));
            int supplierPhoneIndex = cursor.getColumnIndex((BookEntry.COLUMN_SUPPLIER_PHONE));

            while (cursor.moveToNext()) {

                int currentId = cursor.getInt((idIndex));
                String currentTitle = cursor.getString(titleIndex);
                String currentAuthor = cursor.getString(authorIndex);
                String currentPublisher = cursor.getString(publisherIndex);
                String currentYear = cursor.getString(yearIndex);
                int currentSubject = cursor.getInt(subjectIndex);
                int currentPrice = cursor.getInt(priceIndex);
                int currentQuantity = cursor.getInt(quantityIndex);
                String currentSupplier = cursor.getString(supplierIndex);
                String currentSupplierPhone = cursor.getString(supplierPhoneIndex);

                tv.append(("\n" + currentId + " | "
                        + currentTitle + " | "
                        + currentAuthor + " | "
                        + currentPublisher + " | "
                        + currentYear + " | "
                        + currentSubject + " | "
                        + currentPrice + " | "
                        + currentQuantity + " | "
                        + currentSupplier + " | "
                        + currentSupplierPhone
                        + "\n"
                ));
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

    private void newEntry() {

        SQLiteDatabase db = mBookDbHelper.getWritableDatabase();

        ContentValues vals = new ContentValues();

        vals.put(BookEntry.COLUMN_TITLE, "Fantastic Beasts and Where to Find Them");
        vals.put(BookEntry.COLUMN_AUTHOR, "Newton Scamander");
        vals.put(BookEntry.COLUMN_PUBLISHER, "Obscurus Books");
        vals.put(BookEntry.COLUMN_YEAR, "1927");
        vals.put(BookEntry.COLUMN_SUBJECT, BookEntry.SUBJECT_CREATURES);
        vals.put(BookEntry.COLUMN_PRICE, "2");
        vals.put(BookEntry.COLUMN_QUANTITY, BookEntry.QUANTITY_DEFAULT);
        vals.put(BookEntry.COLUMN_SUPPLIER, "Albus Dumbledore");
        vals.put(BookEntry.COLUMN_SUPPLIER_PHONE, "6054756961");

        db.insert(BookEntry.TABLE_NAME, null, vals);
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
                mBookDbHelper.close();
                this.deleteDatabase(BookDbHelper.DB_NAME);
                new BookDbHelper(this);
                displayDbInfo();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

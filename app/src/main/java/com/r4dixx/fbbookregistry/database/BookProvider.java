package com.r4dixx.fbbookregistry.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class BookProvider extends ContentProvider {

    private static final int BOOKS_ALL = 1;
    private static final int BOOK_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(BookContract.AUTHORITY, BookContract.PATH, BOOKS_ALL);
        sUriMatcher.addURI(BookContract.AUTHORITY, BookContract.PATH + "/#", BOOK_ID);
    }

    private BookDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new BookDbHelper(getContext());
        return true;
    }

    // CRUD methods start here (more like IQUD in Android)
    @Override
    public Uri insert(Uri uri, ContentValues vals) {
        final int matchUri = sUriMatcher.match(uri);
        switch (matchUri) {
            case BOOKS_ALL:
                return insertBook(uri, vals);
            default:
                throw new IllegalArgumentException("Unsupported insertion, URI: " + uri);
        }
    }

    private Uri insertBook(Uri uri, ContentValues vals) {
        // TODO Too much boilerplate code here, this could be simplified (also same code repeated in the update method)
        String title = vals.getAsString(BookContract.BookEntry.COLUMN_TITLE);
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title field should be filled");
        }

        String author = vals.getAsString(BookContract.BookEntry.COLUMN_AUTHOR);
        if (author == null || author.isEmpty()) {
            throw new IllegalArgumentException("Author field should be filled");
        }

        Integer year = vals.getAsInteger(BookContract.BookEntry.COLUMN_YEAR);
        if (year == null) {
            throw new IllegalArgumentException("Year field should be filled");
        }

        Integer price = vals.getAsInteger(BookContract.BookEntry.COLUMN_PRICE);
        if (price == null) {
            throw new IllegalArgumentException("Price field should be filled");
        } else if (price < 0) {
            throw new IllegalArgumentException("Price is negative!");
        }

        Integer quantity = vals.getAsInteger(BookContract.BookEntry.COLUMN_QUANTITY);
        if (quantity == null) {
            throw new IllegalArgumentException("Price field should be filled");
        } else if (quantity < 0) {
            throw new IllegalArgumentException("Quantity is negative!");
        }

        // No need to check for publisher, subject supplier and supplier phone, they can be null. For the phone number input type takes care of making sure it only contains numbers. Same for the year field. For the subject, the spinner takes care of making sure it only contains valid entries.

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(BookContract.BookEntry.TABLE_NAME, null, vals);
        if (id < 0) {
            Log.e("PetProvider", "Negative ID. New row cannot be inserted for the URI " + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public Cursor query(Uri uri, String[] proj, String selec, String[] selecArgs, String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor curs;
        int matchUri = sUriMatcher.match(uri);
        switch (matchUri) {
            case BOOKS_ALL:
                curs = db.query(BookContract.BookEntry.TABLE_NAME, proj, selec, selecArgs, null, null, sortOrder);
                break;
            case BOOK_ID:
                selec = BookContract.BookEntry._ID + "=?";
                selecArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                curs = db.query(BookContract.BookEntry.TABLE_NAME, proj, selec, selecArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query: " + uri + ". Unknown URI");
        }
        return curs;
    }

    @Override
    public int update(Uri uri, ContentValues vals, String selec, String[] selecArgs) {
        final int matchUri = sUriMatcher.match(uri);
        switch (matchUri) {
            case BOOKS_ALL:
                return updateBook(uri, vals, selec, selecArgs);
            case BOOK_ID:
                selec = BookContract.BookEntry._ID + "=?";
                selecArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, vals, selec, selecArgs);
            default:
                throw new IllegalArgumentException("Cannot update " + uri);
        }
    }

    private int updateBook(Uri uri, ContentValues vals, String selec, String[] selecArgs) {

        // TODO Too much boilerplate code here, this could be simplified
        // Sanity checks start here
        if (vals.containsKey(BookContract.BookEntry.COLUMN_TITLE)) {
            String title = vals.getAsString(BookContract.BookEntry.COLUMN_TITLE);
            if (title == null || title.isEmpty()) {
                throw new IllegalArgumentException("Title field should be filled");
            }
        }

        if (vals.containsKey(BookContract.BookEntry.COLUMN_AUTHOR)) {
            String author = vals.getAsString(BookContract.BookEntry.COLUMN_AUTHOR);
            if (author == null || author.isEmpty()) {
                throw new IllegalArgumentException("Author field should be filled");
            }
        }

        if (vals.containsKey(BookContract.BookEntry.COLUMN_YEAR)) {
            Integer year = vals.getAsInteger(BookContract.BookEntry.COLUMN_YEAR);
            if (year == null) {
                throw new IllegalArgumentException("Year field should be filled");
            }
        }

        if (vals.containsKey(BookContract.BookEntry.COLUMN_PRICE)) {
            Integer price = vals.getAsInteger(BookContract.BookEntry.COLUMN_PRICE);
            if (price == null) {
                throw new IllegalArgumentException("Price field should be filled");
            } else if (price < 0) {
                throw new IllegalArgumentException("Price is negative!");
            }
        }

        if (vals.containsKey(BookContract.BookEntry.COLUMN_PRICE)) {
            Integer quantity = vals.getAsInteger(BookContract.BookEntry.COLUMN_QUANTITY);
            if (quantity == null) {
                throw new IllegalArgumentException("Price field should be filled");
            } else if (quantity < 0) {
                throw new IllegalArgumentException("Quantity is negative!");
            }
        }

        // No need to check for publisher, subject supplier and supplier phone, they can be null. For the phone number input type takes care of making sure it only contains numbers. Same for the year field. For the subject, the spinner takes care of making sure it only contains valid entries.

        // End of sanity checks

        // Update the db only when there's value to update
        if (vals.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.update(BookContract.BookEntry.TABLE_NAME, vals, selec, selecArgs);
    }

    @Override
    public int delete(Uri uri, String selec, String[] selecArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int matchUri = sUriMatcher.match(uri);
        if (matchUri == BOOKS_ALL) {
            return db.delete(BookContract.BookEntry.TABLE_NAME, selec, selecArgs);
        } else if (matchUri == BOOK_ID) {
            selec = BookContract.BookEntry._ID + "=?";
            selecArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
            return db.delete(BookContract.BookEntry.TABLE_NAME, selec, selecArgs);
        } else {
            throw new IllegalArgumentException("Cannot delete Uri " + uri);
        }
    }
    // End of CRUD methods

    // Gets MIME type
    @Override
    public String getType(Uri uri) {
        final int matchUri = sUriMatcher.match(uri);
        switch (matchUri) {
            case BOOKS_ALL:
                return BookContract.BookEntry.CONTENT_LIST_TYPE;
            case BOOK_ID:
                return BookContract.BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("URI " + uri + " with match " + matchUri + " is unknown");
        }
    }
}
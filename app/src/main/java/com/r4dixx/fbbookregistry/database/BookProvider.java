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
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(BookContract.BookEntry.TABLE_NAME, null, vals);
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
        return 0;
    }

    @Override
    public int delete(Uri uri, String selec, String[] selecArgs) {
        return 0;
    }
    // End of CRUD methods

    // Gets MIME type
    @Override
    public String getType(Uri uri) {
        return null;
    }
}
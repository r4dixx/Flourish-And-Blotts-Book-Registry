package com.r4dixx.fbbookregistry.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class BookProvider extends ContentProvider {

    private static final int BOOKS_ALL = 1;
    private static final int BOOKS_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(BookContract.AUTHORITY, BookContract.PATH, BOOKS_ALL);
        sUriMatcher.addURI(BookContract.AUTHORITY, BookContract.PATH + "/#", BOOKS_ID);
    }

    private BookDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new BookDbHelper(getContext());
        return true;
    }

    // CRUD methods start here (more like IQUD in Android)

    @Override
    public Uri insert(Uri uri, ContentValues contentVals) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] proj, String selec, String[] selecArgs, String sortOrder) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues contentVals, String selec, String[] selecArgs) {
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
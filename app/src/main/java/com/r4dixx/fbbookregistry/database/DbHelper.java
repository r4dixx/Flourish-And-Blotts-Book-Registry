package com.r4dixx.fbbookregistry.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.r4dixx.fbbookregistry.database.Contract.BookEntry;


public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "books.db";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE =
                "CREATE TABLE " + BookEntry.TABLE_NAME + " ("
                        + BookEntry._ID + " INTEGER" + " PRIMARY KEY AUTOINCREMENT, "
                        + BookEntry.COLUMN_TITLE + " TEXT" + " NOT NULL, "
                        + BookEntry.COLUMN_AUTHOR + " TEXT" + " NOT NULL,"
                        + BookEntry.COLUMN_PUBLISHER + " TEXT, "
                        + BookEntry.COLUMN_YEAR + " INTEGER" + " NOT NULL, "
                        + BookEntry.COLUMN_SUBJECT + " INTEGER" + " NOT NULL" + " DEFAULT 0);";

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }
}

package com.r4dixx.fbbookregistry;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.r4dixx.fbbookregistry.database.BookContract;

public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context cont, Cursor curs) {
        super(cont, curs, 0);
    }

    @Override
    public View newView(Context cont, Cursor curs, ViewGroup par) {
        return LayoutInflater.from(cont).inflate(R.layout.list_item, par, false);
    }

    @Override
    public void bindView(View v, Context cont, Cursor curs) {
        TextView titleTV = v.findViewById(R.id.title);
        TextView authorTV = v.findViewById(R.id.author);
        TextView yearTV = v.findViewById(R.id.year);
        TextView priceTV = v.findViewById(R.id.price);
        TextView quantityTV = v.findViewById(R.id.quantity);
        ImageView removeBook = v.findViewById(R.id.remove_book);
        ImageView addBook = v.findViewById(R.id.add_book);

        int titleIndex = curs.getColumnIndex(BookContract.BookEntry.COLUMN_TITLE);
        int authorIndex = curs.getColumnIndex(BookContract.BookEntry.COLUMN_AUTHOR);
        int yearIndex = curs.getColumnIndex(BookContract.BookEntry.COLUMN_YEAR);
        int priceIndex = curs.getColumnIndex(BookContract.BookEntry.COLUMN_PRICE);
        int quantityIndex = curs.getColumnIndex(BookContract.BookEntry.COLUMN_QUANTITY);

        String title = curs.getString(titleIndex);
        String author = curs.getString(authorIndex);
        String year = curs.getString(yearIndex);
        String price = curs.getString(priceIndex);
        String quantity = curs.getString(quantityIndex);

        titleTV.setText(title);
        authorTV.setText(author);
        yearTV.setText(year);
        priceTV.setText(price);
        quantityTV.setText(quantity);

        long id = curs.getLong(curs.getColumnIndex(BookContract.BookEntry._ID));
        removeBook.setOnClickListener(new soldOnClickListener(id, cont, quantityTV));
        addBook.setOnClickListener(new addedOnClickListener(id, cont, quantityTV));
    }

    private class soldOnClickListener implements View.OnClickListener {
        long mId;
        Context mContext;
        TextView mQuantityTV;
        Uri mUri;

        public soldOnClickListener(long id, Context cont, TextView quantityTV) {
            this.mId = id;
            this.mContext = cont;
            this.mQuantityTV = quantityTV;
            mUri = ContentUris.withAppendedId(BookContract.BookEntry.URI_FINAL, id);
        }

        @Override
        public void onClick(View view) {
            int quantity = Integer.valueOf(mQuantityTV.getText().toString().trim()) - 1;

            if (quantity < 1) {
                Toast.makeText(mContext, R.string.toast_decrement_failed, Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(mContext, R.string.toast_book_sold, Toast.LENGTH_SHORT).show();
            }
            ContentValues vals = new ContentValues();
            vals.put(BookContract.BookEntry.COLUMN_QUANTITY, quantity);
            mContext.getContentResolver().update(mUri, vals, null, null);
        }
    }

    private class addedOnClickListener implements View.OnClickListener {
        long mId;
        Context mContext;
        TextView mQuantityTV;
        Uri mUri;

        public addedOnClickListener(long id, Context cont, TextView quantityTV) {
            this.mId = id;
            this.mContext = cont;
            this.mQuantityTV = quantityTV;
            mUri = ContentUris.withAppendedId(BookContract.BookEntry.URI_FINAL, id);
        }

        @Override
        public void onClick(View view) {
            int quantity = Integer.valueOf(mQuantityTV.getText().toString().trim()) + 1;
            ContentValues vals = new ContentValues();
            vals.put(BookContract.BookEntry.COLUMN_QUANTITY, quantity);
            mContext.getContentResolver().update(mUri, vals, null, null);
            Toast.makeText(mContext, R.string.toast_book_added, Toast.LENGTH_SHORT).show();
        }

    }
}

package com.r4dixx.fbbookregistry;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

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
    }
}
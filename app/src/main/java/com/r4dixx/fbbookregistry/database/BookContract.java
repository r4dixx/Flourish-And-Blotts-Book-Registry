package com.r4dixx.fbbookregistry.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class BookContract {

    private BookContract() {
        // Empty constructor to prevent accidental instantiation
    }

    public static final String AUTHORITY = "com.r4dixx.fbbookregistry";
    public static final Uri URI_BASE = Uri.parse("content://" + AUTHORITY);
    public static final String PATH = "books";

    public static final class BookEntry implements BaseColumns {

        public static final Uri URI_FINAL = Uri.withAppendedPath(URI_BASE, PATH);

        // MIME types
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH;

        public static final String TABLE_NAME = "books";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_PUBLISHER = "publisher";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_SUBJECT = "subject";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_SUPPLIER = "supplier";
        public static final String COLUMN_SUPPLIER_PHONE = "phone";

        public static final int QUANTITY_DEFAULT = 1;

        public static final int SUBJECT_UNKNOWN = 0;
        public static final int SUBJECT_ALCHEMY = 1;
        public static final int SUBJECT_ANCIENT_STUDIES = 2;
        public static final int SUBJECT_ANCIENT_RUNES = 3;
        public static final int SUBJECT_APPARITION = 4;
        public static final int SUBJECT_ARITHMANCY = 5;
        public static final int SUBJECT_ART = 6;
        public static final int SUBJECT_ASTRONOMY = 7;
        public static final int SUBJECT_CHARMS = 8;
        public static final int SUBJECT_CREATURES = 9;
        public static final int SUBJECT_DARK_ARTS = 10;
        public static final int SUBJECT_DIVINATION = 11;
        public static final int SUBJECT_FLYING = 12;
        public static final int SUBJECT_HERBOLOGY = 13;
        public static final int SUBJECT_MAGIC_HISTORY = 14;
        public static final int SUBJECT_MAGICAL_THEORY = 15;
        public static final int SUBJECT_MUGGLES_STUDY = 16;
        public static final int SUBJECT_MUGGLE_ART = 17;
        public static final int SUBJECT_MUSIC = 18;
        public static final int SUBJECT_POTIONS = 19;
        public static final int SUBJECT_TRANFIGURATION = 20;
        public static final int SUBJECT_XYLOMANCY = 21;

    }
}
package com.roguekingapps.jokesby.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.roguekingapps.jokesby.data.database.JokeContract.JokeEntry;
import com.roguekingapps.jokesby.di.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Manages database creation and version management.
 */
@Singleton
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "jokesby.db";
    private static final int DATABASE_VERSION = 1;

    @Inject
    public DatabaseOpenHelper(@ApplicationContext Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlCreateDatabase = "CREATE TABLE " +
                JokeEntry.TABLE_NAME + " (" +
                JokeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                JokeEntry.COLUMN_API_ID + " TEXT NOT NULL, " +
                JokeEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                JokeEntry.COLUMN_BODY + " TEXT NOT NULL, " +
                JokeEntry.COLUMN_USER + " TEXT NOT NULL " +
                JokeEntry.COLUMN_URL + " TEXT NOT NULL " +
                ");";
        sqLiteDatabase.execSQL(sqlCreateDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

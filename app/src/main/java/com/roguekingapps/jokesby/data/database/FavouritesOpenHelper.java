package com.roguekingapps.jokesby.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.roguekingapps.jokesby.data.database.JokeContract.FavouriteEntry;
import com.roguekingapps.jokesby.di.DatabaseVersion;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Manages database creation and version management.
 */
@Singleton
public class FavouritesOpenHelper extends SQLiteOpenHelper {

    @Inject
    FavouritesOpenHelper(Context context,
                         String databaseName,
                         @DatabaseVersion int databaseVersion) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlCreateDatabase = "CREATE TABLE " +
                FavouriteEntry.TABLE_NAME + " (" +
                FavouriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavouriteEntry.COLUMN_API_ID + " TEXT NOT NULL, " +
                FavouriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavouriteEntry.COLUMN_BODY + " TEXT NOT NULL, " +
                FavouriteEntry.COLUMN_USER + " TEXT NOT NULL, " +
                FavouriteEntry.COLUMN_URL + " TEXT NOT NULL " +
                ");";
        sqLiteDatabase.execSQL(sqlCreateDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

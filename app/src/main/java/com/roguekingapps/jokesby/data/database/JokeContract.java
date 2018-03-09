package com.roguekingapps.jokesby.data.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines supported URIs and columns for the tables of the jokes database.
 */
public final class JokeContract  {

    public static final String AUTHORITY = "com.roguekingapps.jokesby";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVOURITES = "favourites";
    public static final String PATH_RATED = "rated";

    private JokeContract() {

    }

    public static final class FavouriteEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITES).build();
        static final String TABLE_NAME = "favourites";
        public static final String COLUMN_API_ID = "api_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BODY = "body";
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_URL = "url";
    }

    public static final class RatedEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RATED).build();
        static final String TABLE_NAME = "rated";
        public static final String COLUMN_API_ID = "api_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BODY = "body";
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_RATING = "rating";
    }
}


package io.github.zkhan93.pm.data;

import android.provider.BaseColumns;

/**
 *
 * Created by Zeeshan Khan on 6/13/2016.
 */
public class Contract {
    public class Movies implements BaseColumns {
        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_IS_FAVORITE = "is_favorite";
    }

    public class Review implements BaseColumns {
        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";
    }

    public class Trailer implements BaseColumns {
        public static final String TABLE_NAME = "trailer";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_KEY = "key";
    }
}

package io.github.zkhan93.pm.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import io.github.zkhan93.pm.models.Movie;
import io.github.zkhan93.pm.models.Review;
import io.github.zkhan93.pm.models.Trailer;

/**
 * Movie DbHelper for storing data on local device
 * Created by Zeeshan Khan on 6/13/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "popular_movie.db";
    private static String CREATE_MOVIE_TABLE, CREATE_REVIEW_TABLE, CREATE_TRAILER_TABLE;
    private static String DROP_MOVIE_TABLE, DROP_TRAILER_TABLE, DROP_REVIEW_TABLE;

    {
        CREATE_MOVIE_TABLE = "CREATE TABLE " + Contract.Movies.TABLE_NAME + "(" + Contract
                .Movies._ID + " INTEGER PRIMARY KEY," +
                Contract.Movies.COLUMN_TITLE + " TEXT," +
                Contract.Movies.COLUMN_OVERVIEW + " TEXT," +
                Contract.Movies.COLUMN_POSTER_PATH + " TEXT," +
                Contract.Movies.COLUMN_RELEASE_DATE + " INTEGER," +
                Contract.Movies.COLUMN_RATING + " REAL," +
                Contract.Movies.COLUMN_POPULARITY + " REAL," +
                Contract.Movies.COLUMN_IS_FAVORITE + " INTEGER" +
                ")";
        CREATE_REVIEW_TABLE = "CREATE TABLE " + Contract.Review.TABLE_NAME + "(" +
                Contract.Review._ID + " TEXT PRIMARY KEY," +
                Contract.Review.COLUMN_MOVIE_ID + " INTEGER," +
                Contract.Review.COLUMN_AUTHOR + " TEXT," +
                Contract.Review.COLUMN_CONTENT + " TEXT," +
                Contract.Review.COLUMN_URL + " TEXT, FOREIGN KEY(" + Contract.Review
                .COLUMN_MOVIE_ID + ") REFERENCES " + Contract.Movies.TABLE_NAME + "(" + Contract
                .Movies._ID + "))";
        CREATE_TRAILER_TABLE = "CREATE TABLE " + Contract.Trailer.TABLE_NAME + "(" +
                Contract.Trailer._ID + " TEXT PRIMARY KEY," +
                Contract.Trailer.COLUMN_MOVIE_ID + " INTEGER," +
                Contract.Trailer.COLUMN_NAME + " TEXT," +
                Contract.Trailer.COLUMN_KEY + " TEXT," +
                "FOREIGN KEY(" + Contract.Trailer
                .COLUMN_MOVIE_ID + ") REFERENCES " + Contract.Movies.TABLE_NAME + "(" + Contract
                .Movies._ID + "))";
        DROP_MOVIE_TABLE = "DROP TABLE " + Contract.Movies.TABLE_NAME;
        DROP_REVIEW_TABLE = "DROP TABLE " + Contract.Review.TABLE_NAME;
        DROP_TRAILER_TABLE = "DROP TABLE " + Contract.Trailer.TABLE_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MOVIE_TABLE);
        db.execSQL(CREATE_REVIEW_TABLE);
        db.execSQL(CREATE_TRAILER_TABLE);
    }

    public MovieDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_MOVIE_TABLE);
        db.execSQL(DROP_REVIEW_TABLE);
        db.execSQL(DROP_TRAILER_TABLE);
        onCreate(db);
    }

    public int insertMovies(List<Movie> movies) {
        if (movies == null || movies.size() == 0)
            return 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        int count = 0;
        for (Movie m : movies) {
            values = new ContentValues();
            values.put(Contract.Movies._ID, m.getId());
            values.put(Contract.Movies.COLUMN_TITLE, m.getTitle());
            values.put(Contract.Movies.COLUMN_OVERVIEW, m.getOverview());
            values.put(Contract.Movies.COLUMN_POSTER_PATH, m.getPosterPath());
            values.put(Contract.Movies.COLUMN_RELEASE_DATE, m.getReleaseDate().getTime());
            values.put(Contract.Movies.COLUMN_RATING, m.getRating());
            values.put(Contract.Movies.COLUMN_POPULARITY, m.getPopularity());
            values.put(Contract.Movies.COLUMN_IS_FAVORITE, m.isFavorite());
            if (db.insert(Contract.Movies.TABLE_NAME, null, values) != -1)
                count += 1;
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return count;
    }

    public int insertReviews(List<Review> reviews, int movieId) {
        if (reviews == null || reviews.size() == 0)
            return 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        int count = 0;
        for (Review r : reviews) {
            values = new ContentValues();
            values.put(Contract.Review._ID, r.getId());
            values.put(Contract.Review.COLUMN_MOVIE_ID, movieId);
            values.put(Contract.Review.COLUMN_AUTHOR, r.getAuthor());
            values.put(Contract.Review.COLUMN_CONTENT, r.getContent());
            values.put(Contract.Review.COLUMN_URL, r.getUrl());
            db.insert(Contract.Review.TABLE_NAME, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return count;
    }

    public int insertTrailers(List<Trailer> trailers, int movieId) {
        if (trailers == null || trailers.size() == 0)
            return 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        int count = 0;
        for (Trailer t : trailers) {
            values = new ContentValues();
            values.put(Contract.Trailer._ID, t.getId());
            values.put(Contract.Trailer.COLUMN_MOVIE_ID, movieId);
            values.put(Contract.Trailer.COLUMN_NAME, t.getName());
            values.put(Contract.Trailer.COLUMN_KEY, t.getKey());
            db.insert(Contract.Trailer.TABLE_NAME, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return count;
    }
}

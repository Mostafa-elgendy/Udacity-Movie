package com.example.mostafa.movieapp.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Mostafa on 5/26/2016.
 * This Class is used to connect to sqlite database web services to
 * 1- all Movies
 * 2- all videos(Trailers) related to each movie
 * 3- all reviews related to each movie
 */

public class MoviesDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "movie.db";

    public MoviesDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE favourite_movies( id  TEXT PRIMARY KEY, " +
                "title TEXT NOT NULL ,path TEXT, overview TEXT, vote DOUBLE, prod_date TEXT);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS favourite_movies");
        onCreate(db);
    }

    //Insert data into table
    public boolean AddMovie(String id, String title, String path, String overview, double vote,
                            String prod_date) {
        SQLiteDatabase db = this.getWritableDatabase();

        boolean available = getMovieById(id);
        if (available) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", id);
            contentValues.put("title", title);
            contentValues.put("path", path);
            contentValues.put("overview", overview);
            contentValues.put("vote", vote);
            contentValues.put("prod_date", prod_date);

            db.insert("favourite_movies", null, contentValues);
            db.close();
            return true;
        } else
            return false;
    }

    //Select all data from the table
    public ArrayList<FilmItem> getMovies() {
        ArrayList<FilmItem> favourite = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * from favourite_movies";
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String title = cursor.getString(1);
            String posterPath = cursor.getString(2);
            String overview = cursor.getString(3);
            double vote_average = cursor.getDouble(4);
            String date = cursor.getString(5);
            FilmItem it = new FilmItem(id, posterPath, title, date, overview, vote_average);
            favourite.add(it);
        }
        cursor.close();
        db.close();
        return favourite;
    }

    //Select data for the given id
    private boolean getMovieById(String movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM favourite_movies WHERE id = ?";
        String m[] = {String.valueOf(movieId)};
        Cursor cursor = db.rawQuery(query, m);
        if (cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return false;
        } else
            return true;
    }

}

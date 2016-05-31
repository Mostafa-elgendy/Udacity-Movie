package com.example.mostafa.movieapp.Model;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Mostafa on 5/26/2016.
 * This Class is used to connect to all web services to
 * 1- all Movies
 * 2- all videos(Trailers) related to each movie
 * 3- all reviews related to each movie
 */
public class FilmData {
    private final String LOG_TAG = FilmData.class.getSimpleName();
    String key = "82f3dc217dca60ae08bb4a9337d789a9";

    /**
     * Take the String representing the type of movies to be returned
     * two types are allowed (popular - top_rated) and depend on the type select
     * the method prepare the url need to get data from the web service and
     * return it as JSONObject
     */
    public JSONObject getMovies(String type) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        JSONObject moviesJson = null;
        BufferedReader reader = null;
        // Will contain the raw JSON response as a string.
        String MoviesJsonStr = null;
        try {
            // Construct the URL for the themoviedb query
            String MOVIES_BASE_URL = "https://api.themoviedb.org/3";
            String path = "";
            if (type.equals("popular")) {
                path = "/movie/popular";

            } else if (type.equals("top_rated")) {
                path = "/movie/top_rated";
            }

            MOVIES_BASE_URL += path;
            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendQueryParameter("api_key", key)
                    .build();
            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line.concat("\n"));
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            MoviesJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Movies string: " + MoviesJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the Movie data, there's no point in attempting
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            moviesJson = new JSONObject(MoviesJsonStr);

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return moviesJson;
    }

    /**
     * Take the JSONObject representing the all movies data in JSON Format and
     * convert it to an ArrayList<FilmItem>
     * each FilmItem contain film data
     */
    public ArrayList<FilmItem> getAllMoviesFromJson(JSONObject movies) {
        String basePath = "http://image.tmdb.org/t/p/w185";
        // used to store JSON Array containing all movies
        JSONArray moviesJsonArray;
        // used to store all movies stored in JSON Array containing all movies
        ArrayList<FilmItem> allMovies = null;
        try {
            moviesJsonArray = movies.getJSONArray("results");
            allMovies = new ArrayList<>();
            for (int i = 0; i < moviesJsonArray.length(); i++) {
                // Get the JSON object representing the movie
                JSONObject movie = moviesJsonArray.getJSONObject(i);

                String id = movie.getString("id");
                String title = movie.getString("title");
                String date = movie.getString("release_date");
                String overview = movie.getString("overview");
                double vote_average = movie.getDouble("vote_average");
                String posterPath = basePath + movie.getString("poster_path");

                FilmItem it = new FilmItem(id, posterPath, title, date, overview, vote_average);
                allMovies.add(it);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return allMovies;

    }

    /**
     * Take the String representing the id of the movie
     * The method return all Trailer videos related to this movie
     * return it as JSONObject
     */
    public JSONObject getVideos(String id) {

        // HttpURLConnection and BufferedReader are declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        // Will contain the raw JSON response as a string.
        String MoviesJsonStr = null;

        try {
            // Construct the URL for the themoviedb query
            String Movies_URL = "http://api.themoviedb.org/3/movie/" + id + "/videos";
            Uri builtUri = Uri.parse(Movies_URL).buildUpon()
                    .appendQueryParameter("api_key", key)
                    .build();
            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            // Create the request to  open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line.concat("\n"));
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            MoviesJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Videos string: " + MoviesJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the Movies data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        JSONObject moviesJson = null;
        try {
            moviesJson = new JSONObject(MoviesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return moviesJson;
    }

    /**
     * Take the JSONObject representing the all videos data in JSON Format and
     * convert it to an ArrayList<TrailerItem>
     * each FilmItem contain film data
     */
    public ArrayList<TrailerItem> getAllVideosFromJson(JSONObject videos) {
        String basePath = "http://www.youtube.com/watch?v=";
        JSONArray videosJsonArray;
        ArrayList<TrailerItem> allVideos = null;
        try {
            videosJsonArray = videos.getJSONArray("results");
            allVideos = new ArrayList<>();
            for (int i = 0; i < videosJsonArray.length(); i++) {

                // Get the JSON object representing the video
                JSONObject movie = videosJsonArray.getJSONObject(i);

                String id = movie.getString("id");
                String key = movie.getString("key");
                String name = movie.getString("name");
                String site = basePath + key;
                TrailerItem trailerItem = new TrailerItem(id, name, site);
                allVideos.add(trailerItem);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return allVideos;
    }

    /**
     * Take the String representing the id of the movie
     * The method return all reviews related to this movie
     * return it as JSONObject
     */
    public JSONObject getReviews(String id) {

        // HttpURLConnection and BufferedReader are declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        // Will contain the raw JSON response as a string.
        String ReviewsJsonStr = null;

        try {
            // Construct the URL for the themoviedb query
            String Review_URL = "http://api.themoviedb.org/3/movie/" + id + "/reviews";
            Uri builtUri = Uri.parse(Review_URL).buildUpon()
                    .appendQueryParameter("api_key", key)
                    .build();
            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "Built URI " + builtUri.toString());
            // Create the request to  open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line.concat("\n"));
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            ReviewsJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Reviews string: " + ReviewsJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the Movies data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        JSONObject moviesJson = null;
        try {
            moviesJson = new JSONObject(ReviewsJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return moviesJson;
    }

    /**
     * Take the JSONObject representing the all reviews data in JSON Format and
     * convert it to an ArrayList<ReviewItem>
     * each FilmItem contain film data
     */
    public ArrayList<ReviewItem> getAllReviewsFromJson(JSONObject reviews) {
        JSONArray reviewsJsonArray;
        ArrayList<ReviewItem> allReviews = null;
        try {
            reviewsJsonArray = reviews.getJSONArray("results");
            allReviews = new ArrayList<>();

            for (int i = 0; i < reviewsJsonArray.length(); i++) {
                // Get the JSON object representing the video
                JSONObject movie = reviewsJsonArray.getJSONObject(i);
                String id = movie.getString("id");
                String author = movie.getString("author");
                String content = movie.getString("content");
                ReviewItem reviewItem = new ReviewItem(id, author, content);
                allReviews.add(reviewItem);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return allReviews;
    }
}

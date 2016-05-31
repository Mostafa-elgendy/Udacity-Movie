package com.example.mostafa.movieapp.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.mostafa.movieapp.Model.FilmData;
import com.example.mostafa.movieapp.Model.FilmItem;
import com.example.mostafa.movieapp.Model.MoviesDatabaseHandler;
import com.example.mostafa.movieapp.Model.MoviesPicassoAdapter;
import com.example.mostafa.movieapp.R;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mostafa on 5/26/2016.
 * this is the fragment used to view grid of movies
 */
public class MainActivityFragment extends Fragment {

    private MoviesPicassoAdapter moviesAdapter;
    ArrayList<String> imageUrls = new ArrayList<>();
    ArrayList<FilmItem> allMovies = new ArrayList<>();

    boolean mTwoPane;

    public MainActivityFragment() {

        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.movie_details) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        getMovies();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent det = new Intent(getActivity(), SettingActivity.class);
            startActivity(det);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getMovies() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String type = prefs.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_popular));
        if (type != null && type.equals("favourite")) {
            MoviesDatabaseHandler db = new MoviesDatabaseHandler(getActivity());
            allMovies = db.getMovies();
            if (allMovies.size() > 0) {
                moviesAdapter.clear();
                for (int i = 0; i < allMovies.size(); i++) {
                    moviesAdapter.add(allMovies.get(i).getPath());
                }
            } else {
                AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
                build.setTitle("View Movies From Favourite");
                build.setMessage("No Movies Found In Favourite");
                build.setPositiveButton("OK", null);
                AlertDialog alert = build.create();
                alert.show();
            }

        } else {
            FetchMoviesTask f = new FetchMoviesTask();
            f.execute(type);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) v.findViewById(R.id.gridView);

        moviesAdapter = new MoviesPicassoAdapter(getActivity(), R.layout.grid_item_layout, imageUrls);
        gridView.setAdapter(moviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                FilmItem item = allMovies.get(position);
                if (mTwoPane) {
                    Bundle arguments = new Bundle();

                    arguments.putString("id", item.getId());
                    arguments.putString("title", item.getTitle());
                    arguments.putString("path", item.getPath());
                    arguments.putString("date", item.getDate());
                    arguments.putString("overview", item.getOverview());
                    arguments.putDouble("vote", item.getVote_average());

                    DetailsActivityFragment fragment = new DetailsActivityFragment();
                    fragment.setArguments(arguments);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_details, fragment)
                            .commit();
                } else {

                    //Create intent
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra("id", item.getId());
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("overview", item.getOverview());
                    intent.putExtra("path", item.getPath());
                    intent.putExtra("vote", item.getVote_average());
                    intent.putExtra("date", item.getDate());
                    startActivity(intent);
                }

            }
        });
        return v;
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setIndeterminate(false);
            dialog.setMessage("Loading Movies  ...");
            dialog.show();
        }

        @Override
        protected String[] doInBackground(String... type) {
            FilmData webservice = new FilmData();
            JSONObject result = webservice.getMovies(type[0]);
            allMovies = webservice.getAllMoviesFromJson(result);
            Log.d(LOG_TAG, result.toString());
            String[] imageUrls = new String[allMovies.size()];
            for (int i = 0; i < imageUrls.length; i++) {
                imageUrls[i] = allMovies.get(i).getPath();
            }
            return imageUrls;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                moviesAdapter.clear();
                for (String movieStr : result) {
                    Log.d(LOG_TAG, movieStr);
                    moviesAdapter.add(movieStr);
                }
                dialog.hide();
                dialog.dismiss();
            }
        }

    }
}

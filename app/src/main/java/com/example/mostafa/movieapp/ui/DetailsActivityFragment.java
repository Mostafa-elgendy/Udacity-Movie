package com.example.mostafa.movieapp.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mostafa.movieapp.Model.FilmData;
import com.example.mostafa.movieapp.Model.MoviesDatabaseHandler;
import com.example.mostafa.movieapp.Model.ReviewItem;
import com.example.mostafa.movieapp.Model.TrailerItem;
import com.example.mostafa.movieapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Mostafa on 5/26/2016.
 * Details fragment
 */
public class DetailsActivityFragment extends Fragment {
    ImageButton favourite;
    ListView trailList;
    ListView reviewsList;

    ArrayAdapter<String> videosAdapter;
    ArrayAdapter<String> reviewsAdapter;

    ArrayList<TrailerItem> allVideos = new ArrayList<>();
    ArrayList<ReviewItem> allReviews = new ArrayList<>();

    String id;

    public DetailsActivityFragment() {
    }


    @Override
    public void onStart() {
        super.onStart();
        getVideos();

    }

    private void getVideos() {
        FetchVideoTask f = new FetchVideoTask();
        f.execute(id);
    }

    private void getReviews() {
        FetchReviewTask f = new FetchReviewTask();
        f.execute(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {


            //Intent i = getActivity().getIntent();
            id = arguments.getString("id");
            final String title = arguments.getString("title");
            final String path = arguments.getString("path");
            final String date = arguments.getString("date");
            final String overview = arguments.getString("overview");
            final double vote = arguments.getDouble("vote", 0);

            TextView titleTextView = (TextView) v.findViewById(R.id.title);
            titleTextView.setText(title);
            TextView dateTextView = (TextView) v.findViewById(R.id.date);
            dateTextView.setText(date);
            TextView overviewTextView = (TextView) v.findViewById(R.id.overview);
            overviewTextView.setText(overview);
            TextView rateTextView = (TextView) v.findViewById(R.id.rate);
            rateTextView.setText("" + vote);
            ImageView imageView = (ImageView) v.findViewById(R.id.image);
            Picasso.with(getActivity())
                    .load(path)
                    .fit()
                    .into(imageView);

            favourite = (ImageButton) v.findViewById(R.id.favourite);
            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MoviesDatabaseHandler db = new MoviesDatabaseHandler(getActivity());
                    boolean state = db.AddMovie(id, title, path, overview, vote, date);
                    if (state) {
                        AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
                        build.setTitle("Add Movie To Favourite");
                        build.setMessage("Movie Added To Favourite Successfully");

                        build.setPositiveButton("OK", null);
                        AlertDialog alert = build.create();
                        alert.show();
                    } else {
                        AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
                        build.setTitle("Add Movie To Favourite");
                        build.setMessage("Movie Is Already Added To Favourite Before");
                        build.setPositiveButton("OK", null);
                        AlertDialog alert = build.create();
                        alert.show();
                    }
                }
            });
            Button review = (Button) v.findViewById(R.id.rating);
            review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getReviews();
                }
            });
            trailList = (ListView) v.findViewById(R.id.listView);
            trailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String path = allVideos.get(position).getSite();
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(path));

                    startActivity(intent);
                }
            });
            reviewsList = (ListView) v.findViewById(R.id.reviewsList);
            reviewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ReviewItem reviewer = allReviews.get(position);
                    AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
                    build.setTitle("Review Details");
                    String message = "ID : " + reviewer.getId() + "\n";
                    message += "Author : " + reviewer.getAuthor() + "\n";
                    message += "Content : " + reviewer.getContent();

                    build.setMessage(message);
                    build.setPositiveButton("OK", null);
                    AlertDialog alert = build.create();
                    alert.show();

                }
            });
        }
        return v;
    }

    public class FetchVideoTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchVideoTask.class.getSimpleName();
        ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setIndeterminate(false);
            dialog.setMessage("Loading Videos  ...");
            dialog.show();
        }

        @Override
        protected String[] doInBackground(String... id) {
            FilmData webservice = new FilmData();
            JSONObject result = webservice.getVideos(id[0]);
            allVideos = webservice.getAllVideosFromJson(result);
            Log.d(LOG_TAG, result.toString());
            String[] VideoUrls = new String[allVideos.size()];
            for (int i = 0; i < VideoUrls.length; i++) {
                VideoUrls[i] = "Trailer " + (i + 1);
            }
            return VideoUrls;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                videosAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, result);
                trailList.setAdapter(videosAdapter);
                dialog.hide();
                dialog.dismiss();
            }
        }

    }

    public class FetchReviewTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchReviewTask.class.getSimpleName();
        ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setIndeterminate(false);
            dialog.setMessage("Loading Reviews  ...");
            dialog.show();
        }

        @Override
        protected String[] doInBackground(String... id) {
            FilmData webservice = new FilmData();
            JSONObject result = webservice.getReviews(id[0]);
            allReviews = webservice.getAllReviewsFromJson(result);
            Log.d(LOG_TAG, result.toString());
            String[] reviews = new String[allReviews.size()];
            for (int i = 0; i < reviews.length; i++) {
                reviews[i] = allReviews.get(i).getAuthor();
            }
            return reviews;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                reviewsAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, result);
                reviewsList.setAdapter(reviewsAdapter);
                dialog.hide();
                dialog.dismiss();
            }
        }

    }
}
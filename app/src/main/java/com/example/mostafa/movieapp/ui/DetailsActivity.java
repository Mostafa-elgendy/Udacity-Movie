package com.example.mostafa.movieapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mostafa.movieapp.R;

/**
 * Created by Mostafa on 5/26/2016.
 * Detailed Activity
 */

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Intent i = getIntent();

            Bundle arguments = new Bundle();

            arguments.putString("id", i.getStringExtra("id"));
            arguments.putString("title", i.getStringExtra("title"));
            arguments.putString("path", i.getStringExtra("path"));
            arguments.putString("date", i.getStringExtra("date"));
            arguments.putString("overview", i.getStringExtra("overview"));
            arguments.putDouble("vote", i.getDoubleExtra("vote", 0));

            DetailsActivityFragment fragment = new DetailsActivityFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_details, fragment)
                    .commit();
        }
    }

}

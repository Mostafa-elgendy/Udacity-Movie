package com.example.mostafa.movieapp.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.mostafa.movieapp.R;

/**
 * Created by Mostafa on 5/26/2016.
 */
public class MainActivity extends ActionBarActivity {
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_details) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
        /*    if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_details, new DetailsActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }*/
        } else {
            mTwoPane = false;
        }
    }

}

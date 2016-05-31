package com.example.mostafa.movieapp.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mostafa on 5/28/2016.
 * This is custom Array Adapter which will store the data
 */
public class MoviesPicassoAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;
    private int layoutResourceId;

    private ArrayList<String> imageUrls;

    public MoviesPicassoAdapter(Context context, int layoutResourceId, ArrayList<String> imageUrls) {
        super(context, layoutResourceId, imageUrls);
        this.context = context;
        this.imageUrls = imageUrls;
        this.layoutResourceId = layoutResourceId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        Picasso.with(context)
                .load(imageUrls.get(position))
                .fit()
                .into((ImageView) convertView);
        return convertView;
    }
}
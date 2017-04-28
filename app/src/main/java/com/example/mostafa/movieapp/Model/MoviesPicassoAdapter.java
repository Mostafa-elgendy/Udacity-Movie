package com.example.mostafa.movieapp.Model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mostafa.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mostafa on 5/28/2016.
 * This is custom Array Adapter which will store the data
 * Updated 28/04/2017 by adding Recycle View
 */
public class MoviesPicassoAdapter extends RecyclerView.Adapter<MoviesPicassoAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> imageUrls;

    public MoviesPicassoAdapter(Context context, ArrayList<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
        Log.e("Size", "Size" + imageUrls.size() + "  " + getItemCount());
    }

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView FilmPoster;
        public TextView FilmTitle;

        public MyViewHolder(View view) {
            super(view);
            FilmPoster = (ImageView) view.findViewById(R.id.image);
            FilmTitle = (TextView) view.findViewById(R.id.film_title);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.FilmTitle.setText("Title");
        Log.e("enter", "Enter");
        Picasso.with(context)
                .load(imageUrls.get(position))
                .fit()
                .into(holder.FilmPoster);
    }


    @Override
    public int getItemCount() {
        return imageUrls.size();
    }
}
    /*    public MoviesPicassoAdapter(Context context, int layoutResourceId, ) {
        super(context, layoutResourceId, imageUrls);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        inflater = LayoutInflater.from(context);
    }
*/
  /*  @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(layoutResourceId, parent, false);

        }
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        Picasso.with(context)
                .load(imageUrls.get(position))
                .fit()
                .into(image);
        return convertView;
    }*/




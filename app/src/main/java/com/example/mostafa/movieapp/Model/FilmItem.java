package com.example.mostafa.movieapp.Model;

/**
 * Created by Mostafa on 5/26/2016.
 * This Class is used to store all movie properties
 * we store movie id ,title , production date ,
 * overview  , average rating votes, a path to movie poster
 */
public class FilmItem {
    private String id;
    private String title;
    private String date;
    public String overview;
    private double vote_average;
    private String path;

    public FilmItem(String id, String path, String title, String date, String overview, double vote_average) {
        this.id = id;
        this.path = path;
        this.title = title;
        this.date = date;
        this.overview = overview;
        this.vote_average = vote_average;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }


    public String getOverview() {
        return overview;
    }


    public double getVote_average() {
        return vote_average;
    }

}


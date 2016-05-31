package com.example.mostafa.movieapp.Model;

/**
 * Created by Mostafa on 5/26/2016.
 * This Class is used to store all trailer properties
 * we store trailer id , key , name , type
 * a path to the trailer on youtube
 */

public class TrailerItem {


    private String id;
    private String name;
    private String site;


    public TrailerItem(String id, String name, String site) {
        this.id = id;
        this.name = name;
        this.site = site;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

}

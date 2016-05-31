package com.example.mostafa.movieapp.Model;

/**
 * Created by Mostafa on 5/30/2016.
 * This Class is used to store all review properties
 * we store review id ,author and
 * the content of the review
 */

public class ReviewItem {
    String id;
    String author;
    String content;

    public ReviewItem(String id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }


    public String getContent() {
        return content;
    }
}

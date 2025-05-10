package com.example.movieRecommender;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;

public class Movie {
    String title;
    Set<String> genres;

    Movie(String title, String[] genres) {
        this.title = title;
        this.genres = new HashSet<>(Arrays.asList(genres));
    }

    public String getTitle() {
        return title;
    }

    public Set<String> getGenres() {
        return genres;
    }
}

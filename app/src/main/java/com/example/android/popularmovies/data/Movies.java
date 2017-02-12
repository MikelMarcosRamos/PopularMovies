package com.example.android.popularmovies.data;

import java.util.List;

public class Movies {
    protected Integer page;
    protected List<Movie> results;
    protected Integer total_results;
    protected Integer total_pages;

    public void setMovies(List<Movie> movies){
        this.results = movies;
    }

    public List<Movie> getMovies(){
        return this.results;
    }
}

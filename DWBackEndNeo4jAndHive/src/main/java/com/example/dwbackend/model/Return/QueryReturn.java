package com.example.dwbackend.model.Return;


import com.example.dwbackend.model.item.Movie;
import lombok.Data;

import java.util.ArrayList;

@Data
public class QueryReturn {
    long time;
    ArrayList<Movie> movieList;

    public QueryReturn(long time, ArrayList<Movie> movieList) {
        this.time = time;
        this.movieList = movieList;
    }
}

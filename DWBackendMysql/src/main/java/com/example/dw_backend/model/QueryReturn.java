package com.example.dw_backend.model;

import com.example.dw_backend.model.mysql.Movie;
import lombok.Data;

import java.util.List;

/**
 * 查询结果返回类
 *
 */
@Data
public class QueryReturn {
    long time;
    List<Movie> movieList;

    public QueryReturn(List<Movie> movieList, long time) {
        this.time = time;
        this.movieList = movieList;
    }
}


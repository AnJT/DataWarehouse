package com.example.dw_backend.service.mysql;

import com.example.dw_backend.dao.mysql.MovieRepository;
import com.example.dw_backend.dao.mysql.MovieSourceRepository;
import com.example.dw_backend.model.mysql.Movie;
import com.example.dw_backend.model.mysql.MovieSource;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieSourceRepository movieSourceRepository;
    private long scoreTime;
    private long titleTime;


    public MovieService(MovieRepository movieRepository, MovieSourceRepository movieSourceRepository) {
        this.movieRepository = movieRepository;
        this.movieSourceRepository = movieSourceRepository;
    }

    public List<Movie> parsingScoreList(double score, String comparison) {
        List<Movie> result = new ArrayList<>();

        long startTime = System.currentTimeMillis();    //获取开始时间
        result = this.movieRepository.getMovieCountByScore(score, comparison);
        long endTime = System.currentTimeMillis();    //获取结束时间
        this.scoreTime = endTime - startTime;

        return result;
    }

    public List<String> getMovieSource(String asinn) {
        List<String> ret = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        ret = this.movieRepository.findMovieBySource(asinn);
        long endTime = System.currentTimeMillis();
        long qtime = endTime - startTime;
        this.scoreTime = qtime;
        return ret;
    }

    public List<Movie> parsingTitleList(String title) {
        List<Movie> result = new ArrayList<>();

        long startTime = System.currentTimeMillis();    //获取开始时间
        result = this.movieRepository.findAllByTitle(title);
        long endTime = System.currentTimeMillis();    //获取结束时间
        this.titleTime = endTime - startTime;

        return result;
    }

    public long getScoreTime() {
        return scoreTime;
    }

    public long getTitleTime() {
        return titleTime;
    }
}

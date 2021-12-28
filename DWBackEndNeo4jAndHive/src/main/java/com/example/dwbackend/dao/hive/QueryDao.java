package com.example.dwbackend.dao.hive;

import com.example.dwbackend.model.item.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class QueryDao {

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public ArrayList<Movie> findByTitle(String title, int limit){
        String sql = "select id, title, score, release_date from movie where title = ? limit ?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, title, limit);
        return toMovieList(list);
    }

    public ArrayList<Movie> findByActor(String actor, int limit){
        String sql = "select id, title, score, release_date from movie where array_contains(actor, ?) limit ?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, actor, limit);
        return toMovieList(list);
    }

    public ArrayList<Movie> findByDirector(String director, int limit){
        String sql = "select id, title, score, release_date from movie where array_contains(director, ?) limit ?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, director, limit);
        return toMovieList(list);
    }

    public ArrayList<Movie> findByLabel(String label, int limit){
        String sql = "select id, title, score, release_date from movie where array_contains(label, ?) limit ?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, label, limit);
        return toMovieList(list);
    }

    public ArrayList<Movie> findByScore(Float score, String comparison, int limit){
        String sql = null;
        switch (comparison) {
            case "greater":
                sql = "select id, title, score, release_date from movie where score > ? limit ?";
                break;
            case "less":
                sql = "select id, title, score, release_date from movie where score < ? limit ?";
                break;
            case "equal":
                sql = "select id, title, score, release_date from movie where score = ? limit ?";
                break;
            default:
                sql = "select id, title, score, release_date from movie where score > ? limit ?";
        }
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, score, limit);
        return toMovieList(list);
    }

    private ArrayList<Movie> toMovieList(List<Map<String, Object>> list){
        ArrayList<Movie> arrayList = new ArrayList<>();
        for (Map<String, Object> map: list){
            Movie movie = new Movie();
            movie.setProductId(String.valueOf(map.get("id")));
            movie.setTitle(String.valueOf(map.get("title")));
            if (map.get("score") != null) {
                movie.setScore(((Double) map.get("score")).floatValue());
            }
            try{
                DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dutyDay = new Date(simpleDateFormat.parse(map.get("release_date").toString()).getTime());
                movie.setTime(dutyDay);
            }
            catch (ParseException ignored){

            }
            arrayList.add(movie);
        }
        return arrayList;
    }
}

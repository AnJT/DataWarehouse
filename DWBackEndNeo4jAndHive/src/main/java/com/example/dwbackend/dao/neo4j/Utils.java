package com.example.dwbackend.dao.neo4j;

import com.example.dwbackend.model.item.Movie;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static ArrayList<Movie> recordToMovie(List<Record> recordList) {
        ArrayList<Movie> movies = new ArrayList<>();
        for (Record record : recordList) {
            Movie movie = new Movie();
            movie.setProductId(record.get("asin").asString());
            movie.setTitle(record.get("title").asString());
            try{
                movie.setScore((float) record.get("score").asDouble());
            }
            catch (Exception e){
                movie.setScore(0.0f);
            }
            movies.add(movie);
        }
        return movies;
    }
}

package com.example.dwbackend.dao.hive;

import com.example.dwbackend.model.item.Score;
import com.example.dwbackend.model.item.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class StatisticsDto {

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public ArrayList<Score> getAllScore(){
        String sql = "select score, size(movie_id) as count from score_movie";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        ArrayList<Score> arrayList = new ArrayList<>();
        for (Map<String, Object> map: list){
            Score score = new Score();
            score.setScore(Float.parseFloat(String.valueOf(map.get("score"))));
            score.setCount(Integer.parseInt(String.valueOf(map.get("count"))));
            arrayList.add(score);
        }
        Collections.sort(arrayList, new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                if(o1.getScore() > o2.getScore())
                    return -1;
                else if(o1.getScore() < o2.getScore())
                    return 1;
                return 0;
            };
        });
        System.out.println(arrayList);
        return arrayList;
    }

    public ArrayList<Statistics> getAllLabel(int limit){
        String sql = "select name, size(movie_id) as count from label_movie limit ?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, limit);
        ArrayList<Statistics> arrayList = new ArrayList<>();
        for (Map<String, Object> map: list){
            Statistics statistics = new Statistics();
            statistics.setName(String.valueOf(map.get("name")));
            statistics.setCount(Integer.parseInt(String.valueOf(map.get("count"))));
            arrayList.add(statistics);
        }
        System.out.println(arrayList);
        return arrayList;
    }

    public ArrayList<Statistics> getAllActor(int limit){
        String sql = "select name, size(movie_id) as count from actor_movie limit ?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, limit);
        ArrayList<Statistics> arrayList = new ArrayList<>();
        for (Map<String, Object> map: list){
            Statistics statistics = new Statistics();
            statistics.setName(String.valueOf(map.get("name")));
            statistics.setCount(Integer.parseInt(String.valueOf(map.get("count"))));
            arrayList.add(statistics);
        }
        return arrayList;
    }

    public ArrayList<Statistics> getAllDirector(int limit){
        String sql = "select name, size(movie_id) as count from director_movie limit ?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, limit);
        ArrayList<Statistics> arrayList = new ArrayList<>();
        for (Map<String, Object> map: list){
            Statistics statistics = new Statistics();
            statistics.setName(String.valueOf(map.get("name")));
            statistics.setCount(Integer.parseInt(String.valueOf(map.get("count"))));
            arrayList.add(statistics);
        }
        return arrayList;
    }

    public Integer getMovieCountByScore(float score, String comparison){
        String sql = null;
        switch (comparison) {
            case "greater":
                sql = "select size(movie_id) as count from score_movie where score > ?";
                break;
            case "less":
                sql = "select size(movie_id) as count from score_movie where score < ?";
                break;
            case "equal":
                sql = "select size(movie_id) as count from score_movie where score = ?";
                break;
            default:
                sql = "select size(movie_id) as count from score_movie where score > ?";
        }
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, score);
        int res = 0;
        for (Map<String, Object> map: list){
            res += Integer.parseInt(String.valueOf(map.get("count")));
        }
        return res;
    }

    public Integer getMovieCountByYear(int year, String comparison){
        try {
            String sql = null;
            switch (comparison) {
                case "greater":
                    sql = "with year_c as (select distinct (year), year_count from release_date where year > ?) select sum(year_count) from year_c";
                    break;
                case "less":
                    sql = "with year_c as (select distinct (year), year_count from release_date where year < ?) select sum(year_count) from year_c";
                    break;
                case "equal":
                    sql = "select distinct year_count from release_date where year = ?";
                    break;
                default:
                    sql = "with year_c as (select distinct (year), year_count from release_date where year > ?) select sum(year_count) from year_c";
            }
            Map<String, Object> map = jdbcTemplate.queryForMap(sql, year);
            return getReturn(map);
        }
        catch (EmptyResultDataAccessException e){
            return 0;
        }
    }

    public Integer getMovieCountByMonth(int year, int month, String comparison){
        try {
            String sql = null;
            Map<String, Object> map = new HashMap<>();
            switch (comparison) {
                case "greater":
                    sql = "with month_c as (select distinct (year), month, month_count from release_date where year > ? or (year = ? and month > ?)) select sum(month_count) from month_c";
                    map = jdbcTemplate.queryForMap(sql, year, year, month);
                    break;
                case "less":
                    sql = "with month_c as (select distinct (year), month, month_count from release_date where year < ? or (year = ? and month < ?)) select sum(month_count) from month_c";
                    map = jdbcTemplate.queryForMap(sql, year, year, month);
                    break;
                case "equal":
                    sql = "select distinct month_count from release_date where year = ? and month = ?";
                    map = jdbcTemplate.queryForMap(sql, year, month);
                    break;
                default:
                    sql = "with month_c as (select distinct (year), month, month_count from release_date where year > ? or (year = ? and month > ?)) select sum(month_count) from month_c";
                    map = jdbcTemplate.queryForMap(sql, year, year, month);
            }
            return getReturn(map);
        }
        catch (EmptyResultDataAccessException e){
            return 0;
        }
    }

    public Integer getMovieCountByDay(int year, int month, int day, String comparison){
        try {
            String sql = null;
            Map<String, Object> map = new HashMap<>();
            switch (comparison) {
                case "greater":
                    sql = "with dat_c as (select distinct (year), month, day, day_count from release_date where (year = ? and month = ? and day > ?) or ((year = ? and month > ?) or year > ?)) select sum(day_count) from dat_c";
                    map = jdbcTemplate.queryForMap(sql, year, month, day, year, month, year);
                    break;
                case "less":
                    sql = "with day_c as (select distinct (year), month, day, day_count from release_date where (year = ? and month = ? and day < ?) or ((year = ? and month < ?) or year < ?)) select sum(day_count) from day_c";
                    map = jdbcTemplate.queryForMap(sql, year, month, day, year, month, year);
                    break;
                case "equal":
                    sql = "select distinct day_count from release_date where year = ? and month = ? and day = ?";
                    map = jdbcTemplate.queryForMap(sql, year, month, day);
                    break;
                default:
                    sql = "with dat_c as (select distinct (year), month, day, day_count from release_date where (year = ? and month = ? and day > ?) or ((year = ? and month > ?) or year > ?)) select sum(day_count) from dat_c";
                    map = jdbcTemplate.queryForMap(sql, year, month, day, year, month, year);
            }
            return getReturn(map);
        }
        catch (EmptyResultDataAccessException e){
            return 0;
        }
    }

    public Integer getMovieCountBySeason(int year, int season){
        try {
            String sql = "select distinct (season_count) from release_date where year = ? and season = ?;";
            Map<String, Object> map = jdbcTemplate.queryForMap(sql, year, season);
            return getReturn(map);
        }
        catch (EmptyResultDataAccessException e){
            return 0;
        }
    }

    private Integer getReturn(Map<String, Object> map){
        System.out.println(map);
        Optional<Object> o = map.values().stream().findFirst();
        if (o.isEmpty()){
            return 0;
        }
        System.out.println(o.get());
        return Integer.parseInt(String.valueOf(o.get()));
    }
}

package com.example.dwbackend.service.hive;

import com.example.dwbackend.dao.hive.StatisticsDto;
import com.example.dwbackend.model.Return.ScoreReturn;
import com.example.dwbackend.model.Return.StatisticsReturn;
import com.example.dwbackend.model.item.Score;
import com.example.dwbackend.model.item.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class StatisticsService {

    @Autowired
    private StatisticsDto statisticsDto;

    public ScoreReturn getAllScore(){
        long startTime = System.currentTimeMillis();    //获取开始时间
        ArrayList<Score> scores = statisticsDto.getAllScore();
        long endTime = System.currentTimeMillis();    //获取结束时间
        return new ScoreReturn(endTime - startTime, scores);
    }

    public StatisticsReturn getAllLabel(int limit){
        long startTime = System.currentTimeMillis();    //获取开始时间
        ArrayList<Statistics> statistics = statisticsDto.getAllLabel(limit);
        long endTime = System.currentTimeMillis();    //获取结束时间
        return new StatisticsReturn(endTime - startTime, statistics);
    }

    public StatisticsReturn getAllActor(int limit){
        long startTime = System.currentTimeMillis();    //获取开始时间
        ArrayList<Statistics> statistics = statisticsDto.getAllActor(limit);
        long endTime = System.currentTimeMillis();    //获取结束时间
        return new StatisticsReturn(endTime - startTime, statistics);
    }

    public StatisticsReturn getAllDirector(int limit){
        long startTime = System.currentTimeMillis();    //获取开始时间
        ArrayList<Statistics> statistics = statisticsDto.getAllDirector(limit);
        long endTime = System.currentTimeMillis();    //获取结束时间
        return new StatisticsReturn(endTime - startTime, statistics);
    }

    public HashMap<String, Long> getMovieCountByScore(float score, String comparison){
        HashMap<String, Long> map = new HashMap<>();
        long startTime = System.currentTimeMillis();    //获取开始时间
        Integer res = statisticsDto.getMovieCountByScore(score, comparison);
        long endTime = System.currentTimeMillis();    //获取结束时间
        map.put("time", endTime - startTime);
        map.put("Count", Long.valueOf(res));
        return map;
    }

    public HashMap<String, Long> getMovieCountByYear(int year, String comparison){
        HashMap<String, Long> map = new HashMap<>();
        long startTime = System.currentTimeMillis();    //获取开始时间
        Integer res = statisticsDto.getMovieCountByYear(year, comparison);
        long endTime = System.currentTimeMillis();    //获取结束时间
        map.put("time", endTime - startTime);
        map.put("Count", Long.valueOf(res));
        return map;
    }

    public HashMap<String, Long> getMovieCountByMonth(int year, int month, String comparison){
        HashMap<String, Long> map = new HashMap<>();
        long startTime = System.currentTimeMillis();    //获取开始时间
        Integer res = statisticsDto.getMovieCountByMonth(year, month, comparison);
        long endTime = System.currentTimeMillis();    //获取结束时间
        map.put("time", endTime - startTime);
        map.put("Count", Long.valueOf(res));
        return map;
    }

    public HashMap<String, Long> getMovieCountByDay(int year, int month, int day, String comparison){
        HashMap<String, Long> map = new HashMap<>();
        long startTime = System.currentTimeMillis();    //获取开始时间
        Integer res = statisticsDto.getMovieCountByDay(year, month, day, comparison);
        long endTime = System.currentTimeMillis();    //获取结束时间
        map.put("time", endTime - startTime);
        map.put("Count", Long.valueOf(res));
        return map;
    }

    public HashMap<String, Long> getMovieCountBySeason(int year, int season){
        HashMap<String, Long> map = new HashMap<>();
        long startTime = System.currentTimeMillis();    //获取开始时间
        Integer res = statisticsDto.getMovieCountBySeason(year, season);
        long endTime = System.currentTimeMillis();    //获取结束时间
        map.put("time", endTime - startTime);
        map.put("Count", Long.valueOf(res));
        return map;
    }
}

package com.example.dwbackend.controller.hive;

import com.example.dwbackend.model.Return.ScoreReturn;
import com.example.dwbackend.model.Return.StatisticsReturn;
import com.example.dwbackend.service.hive.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/statistics/hive")
public class StatisticsController {

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StatisticsService statisticsService;

    @ResponseBody
    @GetMapping("score-all")
    public ScoreReturn getAllScore(){
        return statisticsService.getAllScore();
    }

    @ResponseBody
    @GetMapping("label-all")
    public StatisticsReturn getAllLabel(@RequestParam int limit){
        return statisticsService.getAllLabel(limit);
    }

    @ResponseBody
    @GetMapping("actor-all")
    public StatisticsReturn getAllActor(@RequestParam int limit){
        return statisticsService.getAllActor(limit);
    }

    @ResponseBody
    @GetMapping("director-all")
    public StatisticsReturn getAllDirector(@RequestParam int limit){
        return statisticsService.getAllDirector(limit);
    }

    @ResponseBody
    @GetMapping("score")
    public Map<String, Long> getMovieCountByScore(@RequestParam float score, @RequestParam String comparison) {
        return statisticsService.getMovieCountByScore(score, comparison);
    }

    @ResponseBody
    @GetMapping("time")
    public Map<String, Long> getMovieCountByTime(@RequestParam String time, @RequestParam String type, @RequestParam String comparison) {
        HashMap<String, Long> result = new HashMap<>();
        String[] ymd = time.split("-");
        int year = Integer.parseInt(ymd[0]);
        int month = Integer.parseInt(ymd[1]);
        int day = Integer.parseInt(ymd[2]);
        int season = day / 4 + 1;
        switch (type) {
            case "year":
                result = statisticsService.getMovieCountByYear(year, comparison);
                break;
            case "month":
                result = statisticsService.getMovieCountByMonth(year, month, comparison);
                break;
            case "day":
                result = statisticsService.getMovieCountByDay(year, month, day, comparison);
                break;
            case "season":
                result = statisticsService.getMovieCountBySeason(year, season);
                break;
        }
        return result;
    }
}

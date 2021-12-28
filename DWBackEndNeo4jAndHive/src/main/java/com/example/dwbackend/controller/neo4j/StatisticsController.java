package com.example.dwbackend.controller.neo4j;

import com.example.dwbackend.model.Return.ScoreReturn;
import com.example.dwbackend.model.Return.StatisticsReturn;
import com.example.dwbackend.service.neo4j.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController("neo4jStatisticsController")
@RequestMapping("/statistics/neo4j")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @ResponseBody
    @GetMapping("score")
    public Map<String, Long> getMovieCountByScore(@RequestParam float score, @RequestParam String comparison) {
        return statisticsService.getScoreCount(score, comparison);
    }

    @ResponseBody
    @GetMapping("time")
    public Map<String, Long> getMovieCountByTime(@RequestParam String time, @RequestParam String type, @RequestParam String comparison) {
        return statisticsService.getTimeCount(time, type, comparison);
    }

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
}

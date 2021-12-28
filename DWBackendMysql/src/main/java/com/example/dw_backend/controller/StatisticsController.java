package com.example.dw_backend.controller;

import com.example.dw_backend.model.ScoreReturn;
import com.example.dw_backend.model.StatisticsReturn;
import com.example.dw_backend.model.mysql.Score;
import com.example.dw_backend.service.mysql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/statistics", produces = {MediaType.APPLICATION_JSON_VALUE})
public class StatisticsController {

    @Autowired
    private TimeService timeService;
    @Autowired
    private DirectorService directorService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private LabelService labelService;
    @Autowired
    private ScoreService scoreService;

    @ResponseBody
    @RequestMapping(value = "/mysql/time", method = RequestMethod.GET)
    public StatisticsReturn getCountByTime(@RequestParam String time, @RequestParam String type, @RequestParam String comparison) {
        HashMap<String, Integer> result = new HashMap<>();
        String[] ymd = time.split("-");
        int year = Integer.parseInt(ymd[0]);
        int month = Integer.parseInt(ymd[1]);
        int day = Integer.parseInt(ymd[2]);
        int season = day / 4 + 1;
        long staTime = System.currentTimeMillis();
        switch (type) {
            case "year":
                result = timeService.parserYearCount(year, comparison);
                break;
            case "month":
                result = timeService.parserMonthCount(year, month, comparison);
                break;
            case "day":
                result = timeService.parserDayCount(year, month, day, comparison);
                break;
            case "season":
                result = timeService.parserSeasonCount(year, season);
                break;
        }
        long endTime = System.currentTimeMillis();
        long qtime = endTime - staTime;
//        return result;
        return new StatisticsReturn(result, qtime);
    }

    @ResponseBody
    @RequestMapping(value = "/mysql/score", method = RequestMethod.GET)
    public StatisticsReturn getCountByScore(double score, String large) {
        long staTime = System.currentTimeMillis();
        HashMap<String, Integer> ret = this.scoreService.parserCount(score, large);
        long endTime = System.currentTimeMillis();
        long qtime = endTime - staTime;
//        return scoreService.parserCount(score, large);
        return new StatisticsReturn(ret, qtime);
    }

    @ResponseBody
    @RequestMapping(value = "/mysql/score-all", method = RequestMethod.GET)
    public ScoreReturn getAllScore() {
        long staTime = System.currentTimeMillis();
        ArrayList<Score> scores = (ArrayList<Score>) this.scoreService.findAll();
        long endTime = System.currentTimeMillis();
        long qtime = endTime - staTime;
//        return scoreService.findAll();
        return new ScoreReturn(scores, qtime);
    }

//    @ResponseBody
//    @RequestMapping(value = "/mysql/director-all", method = RequestMethod.GET)
//    public HashMap<String, String> getAllDirector(@RequestParam int limit) {
//        return directorService.findAll(limit);
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/mysql/actor-all", method = RequestMethod.GET)
//    public HashMap<String, Integer> getAllActor(@RequestParam int limit) {
//        return actorService.findAll(limit);
//    }

//    @ResponseBody
//    @RequestMapping(value = "/mysql/label-all", method = RequestMethod.GET)
//    public HashMap<String, Integer> getAllLabel(@RequestParam int limit) {
//        return labelService.findAll(limit);
//    }
    @ResponseBody
    @RequestMapping(value = "/mysql/label-all", method = RequestMethod.GET)
    public StatisticsReturn getAllLabel(@RequestParam int limit) {
        return new StatisticsReturn(labelService.findAll(limit), labelService.getMovieTime());
    }

    @ResponseBody
    @RequestMapping(value = "/mysql/director-all", method = RequestMethod.GET)
    public StatisticsReturn getAllDirector(@RequestParam int limit) {
        return new StatisticsReturn(directorService.findAll(limit), directorService.getMovieTime());
    }

    @ResponseBody
    @RequestMapping(value = "/mysql/actor-all", method = RequestMethod.GET)
    public StatisticsReturn getAllActor(@RequestParam int limit) {
        return new StatisticsReturn(actorService.findAll(limit), actorService.getMovieTime());
    }
}

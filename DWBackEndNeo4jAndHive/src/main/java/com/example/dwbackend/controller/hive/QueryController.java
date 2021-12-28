package com.example.dwbackend.controller.hive;


import com.example.dwbackend.model.Return.QueryReturn;
import com.example.dwbackend.service.hive.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/query/hive")
public class QueryController {

    @Autowired
    private QueryService queryService;

    @ResponseBody
    @GetMapping("title")
    public QueryReturn findMovieByTitle(@RequestParam String title, @RequestParam int limit) {
        return queryService.findByTitle(title, limit);
    }

    @ResponseBody
    @GetMapping("actor")
    public QueryReturn findMovieByActor(@RequestParam String actorName, @RequestParam int limit) {
        return queryService.findByActor(actorName, limit);
    }

    @ResponseBody
    @GetMapping("director")
    public QueryReturn findMovieByDirector(@RequestParam String directorName, @RequestParam int limit) {
        return queryService.findByDirector(directorName, limit);
    }

    @ResponseBody
    @GetMapping("label")
    public QueryReturn findMovieByLabel(@RequestParam String labelName, @RequestParam int limit) {
        return queryService.findByLabel(labelName, limit);
    }

    @ResponseBody
    @GetMapping("score")
    public QueryReturn findMovieByScore(@RequestParam Float score, @RequestParam String comparison, @RequestParam int limit) {
        return queryService.findByScore(score, comparison, limit);
    }
}

package com.example.dwbackend.controller.neo4j;


import com.example.dwbackend.model.Return.QueryReturn;
import com.example.dwbackend.service.neo4j.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController("neo4jQueryController")
@RequestMapping("/query/neo4j")
public class QueryController {

    @Autowired
    private QueryService queryService;

    @ResponseBody
    @GetMapping("title")
    public QueryReturn findMovieByTitle(@RequestParam String title, @RequestParam int limit) {
        return queryService.generateMovieList(title, "title", limit);
    }

    @ResponseBody
    @GetMapping("actor")
    public QueryReturn findMovieByActor(@RequestParam String actorName, @RequestParam int limit) {
        return queryService.generateMovieList(actorName, "actor", limit);
    }

    @ResponseBody
    @GetMapping("director")
    public QueryReturn findMovieByDirector(@RequestParam String directorName, @RequestParam int limit) {
        return queryService.generateMovieList(directorName, "director", limit);
    }

    @ResponseBody
    @GetMapping("label")
    public QueryReturn findMovieByLabel(@RequestParam String labelName, @RequestParam int limit) {
        return queryService.generateMovieList(labelName, "label", limit);
    }

    @ResponseBody
    @GetMapping("score")
    public QueryReturn findMovieByScore(@RequestParam Float score, @RequestParam String comparison) {
        return queryService.findByScore(score, comparison);
    }
}

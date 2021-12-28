package com.example.dw_backend.controller;

import com.example.dw_backend.model.mysql.Movie;
//import com.example.dw_backend.model.mysql.returnValue.QueryReturn;
import com.example.dw_backend.model.QueryReturn;
import com.example.dw_backend.service.mysql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


/**
 * 返回值都是MovieList
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/query", produces = {MediaType.APPLICATION_JSON_VALUE})
public class QueryController {

    @Autowired
    private DirectorService directorService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private LabelService labelService;
    @Autowired
    private MovieService movieService;


    /**
     * 查询给定导演的电影,返回电影列表
     *
     * @param directorName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/mysql/director", method = RequestMethod.GET)
    public QueryReturn getDirectorMovieList(@RequestParam String directorName) {
        return new QueryReturn(directorService.parsingDirectorMovie(directorName), directorService.getMovieTime());
    }

    /**
     * 查询给定演员的电影,返回电影列表
     *
     * @param actorName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/mysql/actor", method = RequestMethod.GET)
    public QueryReturn getActorMovieList(@RequestParam String actorName) {
        return new QueryReturn(actorService.parsingActorMovie(actorName), actorService.getMovieTime());
    }

    /**
     * 查询给定标签的电影,返回电影列表
     *
     * @param labelName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/mysql/label", method = RequestMethod.GET)
    public QueryReturn getLabelMovieList(@RequestParam String labelName) {
        return new QueryReturn(labelService.parsingLabelMovie(labelName), labelService.getMovieTime());
    }


    @ResponseBody
    @RequestMapping(value = "/mysql/score", method = RequestMethod.GET)
    public QueryReturn getScoreMovieList(@RequestParam double score, @RequestParam String comparison) {
        return new QueryReturn(movieService.parsingScoreList(score, comparison), movieService.getScoreTime());
    }

    @ResponseBody
    @RequestMapping(value = "/mysql/title", method = RequestMethod.GET)
    public QueryReturn getTitleMovieList(@RequestParam String title) {
        return new QueryReturn(movieService.parsingTitleList(title), movieService.getTitleTime());
    }

    @ResponseBody
    @RequestMapping(value = "/mysql/source", method = RequestMethod.GET)
    public List<String> getMovieBySource(@RequestParam String asinn) {
        return this.movieService.getMovieSource(asinn);
    }

}

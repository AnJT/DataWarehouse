package com.example.dw_backend.controller;

import com.example.dw_backend.model.RelationReturn;
import com.example.dw_backend.service.mysql.ActorService;
import com.example.dw_backend.service.mysql.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/relationships", produces = {MediaType.APPLICATION_JSON_VALUE})
public class RelationController {
    @Autowired
    private DirectorService directorService;
    @Autowired
    private ActorService actorService;

    /**
     * 给出导演，查询合作的演员
     *
     * @param directorName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/mysql/director-actor", method = RequestMethod.GET)
    public RelationReturn getActorByDirector(String directorName) {
        return new RelationReturn(directorService.parsingGetActorList(directorName), directorService.getActorTime());
    }

    /**
     * 给出导演，查询合作的导演
     *
     * @param directorName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/mysql/director-director", method = RequestMethod.GET)
    public RelationReturn getDirectorByDirector(String directorName) {
        return new RelationReturn(directorService.parsingGetDirectorList(directorName), directorService.getDirectorTime());
    }

    /**
     * 给出演员，查询合作过的导演
     *
     * @param actorName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/mysql/actor-director", method = RequestMethod.GET)
    public RelationReturn getDirectorByActor(String actorName) {
        return new RelationReturn(actorService.parsingGetDirectorList(actorName), actorService.getDirectorTime());
    }

    /**
     * 给出演员，查询合作过的演员
     *
     * @param actorName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/mysql/actor-actor", method = RequestMethod.GET)
    public RelationReturn getActorByActor(String actorName) {
        return new RelationReturn(actorService.parsingGetActorList(actorName), actorService.getActorTime());
    }
}

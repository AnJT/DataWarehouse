package com.example.dwbackend.controller.hive;

import com.example.dwbackend.model.Return.RelationReturn;
import com.example.dwbackend.service.hive.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/relationships/hive")
public class RelationController {

    @Autowired
    private RelationService relationService;

    @ResponseBody
    @GetMapping("actor-actor")
    public RelationReturn findActorByActor(@RequestParam String actorName, @RequestParam Integer limit) {
        return relationService.findActorByActor(actorName, limit);
    }

    @ResponseBody
    @GetMapping("director-actor")
    public RelationReturn findDirectorByActor(@RequestParam String actorName, @RequestParam Integer limit) {
        return relationService.findDirectorByActor(actorName, limit);
    }

    @ResponseBody
    @GetMapping("actor-director")
    public RelationReturn findActorByDirector(@RequestParam String directorName, @RequestParam Integer limit) {
        return relationService.findActorByDirector(directorName, limit);
    }

    @ResponseBody
    @GetMapping("director-director")
    public RelationReturn findDirectorByDirector(@RequestParam String directorName, @RequestParam Integer limit) {
        return relationService.findDirectorByDirector(directorName, limit);
    }
}

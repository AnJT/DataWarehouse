package com.example.dwbackend.controller.neo4j;

import com.example.dwbackend.model.Return.RelationReturn;
import com.example.dwbackend.service.neo4j.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("neo4jRelationController")
@RequestMapping("/relationships/neo4j")
public class RelationController {

    @Autowired
    private RelationService relationService;

    @ResponseBody
    @GetMapping("actor-actor")
    public RelationReturn findActorByActor(@RequestParam String actorName, @RequestParam Integer limit) {
        return relationService.generateRelation(actorName, "Actor", "Actor", limit);
    }

    @ResponseBody
    @GetMapping("actor-director")
    public RelationReturn findDirectorByActor(@RequestParam String actorName, @RequestParam Integer limit) {
        return relationService.generateRelation(actorName, "Actor", "Director", limit);
    }

    @ResponseBody
    @GetMapping("director-actor")
    public RelationReturn findActorByDirector(@RequestParam String directorName, @RequestParam Integer limit) {
        return relationService.generateRelation(directorName, "Director", "Actor", limit);
    }

    @ResponseBody
    @GetMapping("director-director")
    public RelationReturn findDirectorByDirector(@RequestParam String directorName, @RequestParam Integer limit) {
        return relationService.generateRelation(directorName, "Director", "Director", limit);
    }
}

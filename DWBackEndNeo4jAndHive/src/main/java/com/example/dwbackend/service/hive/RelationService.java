package com.example.dwbackend.service.hive;

import com.example.dwbackend.dao.hive.RelationDto;
import com.example.dwbackend.model.Return.RelationReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class RelationService {

    @Autowired
    private RelationDto relationDto;

    public RelationReturn findActorByActor(String actor, Integer limit) {
        long startTime = System.currentTimeMillis();    //获取开始时间
        ArrayList<HashMap<String, String>> res = relationDto.findActorByActor(actor, limit);
        long endTime = System.currentTimeMillis();    //获取结束时间
        return new RelationReturn(endTime - startTime, res);
    }

    public RelationReturn findDirectorByActor(String actor, Integer limit) {
        long startTime = System.currentTimeMillis();    //获取开始时间
        ArrayList<HashMap<String, String>> res = relationDto.findDirectorByActor(actor, limit);
        long endTime = System.currentTimeMillis();    //获取结束时间
        return new RelationReturn(endTime - startTime, res);
    }

    public RelationReturn findActorByDirector(String director, Integer limit) {
        long startTime = System.currentTimeMillis();    //获取开始时间
        ArrayList<HashMap<String, String>> res = relationDto.findActorByDirector(director, limit);
        long endTime = System.currentTimeMillis();    //获取结束时间
        return new RelationReturn(endTime - startTime, res);
    }

    public RelationReturn findDirectorByDirector(String director, Integer limit) {
        long startTime = System.currentTimeMillis();    //获取开始时间
        ArrayList<HashMap<String, String>> res = relationDto.findDirectorByDirector(director, limit);
        long endTime = System.currentTimeMillis();    //获取结束时间
        return new RelationReturn(endTime - startTime, res);
    }
}

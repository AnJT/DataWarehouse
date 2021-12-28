package com.example.dwbackend.service.neo4j;

import com.example.dwbackend.dao.neo4j.RelationDto;
import com.example.dwbackend.model.Return.RelationReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("neo4jRelationService")
public class RelationService {

    @Autowired
    private RelationDto relationDto;

    public RelationReturn generateRelation(final String startName, final String startLabel, final String endLabel, final int limit) {

        List<HashMap<String, String>> ans = new ArrayList<>();

        long startTime = System.currentTimeMillis();    //获取开始时间
        ArrayList<HashMap<String, String>> recordList = relationDto.findRelation(startName, startLabel, endLabel, limit);
        long endTime = System.currentTimeMillis();    //获取开始时间
        long time = endTime - startTime;
        return new RelationReturn(time, recordList);
    }
}

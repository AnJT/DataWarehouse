package com.example.dwbackend.service.neo4j;

import com.example.dwbackend.dao.neo4j.QueryDao;
import com.example.dwbackend.model.item.Movie;
import com.example.dwbackend.model.Return.QueryReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("neo4jQueryService")
public class QueryService {

    @Autowired
    private QueryDao queryDao;

    public QueryReturn findByScore(Float score, String cmp) {
        ArrayList<Movie> recordList;
        long startTime = System.currentTimeMillis();    //获取开始时间
        recordList = queryDao.queryMovieByScore(score, cmp, 20);
        long endTime = System.currentTimeMillis();    //获取结束时间
        long time = endTime - startTime;
        return new QueryReturn(time, recordList);
    }

    public QueryReturn generateMovieList(String data, String queryType, int limit) {

        List<HashMap<String, String>> ans = new ArrayList<>();
        ArrayList<Movie> recordList;
        long startTime = System.currentTimeMillis();    //获取开始时间
        switch (queryType) {
            case "title":
            case "Title":
                recordList = queryDao.queryByTitle(data, limit);
                break;
            case "actor":
            case "Actor":
                recordList = queryDao.queryMovieByActor(data, limit);
                break;
            case "director":
            case "Director":
                recordList = queryDao.queryMovieByDirector(data, limit);
                break;
            case "label":
            case "Label":
                recordList = queryDao.queryMovieByLabel(data, limit);
                break;
            default:
                System.out.println("error");
                return new QueryReturn(0, null);
        }
        long endTime = System.currentTimeMillis();    //获取开始时间
        long time = endTime - startTime;
        return new QueryReturn(time, recordList);
    }
}

package com.example.dwbackend.service.hive;

import com.example.dwbackend.dao.hive.QueryDao;
import com.example.dwbackend.model.item.Movie;
import com.example.dwbackend.model.Return.QueryReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class QueryService {

    @Autowired
    private QueryDao queryDao;

    public QueryReturn findByTitle(String title, int limit){
        long startTime = System.currentTimeMillis();    //获取开始时间
        ArrayList<Movie> res = queryDao.findByTitle(title, limit);
        long endTime = System.currentTimeMillis();    //获取结束时间
        return new QueryReturn(endTime - startTime, res);
    }

    public QueryReturn findByActor(String actor, int limit){
        long startTime = System.currentTimeMillis();    //获取开始时间
        ArrayList<Movie> res = queryDao.findByActor(actor, limit);
        long endTime = System.currentTimeMillis();    //获取结束时间
        return new QueryReturn(endTime - startTime, res);
    }

    public QueryReturn findByDirector(String director, int limit){
        long startTime = System.currentTimeMillis();    //获取开始时间
        ArrayList<Movie> res = queryDao.findByDirector(director, limit);
        long endTime = System.currentTimeMillis();    //获取结束时间
        return new QueryReturn(endTime - startTime, res);
    }

    public QueryReturn findByLabel(String label, int limit){
        long startTime = System.currentTimeMillis();    //获取开始时间
        ArrayList<Movie> res = queryDao.findByLabel(label, limit);
        long endTime = System.currentTimeMillis();    //获取结束时间
        return new QueryReturn(endTime - startTime, res);
    }

    public QueryReturn findByScore(Float score, String comparison, int limit){
        long startTime = System.currentTimeMillis();    //获取开始时间
        ArrayList<Movie> res = queryDao.findByScore(score, comparison, limit);
        long endTime = System.currentTimeMillis();    //获取结束时间
        return new QueryReturn(endTime - startTime, res);
    }
}

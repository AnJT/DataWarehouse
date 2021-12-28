package com.example.dwbackend.service.neo4j;

import com.example.dwbackend.dao.neo4j.StatisticsDto;
import com.example.dwbackend.model.Return.ScoreReturn;
import com.example.dwbackend.model.Return.StatisticsReturn;
import com.example.dwbackend.model.item.Score;
import com.example.dwbackend.model.item.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service("neo4jStatisticsService")
public class StatisticsService {

    @Autowired
    private StatisticsDto statisticsDto;

    public HashMap<String, Long> getCountByType(final String data, final String nodeLabel) {
        List<HashMap<String, String>> ans = new ArrayList<>();
        long startTime = System.currentTimeMillis();    //获取开始时间
        int res = statisticsDto.getCountByType(data, nodeLabel);
        long endTime = System.currentTimeMillis();    //获取开始时间
        long time = endTime - startTime;
        HashMap<String, Long> ret = new HashMap<String, Long>();
        ret.put("time", time);
        ret.put("count", (long) res);
        return ret;
    }

    public HashMap<String, Long> getScoreCount(Float score, String cmp) {
        List<HashMap<String, String>> ans = new ArrayList<>();
        long startTime = System.currentTimeMillis();    //获取开始时间
        int res = statisticsDto.getScoreCount(score, cmp);
        long endTime = System.currentTimeMillis();    //获取开始时间
        long time = endTime - startTime;
        HashMap<String, Long> ret = new HashMap<String, Long>();
        ret.put("time", time);
        ret.put("count", (long) res);
        return ret;
    }

    public HashMap<String, Long> getTimeCount(String timeData, String timeType, String cmp) {

        List<HashMap<String, String>> ans = new ArrayList<>();
        int recordCount;
        List<String> dataList;
        long startTime;    //获取开始时间
        switch (timeType) {
            case "year":
                dataList = Arrays.asList(timeData.split("-"));
                startTime = System.currentTimeMillis();
                recordCount = statisticsDto.queryTimeByYear(dataList.get(0), cmp);
                break;
            case "season":
                dataList = Arrays.asList(timeData.split("-"));
                int month = Integer.parseInt(dataList.get(1));
                int season = 1 + (month - 1) / 3;
                startTime = System.currentTimeMillis();
                recordCount = statisticsDto.queryTimeBySeason(dataList.get(0), Integer.toString(season), cmp);
                break;
            case "month":
                dataList = Arrays.asList(timeData.split("-"));
                startTime = System.currentTimeMillis();
                recordCount = statisticsDto.queryTimeByMonth(dataList.get(0), dataList.get(1), cmp);
                break;
            case "day":
                dataList = Arrays.asList(timeData.split("-"));
                startTime = System.currentTimeMillis();
                recordCount = statisticsDto.queryTimeByDay(dataList.get(0), dataList.get(1), dataList.get(2), cmp);
                break;
            default:
                System.out.println("error");
                return new HashMap<>();
        }
        long endTime = System.currentTimeMillis();    //获取开始时间
        long time = endTime - startTime;
        HashMap<String, Long> ret = new HashMap<String, Long>();
        ret.put("time", time);
        ret.put("count", (long) recordCount);
        return ret;
    }

    public ScoreReturn getAllScore(){
        long startTime = System.currentTimeMillis();    //获取开始时间
        ArrayList<Score> scores = statisticsDto.getAllScore();
        long endTime = System.currentTimeMillis();    //获取结束时间
        return new ScoreReturn(endTime - startTime, scores);
    }

    public StatisticsReturn getAllLabel(int limit){
        long startTime = System.currentTimeMillis();    //获取开始时间
        ArrayList<Statistics> statistics = statisticsDto.getAllLabel(limit);
        long endTime = System.currentTimeMillis();    //获取结束时间
        return new StatisticsReturn(endTime - startTime, statistics);
    }

    public StatisticsReturn getAllActor(int limit){
        long startTime = System.currentTimeMillis();    //获取开始时间
        ArrayList<Statistics> statistics = statisticsDto.getAllActor(limit);
        long endTime = System.currentTimeMillis();    //获取结束时间
        return new StatisticsReturn(endTime - startTime, statistics);
    }

    public StatisticsReturn getAllDirector(int limit){
        long startTime = System.currentTimeMillis();    //获取开始时间
        ArrayList<Statistics> statistics = statisticsDto.getAllDirector(limit);
        long endTime = System.currentTimeMillis();    //获取结束时间
        return new StatisticsReturn(endTime - startTime, statistics);
    }
}

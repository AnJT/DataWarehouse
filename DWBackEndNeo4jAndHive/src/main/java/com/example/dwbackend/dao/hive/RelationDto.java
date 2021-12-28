package com.example.dwbackend.dao.hive;

import com.alibaba.fastjson.JSONArray;
import com.example.dwbackend.model.item.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.IntStream;

@Repository
public class RelationDto {

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public ArrayList<HashMap<String, String>> findActorByActor(String actor, Integer limit){
        try {
            String sql = "select cooperation_name, cooperation_count from actor_actor where name = ?";
            Map<String, Object> map = jdbcTemplate.queryForMap(sql, actor);
            return toReturnList(map, limit);
        }
        catch (EmptyResultDataAccessException e){
            return new ArrayList<HashMap<String, String>>();
        }
    }

    public ArrayList<HashMap<String, String>> findDirectorByActor(String actor, Integer limit){
        try{
            String sql = "select cooperation_name, cooperation_count from actor_director where name = ?";
            Map<String, Object> map = jdbcTemplate.queryForMap(sql, actor);
            return toReturnList(map, limit);
        }
        catch (EmptyResultDataAccessException e){
            return new ArrayList<HashMap<String, String>>();
        }
    }

    public ArrayList<HashMap<String, String>> findActorByDirector(String director, Integer limit){
        try {
            String sql = "select cooperation_name, cooperation_count from director_actor where name = ?";
            Map<String, Object> map = jdbcTemplate.queryForMap(sql, director);
            System.out.println("hh");
            return toReturnList(map, limit);
        }
        catch (EmptyResultDataAccessException e){
            return new ArrayList<HashMap<String, String>>();
        }
    }

    public ArrayList<HashMap<String, String>> findDirectorByDirector(String director, Integer limit){
        try {
            String sql = "select cooperation_name, cooperation_count from director_director where name = ?";
            Map<String, Object> map = jdbcTemplate.queryForMap(sql, director);
            return toReturnList(map, limit);
        }
        catch (EmptyResultDataAccessException e){
            return new ArrayList<HashMap<String, String>>();
        }
    }

    private ArrayList<HashMap<String, String>> toReturnList(Map<String, Object> map, Integer limit){
        ArrayList<HashMap<String, String>> res= new ArrayList<>();
        String[] cooperation_name = ((JSONArray) JSONArray.parse(String.valueOf(map.get("cooperation_name")))).toArray(new String[] {});
        Integer[] cooperation_count = ((JSONArray) JSONArray.parse(String.valueOf(map.get("cooperation_count")))).toArray(new Integer[] {});
        IntStream.range(0, cooperation_count.length).forEach(i -> res.add(new HashMap<>() {{
            put("name", String.valueOf(cooperation_name[i]));
            put("count", String.valueOf(cooperation_count[i]));
        }}));
        Collections.sort(res, new Comparator<HashMap<String, String>>() {
            @Override
            public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                if(Integer.parseInt(o1.get("count")) > Integer.parseInt(o2.get("count")))
                    return -1;
                else if(Integer.parseInt(o1.get("count")) < Integer.parseInt(o2.get("count")))
                    return 1;
                return 0;
            };
        });
        return new ArrayList<>(res.subList(0, Math.min(res.size(), limit)));
    }
}

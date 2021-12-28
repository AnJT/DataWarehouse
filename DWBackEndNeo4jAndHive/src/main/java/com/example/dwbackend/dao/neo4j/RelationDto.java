package com.example.dwbackend.dao.neo4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

@Repository("neo4jRelationDto")
public class RelationDto {

    @Autowired
    @Qualifier("neo4jDriver")
    private Driver driver;

    public ArrayList<HashMap<String, String>> findRelation(final String startName, final String startLabel, final String endLabel, final int limit) {
        try (Session session = driver.session()) {
            return session.readTransaction(transaction -> {
                Result result;
                if (startLabel.equals("Actor") && endLabel.equals("Actor")) {
                    result = transaction.run("match (d:Actor)-[r:actor_actor]-(a:Actor) where a.actor=$data return distinct d.actor as name, r.count as count order by count DESC limit $limit;",
                            parameters("data", startName, "limit", limit));
                } else if (startLabel.equals("Actor") && endLabel.equals("Director")) {
                    result = transaction.run("match (d:Director)-[r:actor_director]-(a:Actor) where a.actor=$data return distinct d.director as name, r.count as count order by count DESC limit $limit;",
                            parameters("data", startName, "limit", limit));
                } else if (startLabel.equals("Director") && endLabel.equals("Actor")) {
                    result = transaction.run("match (d:Actor)-[r:actor_director]-(a:Director) where a.director=$data return distinct d.actor as name, r.count as count order by count DESC limit $limit;",
                            parameters("data", startName, "limit", limit));
                } else if (startLabel.equals("Director") && endLabel.equals("Director")) {
                    result = transaction.run("match (d:Director)-[r:director_director]-(a:Director) where a.director=$data return distinct d.director as name, r.count as count order by count DESC limit $limit;",
                            parameters("data", startName, "limit", limit));
                } else {
                    return new ArrayList<>();
                }
                List<Record> resultList = result.list();
                ArrayList<HashMap<String, String>> res = new ArrayList<>();
                for (Record record : resultList) {
                    res.add(new HashMap<>() {{
                        put("name", record.get("name").asString());
                        put("count", String.valueOf(record.get("count")));
                    }});
                }
                return res;
            });
        }
    }
}

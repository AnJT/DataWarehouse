package com.example.dwbackend.dao.neo4j;

import com.example.dwbackend.model.item.Score;
import com.example.dwbackend.model.item.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.neo4j.driver.Values.parameters;

@Repository("neo4jStatisticsDto")
public class StatisticsDto {

    @Autowired
    @Qualifier("neo4jDriver")
    private Driver driver;

    public int getCountByType(final String data, final String nodeLabel) {
        try (Session session = driver.session()) {
            return session.readTransaction(transaction -> {
                Result result;
                switch (nodeLabel) {
                    case "Actor":
                    case "actor":
                        result = transaction.run("match (a:Actor) where a.actor=$data " +
                                        "return a.count as count;",
                                parameters("data", data));
                        break;
                    case "Director":
                    case "director":
                        result = transaction.run("match (a:Director) where a.director=$data " +
                                        "return a.count as count;",
                                parameters("data", data));
                        break;
                    case "Label":
                    case "label":
                        result = transaction.run("match (a:Label) where a.label=$data " +
                                        "return a.count as count;",
                                parameters("data", data));
                        break;
                    default:
                        return new ArrayList<Record>();
                }
                return result.list();
            }).get(0).get("count").asInt();
        }
    }

    public int getScoreCount(final Float score, final String cmp) {
        try (Session session = driver.session()) {
            int res =  session.readTransaction(transaction -> {
                Result result;
                switch (cmp) {
                    case "equal":
                        result = transaction.run("call{" +
                                        "MATCH (m:Movie) where (m.score="+score+") " +
                                        "return distinct m.score as score, m.score_count as cnt} " +
                                        "return sum(cnt) as count;");
                        break;
                    case "greater":
                        result = transaction.run("call{" +
                                        "MATCH (m:Movie) where (m.score>$score) " +
                                        "return distinct m.score as score, m.score_count as cnt} " +
                                        "return sum(cnt) as count;",
                                parameters("score", score));
                        break;
                    case "less":
                        result = transaction.run("call{" +
                                        "MATCH (m:Movie) where (m.score<$score) " +
                                        "return distinct m.score as score, m.score_count as cnt} " +
                                        "return sum(cnt) as count;",
                                parameters("score", score));
                        break;
                    default:
                        System.out.println("error");
                        return new ArrayList<Record>();
                }
                return result.list();
            }).get(0).get("count").asInt();
            return res;
        }
    }

    public int queryTimeByYear(final String year, final String cmp) {
        try (Session session = driver.session()) {
            return session.readTransaction(transaction -> {
                Result result;
                switch (cmp) {
                    case "equal":
                        result = transaction.run("match(t:Time) where t.year=$year return distinct t.year_count as count;",
                                parameters("year", Integer.valueOf(year)));
                        break;
                    case "greater":
                        result = transaction.run("call{\n" +
                                        "MATCH (t:Time)\n" +
                                        "where (t.year>$year)\n" +
                                        "return distinct t.year as year, t.year_count as cnt}\n" +
                                        "return sum(cnt) as count;",
                                parameters("year", Integer.valueOf(year)));
                        break;
                    case "less":
                        result = transaction.run("call{\n" +
                                        "MATCH (t:Time)\n" +
                                        "where (t.year<$year)\n" +
                                        "return distinct t.year as year, t.year_count as cnt}\n" +
                                        "return sum(cnt) as count;",
                                parameters("year", Integer.valueOf(year)));
                        break;
                    default:
                        return new ArrayList<Record>();
                }
                return result.list();
            }).get(0).get("count").asInt();
        }
    }

    public int queryTimeBySeason(final String year, final String season, final String cmp) {
        try (Session session = driver.session()) {
            return session.readTransaction(transaction -> {
                Result result;
                switch (cmp) {
                    case "equal":
                        result = transaction.run("match(t:Time) where t.year=$year and t.season=$season return distinct t.season_count as count;",
                                parameters("year", Integer.valueOf(year), "season", Integer.valueOf(season)));
                        break;
                    case "greater":
                        result = transaction.run("call{\n" +
                                        "MATCH (t1:Time)\n" +
                                        "where (t1.year=$year and t1.season>$season)\n" +
                                        "return distinct t1.year as year, t1.season as season, t1.season_count as cnt\n" +
                                        "union\n" +
                                        "MATCH (t2:Time)\n" +
                                        "where (t2.year>$year)\n" +
                                        "return distinct t2.year as year, t2.season as season, t2.season_count as cnt}\n" +
                                        "return sum(cnt) as count;",
                                parameters("year", Integer.valueOf(year), "season", Integer.valueOf(season)));
                        break;
                    case "less":
                        result = transaction.run("call{\n" +
                                        "MATCH (t1:Time)\n" +
                                        "where (t1.year=$year and t1.season<$season)\n" +
                                        "return distinct t1.year as year, t1.season as season, t1.season_count as cnt\n" +
                                        "union\n" +
                                        "MATCH (t2:Time)\n" +
                                        "where (t2.year<$year)\n" +
                                        "return distinct t2.year as year, t2.season as season, t2.season_count as cnt}\n" +
                                        "return sum(cnt) as count;",
                                parameters("year", Integer.valueOf(year), "season", Integer.valueOf(season)));
                        break;
                    default:
                        return new ArrayList<Record>();
                }
                return result.list();
            }).get(0).get("count").asInt();
        }
    }

    public int queryTimeByMonth(final String year, final String month, final String cmp) {
        try (Session session = driver.session()) {
            return session.readTransaction(transaction -> {
                Result result;
                switch (cmp) {
                    case "equal":
                        result = transaction.run("match(t:Time) where t.year=$year and t.month=$month return distinct t.month_count as count;",
                                parameters("year", Integer.valueOf(year), "month", Integer.valueOf(month)));
                        break;
                    case "greater":
                        result = transaction.run("call{\n" +
                                        "MATCH (t1:Time)\n" +
                                        "where (t1.year=$year and t1.month>$month)\n" +
                                        "return distinct t1.year as year, t1.month as month, t1.month_count as cnt\n" +
                                        "union\n" +
                                        "MATCH (t2:Time)\n" +
                                        "where (t2.year>$year)\n" +
                                        "return distinct t2.year as year, t2.month as month, t2.month_count as cnt}\n" +
                                        "return sum(cnt) as count;",
                                parameters("year", Integer.valueOf(year), "month", Integer.valueOf(month)));
                        break;
                    case "less":
                        result = transaction.run("call{\n" +
                                        "MATCH (t1:Time)\n" +
                                        "where (t1.year=$year and t1.month<$month)\n" +
                                        "return distinct t1.year as year, t1.month as month, t1.month_count as cnt\n" +
                                        "union\n" +
                                        "MATCH (t2:Time)\n" +
                                        "where (t2.year<$year)\n" +
                                        "return distinct t2.year as year, t2.month as month, t2.month_count as cnt}\n" +
                                        "return sum(cnt) as count;",
                                parameters("year", Integer.valueOf(year), "month", Integer.valueOf(month)));
                        break;
                    default:
                        return new ArrayList<Record>();
                }
                return result.list();
            }).get(0).get("count").asInt();
        }
    }

    public int queryTimeByDay(final String year, final String month, final String day, final String cmp) {
        try (Session session = driver.session()) {
            return session.readTransaction(transaction -> {
                Result result;
                switch (cmp) {
                    case "equal":
                        result = transaction.run("match(t:Time) where t.year=$year and t.month=$month and t.day=$day return distinct t.day_count as count;",
                                parameters("year", Integer.valueOf(year), "month", Integer.valueOf(month), "day", Integer.valueOf(day)));
                        break;
                    case "greater":
                        result = transaction.run("call{\n" +
                                        "MATCH (t1:Time)\n" +
                                        "where (t1.year=$year and t1.month=$month and t1.day>$day)\n" +
                                        "return distinct t1.year as year, t1.month as month, t1.day as day, t1.day_count as cnt\n" +
                                        "union\n" +
                                        "MATCH (t2:Time)\n" +
                                        "where (t2.year=$year and t2.month>$month)\n" +
                                        "return distinct t2.year as year, t2.month as month, t2.day as day, t2.day_count as cnt\n" +
                                        "union\n" +
                                        "MATCH (t3:Time)\n" +
                                        "where (t3.year>$year)\n" +
                                        "return distinct t3.year as year, t3.month as month, t3.day as day, t3.day_count as cnt}" +
                                        "return sum(cnt) as count;",
                                parameters("year", Integer.valueOf(year), "month", Integer.valueOf(month), "day", Integer.valueOf(day)));
                        break;
                    case "less":
                        result = transaction.run("call{\n" +
                                        "MATCH (t1:Time)\n" +
                                        "where (t1.year=$year and t1.month=$month and t1.day<$day)\n" +
                                        "return distinct t1.year as year, t1.month as month, t1.day as day, t1.day_count as cnt\n" +
                                        "union\n" +
                                        "MATCH (t2:Time)\n" +
                                        "where (t2.year=$year and t2.month<$month)\n" +
                                        "return distinct t2.year as year, t2.month as month, t2.day as day, t2.day_count as cnt\n" +
                                        "union\n" +
                                        "MATCH (t3:Time)\n" +
                                        "where (t3.year<$year)\n" +
                                        "return distinct t3.year as year, t3.month as month, t3.day as day, t3.day_count as cnt}" +
                                        "return sum(cnt) as count;",
                                parameters("year", Integer.valueOf(year), "month", Integer.valueOf(month), "day", Integer.valueOf(day)));
                        break;
                    default:
                        return new ArrayList<Record>();
                }
                return result.list();
            }).get(0).get("count").asInt();
        }
    }

    public ArrayList<Score> getAllScore() {
        try (Session session = driver.session()) {
            List<Record> recordList = session.readTransaction(transaction -> {
                Result result = transaction.run("match(m:Movie) return distinct m.score as score, m.score_count as score_count order by score desc;");
                return result.list();
            });
            ArrayList<Score> arrayList = new ArrayList<>();
            for (Record record : recordList) {
                Score score = new Score();
                try{
                    score.setScore((float) record.get("score").asDouble());
                    score.setCount(record.get("score_count").asInt());
                    arrayList.add(score);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            return arrayList;
        }
    }

    public ArrayList<Statistics> getAllLabel(int limit) {
        try (Session session = driver.session()) {
            List<Record> recordList = session.readTransaction(transaction -> {
                Result result = transaction.run("match(l:Label) return distinct l.label as label, l.count as count order by count desc limit " + limit + ";");
                return result.list();
            });
            ArrayList<Statistics> arrayList = new ArrayList<>();
            for (Record record : recordList){
                Statistics statistics = new Statistics();
                try{
                    statistics.setName(record.get("label").asString());
                    statistics.setCount(record.get("count").asInt());
                    arrayList.add(statistics);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            return arrayList;
        }
    }

    public ArrayList<Statistics> getAllActor(int limit) {
        try (Session session = driver.session()) {
            List<Record> recordList = session.readTransaction(transaction -> {
                Result result = transaction.run("match(a:Actor) where a.actor<>'Various' return distinct a.actor as name, a.count as count order by count desc limit " + limit + ";");
                return result.list();
            });
            ArrayList<Statistics> arrayList = new ArrayList<>();
            for (Record record : recordList){
                Statistics statistics = new Statistics();
                try{
                    statistics.setName(record.get("name").asString());
                    statistics.setCount(record.get("count").asInt());
                    arrayList.add(statistics);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            return arrayList;
        }
    }

    public ArrayList<Statistics> getAllDirector(int limit) {
        try (Session session = driver.session()) {
            List<Record> recordList = session.readTransaction(transaction -> {
                Result result = transaction.run("match(d:Director) where d.director<>'Various' return distinct d.director as name, d.count as count order by count desc limit " + limit + ";");
                return result.list();
            });
            ArrayList<Statistics> arrayList = new ArrayList<>();
            for (Record record : recordList){
                Statistics statistics = new Statistics();
                try{
                    statistics.setName(record.get("name").asString());
                    statistics.setCount(record.get("count").asInt());
                    arrayList.add(statistics);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            return arrayList;
        }
    }
}

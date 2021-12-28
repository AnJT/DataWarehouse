package com.example.dwbackend.dao.neo4j;

import com.example.dwbackend.model.item.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

@Repository("neo4jQueryDao")
public class QueryDao {

    @Autowired
    @Qualifier("neo4jDriver")
    private Driver driver;

    public ArrayList<Movie> queryByTitle(final String title, final int limit) {
        try (Session session = driver.session()) {
            return Utils.recordToMovie(session.readTransaction(transaction -> {
                Result result = transaction.run("match (a:Movie) where a.title=$title return " +
                                "a.asin as asin, a.score as score, " +
                                "a.title as title limit $limit;",
                        parameters("title", title, "limit", limit));
                return result.list();
            }));
        }
    }

    public ArrayList<Movie> queryMovieByDirector(final String director, final int limit) {
        try (Session session = driver.session()) {
            return Utils.recordToMovie(session.readTransaction(transaction -> {
                Result result = transaction.run("match (d:Director)-[]-(a:Movie) where d.director=$director return " +
                                "a.asin as asin, a.score as score, " +
                                "a.title as title limit $limit;",
                        parameters("director", director, "limit", limit));
                return result.list();
            }));
        }
    }

    public ArrayList<Movie> queryMovieByActor(final String actor, final int limit) {
        try (Session session = driver.session()) {
            return Utils.recordToMovie(session.readTransaction(transaction -> {
                Result result = transaction.run("match (d:Actor)-[]-(a:Movie) where d.actor=$actor return " +
                                "a.asin as asin, a.score as score, " +
                                "a.title as title limit $limit;",
                        parameters("actor", actor, "limit", limit));
                return result.list();
            }));
        }
    }

    public ArrayList<Movie> queryMovieByLabel(final String label, final int limit) {
        try (Session session = driver.session()) {
            return Utils.recordToMovie(session.readTransaction(transaction -> {
                Result result = transaction.run("match (d:Label)-[]-(a:Movie) where d.label=$label return " +
                                "a.asin as asin, a.score as score, " +
                                "a.title as title limit $limit;",
                        parameters("label", label, "limit", limit));
                return result.list();
            }));
        }
    }

    public ArrayList<Movie> queryMovieByScore(final Float score, final String cmp, final int limit) {
        try (Session session = driver.session()) {
            return Utils.recordToMovie(session.readTransaction(transaction -> {
                Result result;
                switch (cmp) {
                    case "equal":
                        result = transaction.run("match (a:Movie) where a.score=" + score + " return " +
                                        "a.asin as asin, a.score as score, " +
                                        "a.title as title limit $limit;",
                                parameters("limit", limit));
                        break;
                    case "greater":
                        result = transaction.run("match (a:Movie) where a.score>" + score + " return " +
                                        "a.asin as asin, a.score as score, " +
                                        "a.title as title limit $limit;",
                                parameters("limit", limit));
                        break;
                    case "less":
                        result = transaction.run("match (a:Movie) where a.score<" + score + " return " +
                                        "a.asin as asin, a.score as score, " +
                                        "a.title as title limit $limit;",
                                parameters("limit", limit));
                        break;
                    default:
                        return new ArrayList<>();
                }
                return result.list();
            }));
        }
    }

}

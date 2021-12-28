package com.example.dw_backend.model.mysql;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 评论实体类
 *
 */
@Data
@EqualsAndHashCode(exclude = {"user", "movie"})
@Entity
@Table(name = "review")
@org.hibernate.annotations.Table(appliesTo = "review", comment = "电影评论表")
public class Review {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Movie movie;

    private double score;

    private long timeStamp;

    @Lob
    private String summary;

    @Lob
    private String text;

    public int getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public double getScore() {
        return score;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getSummary() {
        return summary;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }
}

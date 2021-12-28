package com.example.dw_backend.model.mysql;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

/**
 * 电影实体类
 */
@Data
@EqualsAndHashCode(exclude = {"time", "score"})
@Entity
@org.hibernate.annotations.Table(appliesTo = "movie", comment = "电影表")
public class Movie {
    @Id
    @GeneratedValue
    private String productId;

    @Column(nullable = true)
    private String title;

    @ManyToOne
    private Time time;

    @ManyToOne
    private Score score;

}

package com.example.dw_backend.model.mysql;

import lombok.Data;
import javax.persistence.*;

/**
 * 电影溯源实体类
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "movie_source", comment = "电影溯源表")
public class MovieSource {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = true)
    private String asin;

    @Column(nullable = true)
    private String neighbor;
}

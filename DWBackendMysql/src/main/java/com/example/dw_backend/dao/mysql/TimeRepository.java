package com.example.dw_backend.dao.mysql;

import com.example.dw_backend.model.mysql.Time;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 */
@Repository
public interface TimeRepository extends CrudRepository<Time, Long> {

    /**
     * 写一个查询X年以后/前的电影总数 返回电影数量
     *
     * @param year
     * @param after 是：查询该年份及之后的，否：查询该年份以前的
     * @return
     */
    @Query(value = "call find_movie_by_year(:ye, :after);", nativeQuery = true)
    List<Integer> getMovieCountByYear(@Param("ye") int year, @Param("after") String after);

    /**
     * 写一个查询X年Y月以后/前的电影总数 返回电影数量
     *
     * @param year
     * @param month
     * @param after
     * @return
     */
    @Query(value = "call find_movie_by_month(:ye, :mon, :after);", nativeQuery = true)
    List<Integer> getMovieCountByMonth(@Param("ye") int year, @Param("mon") int month, @Param("after") String after);


    /**
     * 写一个查询X年Y月Z日以后/前的电影总数 返回电影数量
     *
     * @param year
     * @param month
     * @param day
     * @param after
     * @return
     */
    @Query(value = "call find_movie_by_day(:ye, :mon, :da, :after);", nativeQuery = true)
    List<Integer> getMovieCountByDay(@Param("ye") int year, @Param("mon") int month, @Param("da") int day, @Param("after") String after);

    /**
     * 查询X年W季度的电影总数 返回电影数量
     *
     * @param year
     * @param season
     * @return
     */
    @Query(value = "call find_movie_by_season(:ye, :sea);", nativeQuery = true)
    List<Integer> getMovieCountBySeason(@Param("ye") int year, @Param("sea") int season);


}

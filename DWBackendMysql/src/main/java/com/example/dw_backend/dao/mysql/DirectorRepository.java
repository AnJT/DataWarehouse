package com.example.dw_backend.dao.mysql;

import com.example.dw_backend.model.mysql.Director;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 */
@Repository
public interface DirectorRepository extends CrudRepository<Director, Long> {

    /**
     * 根据导演名查找电影
     *
     * @param director
     * @return
     */
    @Query(value = "call find_director_movie(:dir);", nativeQuery = true)
    List<Director> getMovieCount(@Param("dir") String director);

    /**
     * 根据导演名查找合作的演员
     *
     * @param director
     * @return
     */
    @Query(value = "call find_actor_by_director(:dir);", nativeQuery = true)
    List<Object> getActorList(@Param("dir") String director);

    /**
     * 找根据导演查询合作导演
     *
     * @param director
     * @return
     */
    @Query(value = "call find_director_by_director(:dir);", nativeQuery = true)
    List<Object> getDirectorList(@Param("dir") String director);

    @Query(value = "call find_all_director();", nativeQuery = true)
    List<Director> findAll();

}

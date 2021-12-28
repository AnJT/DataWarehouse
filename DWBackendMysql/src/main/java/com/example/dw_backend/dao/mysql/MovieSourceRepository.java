package com.example.dw_backend.dao.mysql;

import com.example.dw_backend.model.mysql.MovieSource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieSourceRepository extends CrudRepository<MovieSource, Long> {

    @Query(value = "call find_movie_source(:asinn);", nativeQuery = true)
    List<MovieSource> getMovieSource(@Param("asinn") String asinn);
}

package com.example.dw_backend.service.mysql;

import com.example.dw_backend.dao.mysql.LabelRepository;
import com.example.dw_backend.model.mysql.Actor;
import com.example.dw_backend.model.mysql.Director;
import com.example.dw_backend.model.mysql.Label;
import com.example.dw_backend.model.mysql.Movie;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class LabelService {
    private final LabelRepository labelRepository;
    private long movieTime;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    /**
     * 把查询返回值变成MovieList类型
     *
     * @param label
     * @return
     */
    public List<Movie> parsingLabelMovie(String label) {
        long startTime = System.currentTimeMillis();    //获取开始时间
        List<Label> labelList = labelRepository.getMovieCount(label);
        long endTime = System.currentTimeMillis();    //获取开始时间
        this.movieTime = endTime - startTime;

        List<Movie> movieList = new ArrayList<>();
        for (Label label1 : labelList) {
            movieList.add(label1.getMovie());
        }
        return movieList;
    }

    public HashMap<String, Integer> findAll(int limit) {
        long startTime = System.currentTimeMillis();
        List<Label> directorList = this.labelRepository.findAll();
        long endTime = System.currentTimeMillis();
        this.movieTime = endTime - startTime;
        System.out.println(this.movieTime);
        HashMap<String, Integer> temp1 = new HashMap<>();
        for (int i = 0; i < directorList.size(); i++) {
            String name = directorList.get(i).getLabelName();
            int count = directorList.get(i).getMovieCount();
            System.out.println(name);
            temp1.put(name, count);
//            if (temp1.size() >= limit) {
//                System.out.println("reach limit");
//                return temp1;
//            }
        }
        System.out.println(temp1);
        return temp1;
    }

    public long getMovieTime() {
        return movieTime;
    }
}

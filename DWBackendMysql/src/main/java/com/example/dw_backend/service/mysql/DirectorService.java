package com.example.dw_backend.service.mysql;

import com.example.dw_backend.dao.mysql.DirectorRepository;
import com.example.dw_backend.model.mysql.Director;
import com.example.dw_backend.model.mysql.Movie;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class DirectorService {
    private final DirectorRepository directorRepository;
    private long actorTime;
    private long directorTime;
    private long movieTime;

    public DirectorService(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    /**
     * 把查询返回值变成MovieList类型
     *
     * @param directorName
     * @return
     */
    public List<Movie> parsingDirectorMovie(String directorName) {

        long startTime = System.currentTimeMillis();    //获取开始时间
        List<Director> directors = directorRepository.getMovieCount(directorName);
        long endTime = System.currentTimeMillis();    //获取开始时间
        this.movieTime = endTime - startTime;

        List<Movie> movieList = new ArrayList<>();
        for (Director dir : directors) {
            movieList.add(dir.getMovie());
        }
        return movieList;
    }


    /**
     * 返回ActorList
     *
     * @param director
     * @return
     */
    public List<HashMap<String, String>> parsingGetActorList(String director) {

        List<HashMap<String, String>> result = new ArrayList<>();

        long startTime = System.currentTimeMillis();    //获取开始时间
        List<Object> actorList = this.directorRepository.getActorList(director);
        long endTime = System.currentTimeMillis();    //获取开始时间
        this.actorTime = endTime - startTime;

        for (Object row : actorList) {
            HashMap<String, String> temp1 = new HashMap<>();
            Object[] cells = (Object[]) row;
            temp1.put("actorName", String.valueOf(cells[0]));
            temp1.put("cooperation", String.valueOf(cells[1]));
            result.add(temp1);
        }
        return result;
    }


    /**
     * 返回directorList
     *
     * @param director
     * @return
     */
    public List<HashMap<String, String>> parsingGetDirectorList(String director) {

        List<HashMap<String, String>> result = new ArrayList<>();

        long startTime = System.currentTimeMillis();    //获取开始时间
        List<Object> directorList = this.directorRepository.getDirectorList(director);
        long endTime = System.currentTimeMillis();    //获取开始时间
        this.directorTime = endTime - startTime;

        for (Object row : directorList) {
            HashMap<String, String> temp1 = new HashMap<>();
            Object[] cells = (Object[]) row;
            temp1.put("directorName", String.valueOf(cells[0]));
            temp1.put("cooperation", String.valueOf(cells[1]));
            result.add(temp1);
        }
        return result;
    }

//    public HashMap<String, String> findAll(int limit) {
//        List<Director> directorList = this.directorRepository.findAll();
//        HashMap<String, String> temp1 = new HashMap<>();
//        for (int i = 0; i < directorList.size(); i++) {
//            String name = directorList.get(i).getDirectorName();
//            String count = directorList.get(i).getMovieCount();
//            temp1.put(name, count);
//            if (temp1.size() >= limit) {
//                return temp1;
//            }
//        }
//        return temp1;
//    }

    public HashMap<String, Integer> findAll(int limit) {
        long startTime = System.currentTimeMillis();
        List<Director> directorList = this.directorRepository.findAll();
        long endTime = System.currentTimeMillis();
        this.movieTime = endTime - startTime;
        HashMap<String, Integer> temp1 = new HashMap<>();
        for (int i = 0; i < directorList.size(); i++) {
            String name = directorList.get(i).getDirectorName();
            String count = directorList.get(i).getMovieCount();
            temp1.put(name, Integer.valueOf(count));
            if (temp1.size() >= limit) {
                return temp1;
            }
        }
        return temp1;
    }


    public long getActorTime() {
        return actorTime;
    }

    public long getDirectorTime() {
        return directorTime;
    }

    public long getMovieTime() {
        return movieTime;
    }
}

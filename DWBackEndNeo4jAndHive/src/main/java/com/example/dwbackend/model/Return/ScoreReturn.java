package com.example.dwbackend.model.Return;

import com.example.dwbackend.model.item.Score;
import lombok.Data;

import java.util.ArrayList;

@Data
public class ScoreReturn {
    long time;
    ArrayList<Score> scores;

    public ScoreReturn(long time, ArrayList<Score> scores) {
        this.time = time;
        this.scores = scores;
    }

}
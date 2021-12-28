package com.example.dw_backend.model;

import com.example.dw_backend.model.mysql.Score;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

@Data
public class ScoreReturn {
    long time;
    ArrayList<Score> scores;

    public ScoreReturn(ArrayList<Score> scores, long time) {
        this.scores = scores;
        this.time = time;
    }
}


package com.example.dw_backend.model;

import lombok.Data;

import java.util.HashMap;

@Data
public class StatisticsReturn {
    long time;
    HashMap<String, Integer> staInfo;

    public StatisticsReturn(HashMap<String, Integer> staInfo, long time) {
        System.out.println(time);
        System.out.println(staInfo);
        this.staInfo = staInfo;
        this.time = time;
    }
}

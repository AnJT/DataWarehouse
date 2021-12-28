package com.example.dwbackend.model.Return;

import com.example.dwbackend.model.item.Statistics;
import lombok.Data;

import java.util.ArrayList;

@Data
public class StatisticsReturn {
    long time;
    ArrayList<Statistics> statistics;

    public StatisticsReturn(long time, ArrayList<Statistics> statistics) {
        this.time = time;
        this.statistics = statistics;
    }

}

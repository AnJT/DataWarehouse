package com.example.dwbackend.model.Return;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class RelationReturn {
    long time;
    List<HashMap<String, String>> relationInfo;

    public RelationReturn(long time, List<HashMap<String, String>> relationInfo){
        this.time = time;
        this.relationInfo = relationInfo;
    }
}

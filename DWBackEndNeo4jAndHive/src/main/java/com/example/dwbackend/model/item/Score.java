package com.example.dwbackend.model.item;

import lombok.Data;

@Data
public class Score {
    private float score;
    private int count;

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

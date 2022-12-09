package com.guwen.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Details {
    private Integer userId;
    private Integer wordNum;
    private Integer reviewNum;
    private String studyDate;

    public Details(Integer userId, Integer wordNum, String studyDate) {
        this.userId = userId;
        this.wordNum = wordNum;
        this.studyDate = studyDate;
    }
}

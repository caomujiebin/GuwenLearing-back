package com.guwen.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private Integer userId;
    private Integer wordId;
    private String firstDate;
    private String lastDate;
    private Integer gapDay;
    private Integer reviewStatus;
    private Integer betweenDay;

    public Review(Integer userId, int wordId,String lastDate, int reviewStatus) {
        this.userId = userId;
        this.wordId = wordId;
        this.lastDate = lastDate;
        this.reviewStatus = reviewStatus;
    }
}

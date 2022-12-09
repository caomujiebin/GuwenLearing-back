package com.guwen.model.Dto;

import lombok.Data;

@Data
public class ReviewDto {
    private Integer userId;
    private String email;
    private String userName;
    private String newWordsNum;
    private String reviewWordsNum;
}

package com.guwen.model.Dto;

import lombok.Data;

import java.util.List;

@Data
public class StudyDto {
    private Integer userId;
    private Integer bookId;
    private Integer bookPage;
    private Integer newWordsNum;
    private Integer reviewWordsNum;
    private Integer studyType;
    private List<Integer> forgetList;
    private List<Integer> wordIdList;
}

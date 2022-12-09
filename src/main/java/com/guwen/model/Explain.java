package com.guwen.model;

import lombok.Data;

@Data
public class Explain {
    private Integer id;
    private Integer wordId;
    private String wordExplain;
    private String wordSentence;
    private String wordInBook;
    private String wordType;
}

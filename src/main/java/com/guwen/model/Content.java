package com.guwen.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Content {
    private Integer wordId;
    private String wordName;
    private String wordExplain;
    private String wordSentence;
    private String wordInBook;
    private String wordType;
}

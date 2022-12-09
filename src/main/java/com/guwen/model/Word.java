package com.guwen.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("word")
public class Word {
    private Integer bookId;
    @TableId(value = "wordId")
    private Integer wordId;
    private String wordName;
}

package com.guwen.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("book")
public class Book {
    @TableId(value = "bookId")
    private Integer bookId;
    private String bookName;
    private Integer bookNum;
    private String bookImg;
}

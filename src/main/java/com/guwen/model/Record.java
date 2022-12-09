package com.guwen.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Record {
    private Integer userId;
    private Integer bookId;
    private Integer bookPage;
}

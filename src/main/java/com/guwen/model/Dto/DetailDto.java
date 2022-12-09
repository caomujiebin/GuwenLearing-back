package com.guwen.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailDto {
    private Integer userId;
    private Integer wordNum;
    private String year;
    private String month;
    private String day;
    private Integer reviewNum;
}

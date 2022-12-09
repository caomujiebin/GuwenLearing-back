package com.guwen.model.Dto;

import com.guwen.model.Word;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordDto {
    private Integer id;
    private String name;
    private String nodeId;

    public WordDto(String name,String nodeId){
        this.name = name;
        this.nodeId = nodeId;
    }
}

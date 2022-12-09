package com.guwen.service;


import com.guwen.model.Book;
import com.guwen.model.Dto.StudyDto;
import com.guwen.model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface StudyService {
    HashMap<String,Object> InitialStudy(StudyDto study);

    List<Object> everyStudyRecord(User user);


    List<Map<String, Object>> SearchByWord(String keyword,String type,Integer pageIndex,Integer pageSize) throws IOException;
}

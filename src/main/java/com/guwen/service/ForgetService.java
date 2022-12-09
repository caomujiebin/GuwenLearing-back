package com.guwen.service;


import com.guwen.model.Dto.StudyDto;

import java.text.ParseException;
import java.util.HashMap;

public interface ForgetService {
    HashMap<String, Object> saveWordRecord(StudyDto study) throws ParseException;
}
